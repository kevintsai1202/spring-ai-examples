@echo off
REM ========================================
REM MCP Server 執行腳本
REM ========================================

echo ========================================
echo   設置環境變數
echo ========================================

set JAVA_HOME=D:\java\jdk-21
set Path=D:\java\jdk-21\bin;%Path%

echo JAVA_HOME=%JAVA_HOME%
echo.

echo ========================================
echo   檢查 Java 版本
echo ========================================

java -version
echo.

echo ========================================
echo   啟動 MCP Server (SSE 模式)
echo ========================================
echo 端點: http://localhost:8080/mcp/message
echo 按 Ctrl+C 停止服務
echo.

cd /d "E:\Spring_AI_BOOK\code-examples\chapter9-mcp-integration\chapter9-mcp-server-tools-resources"
D:\apache-maven-3.9.11\bin\mvn.cmd spring-boot:run
