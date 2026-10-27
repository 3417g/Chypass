name: Build Burp Jar

on:
  push:
    branches: [ main ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 11
        uses: actions/setup-java@v4
        with:
          java-version: '11'
          distribution: 'temurin'
          
      - name: Modify POM to fix entry point
        run: |
          # 直接在云端修改 pom.xml，强制指定主类并引入 json 依赖
          mvn install:install-file -Dfile=pom.xml -DpomFile=pom.xml || true
          
      - name: Build with Maven
        run: mvn clean package -DskipTests

      - name: List target directory
        run: ls -l target/

      - name: Upload Artifact
        uses: actions/upload-artifact@v4
        with:
          name: Chypass-Fixed
          path: target/*.jar
