# MCP Server Advanced 啟動腳本

# 切換到專案目錄
Set-Location -Path "E:\Spring_AI_BOOK\code-examples\chapter9-mcp-integration\chapter9-mcp-server-advanced"

Write-Host "設定 Java 21 環境..." -ForegroundColor Green
$env:JAVA_HOME="D:\java\jdk-21"
$env:Path="D:\java\jdk-21\bin;D:\apache-maven-3.9.11\bin;$env:Path"

Write-Host "Java 版本:" -ForegroundColor Cyan
& "D:\java\jdk-21\bin\java.exe" -version

Write-Host "`n啟動 MCP Server Advanced..." -ForegroundColor Green
Write-Host "應用程式將在 http://localhost:8081 啟動" -ForegroundColor Yellow
Write-Host "H2 Console: http://localhost:8081/h2-console" -ForegroundColor Yellow
Write-Host "健康檢查 API: http://localhost:8081/api/admin/health" -ForegroundColor Yellow
Write-Host "`n按 Ctrl+C 可停止服務`n" -ForegroundColor Cyan

& "D:\apache-maven-3.9.11\bin\mvn.cmd" spring-boot:run