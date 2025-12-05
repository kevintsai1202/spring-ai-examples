# Spring AI RAG 基礎系統啟動腳本

# 設定 Java 環境
$env:JAVA_HOME = "D:\java\jdk-21"
$env:Path = "D:\java\jdk-21\bin;$env:Path"

# 設定 OpenAI API Key
$env:OPENAI_API_KEY = "your-openai-api-key"

Write-Host "環境變數設定完成" -ForegroundColor Green
Write-Host "  JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Gray
Write-Host "  OPENAI_API_KEY: ****已設定****" -ForegroundColor Gray

# 確認 Neo4j 是否運行
Write-Host "檢查 Neo4j 容器狀態..." -ForegroundColor Cyan
$neo4jStatus = docker ps --filter "name=neo4j-rag" --format "{{.Status}}"
if (-not $neo4jStatus) {
    Write-Host "Neo4j 容器未運行，正在啟動..." -ForegroundColor Yellow
    docker run -d --name neo4j-rag -p 7474:7474 -p 7687:7687 -e NEO4J_AUTH=neo4j/test1234 neo4j:5.15
    Write-Host "等待 Neo4j 啟動 (30秒)..." -ForegroundColor Yellow
    Start-Sleep -Seconds 30
}

Write-Host "Neo4j 狀態: $neo4jStatus" -ForegroundColor Green

# 啟動應用程序
Write-Host "`n正在啟動 Spring Boot 應用程序..." -ForegroundColor Cyan
Write-Host "應用程序將在 http://localhost:8080 運行" -ForegroundColor Green
Write-Host "按 Ctrl+C 停止應用程序`n" -ForegroundColor Yellow

# 使用 -DskipTests 跳過測試以避免 Java 版本衝突
mvn clean spring-boot:run -DskipTests
