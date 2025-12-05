@echo off
REM 設定 Java 21 環境變數
set JAVA_HOME=D:\java\jdk-21
set Path=D:\java\jdk-21\bin;%Path%

REM 設定 OpenAI API Key
set OPENAI_API_KEY=your-openai-api-key

REM 進入項目目錄
cd /d "%~dp0"

REM 運行應用
echo 啟動 chapter6-memory-core 應用程序...
mvn spring-boot:run

pause
