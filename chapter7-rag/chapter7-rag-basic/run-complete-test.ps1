# Spring AI RAG 完整測試腳本

Write-Host "=== Spring AI RAG 完整測試 ===" -ForegroundColor Cyan
Write-Host ""

# 設定環境變數
Write-Host "[1] 設定環境變數..." -ForegroundColor Yellow
$env:JAVA_HOME = "D:\java\jdk-21"
$env:Path = "D:\java\jdk-21\bin;$env:Path"
$env:OPENAI_API_KEY = "your-openai-api-key"
$env:NEO4J_PASSWORD = "test1234"
Write-Host "✓ 環境變數設定完成" -ForegroundColor Green
Write-Host ""

# 檢查 Neo4j
Write-Host "[2] 檢查 Neo4j 狀態..." -ForegroundColor Yellow
$neo4jStatus = docker ps --filter "name=neo4j-rag" --format "{{.Status}}"
if ($neo4jStatus) {
    Write-Host "✓ Neo4j 運行中: $neo4jStatus" -ForegroundColor Green
} else {
    Write-Host "× Neo4j 未運行" -ForegroundColor Red
    exit 1
}
Write-Host ""

# 編譯專案
Write-Host "[3] 編譯專案..." -ForegroundColor Yellow
mvn clean compile -q
if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ 編譯成功" -ForegroundColor Green
} else {
    Write-Host "× 編譯失敗" -ForegroundColor Red
    exit 1
}
Write-Host ""

# 啟動應用程序
Write-Host "[4] 啟動應用程序..." -ForegroundColor Yellow
Write-Host "  應用程序將在背景啟動..." -ForegroundColor Gray

Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PWD'; `$env:JAVA_HOME='D:\java\jdk-21'; `$env:Path='D:\java\jdk-21\bin;`$env:Path'; `$env:OPENAI_API_KEY='your-openai-api-key'; `$env:NEO4J_PASSWORD='test1234'; mvn spring-boot:run"

Write-Host "  等待應用程序啟動 (20秒)..." -ForegroundColor Gray
Start-Sleep -Seconds 20
Write-Host "✓ 應用程序啟動完成" -ForegroundColor Green
Write-Host ""

# 測試健康檢查
Write-Host "[5] 測試健康檢查..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/api/rag/health" -Method Get
    Write-Host "✓ 健康檢查成功" -ForegroundColor Green
    Write-Host "  狀態: $($health.status)" -ForegroundColor White
    Write-Host "  服務: $($health.service)" -ForegroundColor White
} catch {
    Write-Host "× 健康檢查失敗: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# 測試文檔上傳
Write-Host "[6] 測試文檔上傳..." -ForegroundColor Yellow
if (Test-Path "test-document.txt") {
    try {
        $form = @{
            files = Get-Item -Path "test-document.txt"
        }
        $uploadResult = Invoke-RestMethod -Uri "http://localhost:8080/api/rag/documents" -Method Post -Form $form

        if ($uploadResult.success) {
            Write-Host "✓ 文檔上傳成功" -ForegroundColor Green
            Write-Host "  訊息: $($uploadResult.message)" -ForegroundColor White
        } else {
            Write-Host "× 文檔上傳失敗: $($uploadResult.message)" -ForegroundColor Red
        }
    } catch {
        Write-Host "× 文檔上傳失敗: $($_.Exception.Message)" -ForegroundColor Red
    }
} else {
    Write-Host "× 測試文檔不存在" -ForegroundColor Red
}
Write-Host ""

# 等待向量化完成
Write-Host "  等待文檔向量化完成 (5秒)..." -ForegroundColor Gray
Start-Sleep -Seconds 5

# 測試 RAG 查詢
Write-Host "[7] 測試 RAG 查詢..." -ForegroundColor Yellow
$queryRequest = @{
    question = "什麼是 Spring AI?"
    topK = 5
    similarityThreshold = 0.7
} | ConvertTo-Json

try {
    $queryResult = Invoke-RestMethod -Uri "http://localhost:8080/api/rag/query" -Method Post `
        -ContentType "application/json" -Body $queryRequest

    Write-Host "✓ RAG 查詢成功" -ForegroundColor Green
    Write-Host "  問題: $($queryResult.question)" -ForegroundColor White
    Write-Host "  答案: $($queryResult.answer)" -ForegroundColor Cyan
    Write-Host "  來源數量: $($queryResult.sources.Count)" -ForegroundColor White
    Write-Host "  處理時間: $($queryResult.processingTimeMs) ms" -ForegroundColor White

    if ($queryResult.sources -and $queryResult.sources.Count -gt 0) {
        Write-Host "`n  來源文檔:" -ForegroundColor Yellow
        $queryResult.sources | ForEach-Object {
            Write-Host "    - $($_.title) (相關性: $([math]::Round($_.relevanceScore, 3)))" -ForegroundColor White
        }
    }
} catch {
    Write-Host "× RAG 查詢失敗: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

Write-Host "=== 測試完成 ===" -ForegroundColor Cyan
Write-Host ""
Write-Host "應用程序仍在背景運行,請在另一個視窗查看" -ForegroundColor Yellow
Write-Host "按任意鍵退出測試腳本..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
