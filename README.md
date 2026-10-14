# 예린이 도박일지 — 새로 시작하는 GitHub APK 프로젝트

이 ZIP은 **별도 상위 폴더 없이 GitHub에 올릴 수 있는 구조**입니다. 기존 실패한 저장소는 삭제하지 않아도 됩니다.

## 컴퓨터에서 5단계

1. [GitHub 새 저장소](https://github.com/new)로 가서 `yerin-diary-new` 이름의 **Private** 저장소를 만들고, README 자동 생성은 선택하지 마세요.
2. `yerin_diary_START_OVER.zip`을 **압축 풀기** 한 뒤, 나온 폴더 안으로 들어가세요.
3. GitHub 새 저장소에서 **uploading an existing file** 클릭. 컴퓨터에서 **폴더 자체가 아니라, 폴더 안의 파일과 폴더 전부**를 Ctrl+A로 선택해 GitHub 업로드 영역에 드래그하세요.
4. GitHub 파일 목록에 `app`, `.github`, `build.gradle`, `settings.gradle`, `index.html`이 **최상위**에 보이면 **Commit changes**를 누르세요.
5. **Actions → Build Android APK**에서 성공한 실행을 열고, 아래쪽 **Artifacts → yerin-gambling-diary-apk**를 다운로드해 APK를 설치하세요.

**중요:** Jekyll이나 Java Package 기본 템플릿의 **Configure는 누르지 마세요.** 이 프로젝트에 자동빌드 작업이 이미 들어있습니다.

빌드 오류가 나면 실패한 `Build debug APK`의 **첫 번째 빨간 X가 표시된 단계 로그**를 알려주세요. Android Studio 설치는 필요 없습니다.

## 기능

- 날짜 선택: 일진(60갑자)과 사용자가 제공한 1~5점 명리학 상징점수 자동 표시
- 카지노 방문 장소, 베팅 금액, 순이익 / 순손실 입력
- 총 누적 손익, 그래프, 기록 수정/삭제, CSV 다운로드, JSON 백업 및 복원
- 통화 AUD; 데이터는 기기에만 로컬 저장됩니다. 앱 삭제 전에 백업하세요.
- 상징점수는 도박의 승률 또는 기대수익을 예측하는 수치가 아닙니다.

## 개발 구성

- Android Gradle Plugin 8.7.3 / Gradle 8.9 / Java 17 / Android SDK 35
- APK는 디버그 설치용이며 Google Play 배포용이 아닙니다.
- Actions의 Android SDK 설치에는 `android-actions/setup-android@v4`를 사용합니다.
- GitHub에서 APK 실제 빌드에 성공했는지는 아직 검증되지 않았습니다.
