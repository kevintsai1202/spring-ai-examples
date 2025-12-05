# 設定環境變數
$env:JAVA_HOME = "D:\java\jdk-21"
$env:Path = "D:\java\jdk-21\bin;$env:Path"
$env:OPENAI_API_KEY = "your-openai-api-key"

# 啟動應用
Write-Host "啟動 chapter6-memory-core 應用程序..." -ForegroundColor Green
cd "E:\Spring_AI_BOOK\code-examples\chapter6-ai-memory\chapter6-memory-core"

# 在後台啟動應用
$process = Start-Process -FilePath "mvn" -ArgumentList "spring-boot:run" -NoNewWindow -PassThru -RedirectStandardOutput "app.log" -RedirectStandardError "app.log"

Write-Host "應用正在啟動，進程ID: $($process.Id)" -ForegroundColor Yellow
Write-Host "等待應用啟動（30秒）..." -ForegroundColor Yellow

# 等待應用啟動
Start-Sleep -Seconds 15

# 測試健康檢查端點
Write-Host "`n測試健康檢查端點..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/chat/health" -Method Get
    Write-Host "✅ 健康檢查成功:" -ForegroundColor Green
    Write-Host $response.message -ForegroundColor Green
} catch {
    Write-Host "❌ 健康檢查失敗:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}

# 測試新建對話
Write-Host "`n測試新建對話..." -ForegroundColor Cyan
try {
    $body = @{
        userId = "user123"
        message = "你好，請告訴我今天的日期和時間"
        enableTools = $true
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/chat/new" `
        -Method Post `
        -Body $body `
        -ContentType "application/json"

    Write-Host "✅ 新建對話成功:" -ForegroundColor Green
    Write-Host "對話ID: $($response.conversationId)" -ForegroundColor Green
    Write-Host "回應: $($response.message)" -ForegroundColor Green

    # 保存對話ID供後續測試
    $conversationId = $response.conversationId
} catch {
    Write-Host "❌ 新建對話失敗:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}

# 等待一秒
Start-Sleep -Seconds 1

# 測試發送消息
if ($conversationId) {
    Write-Host "`n測試發送消息到既存對話..." -ForegroundColor Cyan
    try {
        $body = @{
            userId = "user123"
            message = "外面天氣怎麼樣？"
            enableTools = $true
        } | ConvertTo-Json

        $response = Invoke-RestMethod -Uri "http://localhost:8080/api/chat/conversation/$conversationId" `
            -Method Post `
            -Body $body `
            -ContentType "application/json"

        Write-Host "✅ 發送消息成功:" -ForegroundColor Green
        Write-Host "回應: $($response.message)" -ForegroundColor Green
    } catch {
        Write-Host "❌ 發送消息失敗:" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red
    }

    # 測試獲取對話歷史
    Write-Host "`n測試獲取對話歷史..." -ForegroundColor Cyan
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:8080/api/chat/conversation/$conversationId/history?userId=user123" `
            -Method Get

        Write-Host "✅ 獲取對話歷史成功:" -ForegroundColor Green
        Write-Host "消息數量: $($response.messages.Count)" -ForegroundColor Green
    } catch {
        Write-Host "❌ 獲取對話歷史失敗:" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red
    }
}

# 終止應用
Write-Host "`n終止應用程序..." -ForegroundColor Yellow
Stop-Process -Id $process.Id -Force
Write-Host "✅ 應用已終止" -ForegroundColor Green

# 顯示應用日誌
Write-Host "`n應用日誌（最後20行）:" -ForegroundColor Cyan
Get-Content "app.log" | Select-Object -Last 20
