package com.yerin.diary;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
    private static final int PICK_JSON = 101;
    private static final int SAVE_DOCUMENT = 102;
    private WebView webView;
    private ValueCallback<Uri[]> fileCallback;
    private String pendingFileData;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(0xFF14223C);
        getWindow().setNavigationBarColor(0xFFF5F7FB);
        webView = new WebView(this);
        setContentView(webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        webView.addJavascriptInterface(new FilesBridge(), "AndroidFile");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onShowFileChooser(WebView view,
                    ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (fileCallback != null) fileCallback.onReceiveValue(null);
                fileCallback = callback;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/json");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try { startActivityForResult(Intent.createChooser(intent,"백업 파일 선택"), PICK_JSON); }
                catch (Exception ex) { fileCallback.onReceiveValue(null); fileCallback=null; return false; }
                return true;
            }
        });
        try {
            InputStream input = getAssets().open("index.html");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            while ((n = input.read(buf)) >= 0) baos.write(buf, 0, n);
            input.close();
            String html = baos.toString("UTF-8");
            // Stable HTTPS-like origin, so localStorage persists without any server.
            webView.loadDataWithBaseURL("https://appassets.androidplatform.net/", html,
                    "text/html", "UTF-8", null);
        } catch (Exception ex) {
            Toast.makeText(this, "앱 화면을 열 수 없습니다.", Toast.LENGTH_LONG).show();
        }
    }

    public class FilesBridge {
        @JavascriptInterface public void saveTextFile(String name, String text, String mime) {
            runOnUiThread(() -> {
                pendingFileData = text;
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/csv".equals(mime.split(";")[0]) ? "text/csv" : "application/json");
                intent.putExtra(Intent.EXTRA_TITLE, name);
                try { startActivityForResult(intent, SAVE_DOCUMENT); }
                catch (Exception ex) { Toast.makeText(MainActivity.this, "저장 창을 열 수 없습니다.", Toast.LENGTH_LONG).show(); }
            });
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_JSON) {
            if (fileCallback != null) {
                Uri uri = resultCode == RESULT_OK && data != null ? data.getData() : null;
                fileCallback.onReceiveValue(uri == null ? null : new Uri[]{uri});
                fileCallback = null;
            }
        } else if (requestCode == SAVE_DOCUMENT) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null && pendingFileData != null) {
                try (OutputStream output = getContentResolver().openOutputStream(data.getData())) {
                    if (output == null) throw new Exception("Cannot open output");
                    output.write(pendingFileData.getBytes(StandardCharsets.UTF_8));
                    Toast.makeText(this, "파일을 저장했습니다.", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(this, "파일 저장에 실패했습니다.", Toast.LENGTH_LONG).show();
                }
            }
            pendingFileData = null;
        }
    }

    @Override protected void onDestroy() {
        if (webView != null) webView.destroy();
        super.onDestroy();
    }
}
