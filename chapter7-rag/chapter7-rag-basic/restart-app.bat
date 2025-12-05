@echo off
echo === Restarting Spring AI RAG Application ===
echo.

REM Set Java Environment
set JAVA_HOME=D:\java\jdk-21
set PATH=D:\java\jdk-21\bin;%PATH%

REM Set OpenAI API Key
set OPENAI_API_KEY=your-openai-api-key

echo Starting application with fixed Neo4j password...
echo Application will be available at http://localhost:8080
echo Neo4j password is set to default: test1234
echo.

mvn spring-boot:run
