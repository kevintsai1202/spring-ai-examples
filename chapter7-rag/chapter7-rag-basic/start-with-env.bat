@echo off
echo === Starting Spring AI RAG Basic System ===
echo.

REM Set Java Environment
set JAVA_HOME=D:\java\jdk-21
set PATH=D:\java\jdk-21\bin;%PATH%

REM Set API Keys from .env file
set OPENAI_API_KEY=your-openai-api-key
set NEO4J_PASSWORD=test1234

echo Environment variables set successfully
echo.
echo Starting Spring Boot application...
echo Application will be available at http://localhost:8080
echo.

mvn spring-boot:run
