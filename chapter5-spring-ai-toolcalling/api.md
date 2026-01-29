# API 文件（api.md）

> 專案：spring-ai-toolcalling（Tool Calling 範例）
> 日期：2026-01-29
> 風格：RESTful
> 狀態：已確認

---

## 共通說明

- Base URL：`http://localhost:8080`
- 內容類型：`application/json`
- 成功回應：HTTP 200
- 伺服器錯誤：HTTP 500

---

## 1) 基礎 Tool Calling

### 1.1 取得 AI 回應（單一工具）
- Method：`GET`
- Path：`/api/simple-tool/chat`
- Query：`message`（必填）

**Response**
```json
"現在是 2026年01月29日 10:30:00（台灣時間）"
```

### 1.2 取得 AI 回應（多工具）
- Method：`GET`
- Path：`/api/multi-tool/chat`
- Query：`message`（必填）

**Response**
```json
"3 小時後是下午 5:30"
```

---

## 2) 智能助手

### 2.1 智能助手對話
- Method：`POST`
- Path：`/api/assistant/chat`
- Body：
```json
{
  "message": "現在幾點？"
}
```

**Response**
```json
{
  "success": true,
  "question": "現在幾點？",
  "answer": "現在是 2026年01月29日 10:30:00（台灣時間）",
  "executionTime": 120,
  "timestamp": "2026-01-29T10:30:00"
}
```

---

## 3) 企業資料分析

### 3.1 企業資料查詢
- Method：`POST`
- Path：`/api/enterprise/chat`
- Body：
```json
{
  "message": "請分析 2024 年銷售表現"
}
```

**Response**
```json
{
  "success": true,
  "question": "請分析 2024 年銷售表現",
  "answer": "2024 年總銷量...",
  "executionTime": 250,
  "timestamp": "2026-01-29T10:30:00"
}
```

---

## 4) 工具鏈

### 4.1 複雜工具鏈查詢
- Method：`GET`
- Path：`/api/tool-chain/complex-query`
- Query：`prompt`（必填）

**Response**
```json
"📊 2023 年最熱銷產品分析報告..."
```

### 4.2 產品深度分析
- Method：`POST`
- Path：`/api/tool-chain/product-analysis`
- Body：
```json
{
  "productCode": "PD-1385",
  "analysisType": "市場表現",
  "year": 2024
}
```

**Response**
```json
{
  "success": true,
  "productCode": "PD-1385",
  "analysisType": "市場表現",
  "analysis": "...",
  "executionTime": 320,
  "timestamp": "2026-01-29T10:30:00"
}
```

---

## 5) 天氣查詢

### 5.1 AI 天氣查詢
- Method：`GET`
- Path：`/api/weather/chat`
- Query：`question`（必填）

**Response**
```json
{
  "question": "桃園目前天氣如何？",
  "answer": "📍 地點：桃園...",
  "success": true,
  "error": null,
  "timestamp": 1706518200000
}
```

### 5.2 直接取得天氣資訊
- Method：`GET`
- Path：`/api/weather/current`
- Query：`location`（必填）

**Response**
```json
{
  "location": "桃園",
  "temperature": 28.5,
  "humidity": 0.72,
  "weather": "多雲",
  "rainfall": 0.0,
  "wind_direction": "東南風",
  "wind_speed": 2.3,
  "success": true,
  "error": null,
  "observation_time": "2026-01-29 10:30:00"
}
```

### 5.3 溫度排行榜
- Method：`GET`
- Path：`/api/weather/temperature-ranking`
- Query：`topCount`（選填，預設 10）

**Response**
```json
{
  "rankings": [
    { "location": "台北", "temperature": 30.5 },
    { "location": "台中", "temperature": 29.8 }
  ],
  "success": true,
  "error": null,
  "updateTime": "2026-01-29 10:30:00"
}
```
