# Spring AI 完整學習範例

本專案是《Spring AI 深入指南》書籍的完整程式範例，涵蓋從 Spring Boot 基礎到 Spring AI 進階應用的完整學習路徑。

## 📁 目錄結構

```
spring-ai-examples/
├── chapter0-prerequisite/                      # 第零章：前置準備
├── chapter1-spring-boot-basics/                # 第一章：Spring Boot 基礎
├── chapter2-spring-mvc-api/                    # 第二章：Spring MVC API 開發
├── chapter3-enterprise-features/               # 第三章：企業級功能
├── chapter4-spring-ai-intro/                   # 第四章：Spring AI 入門
├── chapter5-image-generation/                  # 第五章：圖片生成
├── chapter5-spring-ai-advanced/                # 第五章：Spring AI 進階功能
├── chapter5-spring-ai-toolcalling/             # 第五章：Tool Calling 實戰
├── chapter5-voice-generation/                  # 第五章：語音生成
├── chapter6-ai-memory/                         # 第六章：AI 記憶增強
├── chapter7-rag/                               # 第七章：RAG 實作
├── chapter8-advanced-rag/                      # 第八章：進階 RAG
└── chapter9-mcp-integration/                   # 第九章：MCP 整合
    ├── chapter9-mcp-client-basic/              # MCP 客戶端基礎
    ├── chapter9-mcp-server-advanced/           # MCP 伺服器進階
    └── chapter9-mcp-server-tools-resources/    # MCP 工具與資源
```

## 📋 各章節內容

### 第零章：前置準備
開發環境設定與必要工具安裝指南

### 第一章：Spring Boot 基礎
- **DemoApplication.java** - Spring Boot 主程式入口
- **User.java** - 使用者實體模型
- **CreateUserRequest.java** - 建立使用者請求 DTO
- **UserResponse.java** - 使用者回應 DTO
- **UserService.java** - 使用者服務介面
- **UserServiceImpl.java** - 使用者服務實作
- **UserController.java** - 使用者控制器
- **application.yml** - 應用程式配置檔案

### 第二章：Spring MVC API 開發
- **UserRestController.java** - RESTful 使用者控制器
- **ProductRestController.java** - RESTful 產品控制器
- **GlobalExceptionHandler.java** - 全域異常處理器

### 第三章：企業級功能
- **StrongPassword.java** - 自訂密碼強度驗證註解
- **StrongPasswordValidator.java** - 密碼強度驗證器
- **FileUploadController.java** - 檔案上傳控制器

### 第四章：Spring AI 入門
Spring AI 框架基礎概念與初步應用

### 第五章：Spring AI 進階功能
完整的 Spring AI 進階功能實現，包括：

#### chapter5-spring-ai-advanced
- ✅ **提示詞範本** - 動態範本管理與變數替換
- ✅ **Tool Calling** - 函數呼叫與工具整合
- ✅ **多模態處理** - 圖片、文字混合分析
- ✅ **企業數據工具** - 銷售分析、數據查詢
- ✅ **天氣 API 集成** - 實時天氣查詢與預報

#### chapter5-spring-ai-toolcalling
專注於 Tool Calling 功能的深入實作

#### chapter5-image-generation
AI 圖片生成功能實現

#### chapter5-voice-generation
語音生成與處理功能

### 第六章：AI 記憶增強
實現 AI 對話記憶與上下文管理

### 第七章：RAG 實作
檢索增強生成（RAG）基礎實現

### 第八章：進階 RAG
進階 RAG 技術與優化策略

### 第九章：MCP 整合
Model Context Protocol (MCP) 完整實現：

#### chapter9-mcp-client-basic
- MCP 客戶端基礎實現
- 與 MCP 伺服器的通訊

#### chapter9-mcp-server-advanced
- MCP 伺服器進階功能
- 自訂協議處理

#### chapter9-mcp-server-tools-resources
- MCP 工具提供者實現
- 資源管理與共享


## 🚀 技術棧

- **Java**: 21 (部分章節支援 Java 8)
- **Spring Boot**: 3.3.0+
- **Spring AI**: 1.0.0-M4+
- **Maven**: 3.9+
- **AI APIs**: 
  - OpenAI GPT-4 / GPT-4o-mini
  - Groq (可選)
  - Azure OpenAI (可選)

## 🚀 快速開始

### 1. 環境準備

#### Java 環境設定

```bash
# Windows PowerShell - 設置 Java 21
$env:JAVA_HOME = "D:\java\jdk-21"
$env:Path = "D:\java\jdk-21\bin;$env:Path"

# 驗證 Java 版本
java -version
```

#### Maven 安裝

確保安裝 Maven 3.9 或更高版本。

### 2. 設置 API Keys

```bash
# Windows PowerShell
$env:OPENAI_API_KEY = "your-openai-api-key-here"
$env:GROQ_API_KEY = "your-groq-api-key-here"  # 可選
```

### 3. 建立 Spring Boot 專案

使用 Spring Initializr 或 STS4 建立新的 Spring Boot 專案，選擇以下基礎依賴：

- Spring Web
- Spring Boot DevTools
- Lombok
- Validation

### 4. 複製對應章節程式碼

將對應章節的 Java 檔案複製到您的專案中，注意包結構：

```text
src/main/java/com/example/
├── controller/     # 控制器類別
├── service/        # 服務類別
├── model/          # 實體模型
├── dto/            # 資料傳輸物件
├── config/         # 配置類別
├── tools/          # AI 工具
└── exception/      # 異常處理
```

### 5. 配置檔案

將 `application.yml` 複製到 `src/main/resources/` 目錄下。

### 6. 執行應用程式

```bash
# 進入專案目錄
cd chapter5-spring-ai-advanced

# 編譯專案
mvn clean compile

# 執行應用
mvn spring-boot:run
```

## 📖 API 測試範例

### 基礎 API 測試

```bash
# 獲取所有使用者
curl -X GET http://localhost:8080/api/users

# 建立新使用者
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "張小明",
    "email": "ming@example.com",
    "password": "password123"
  }'
```

### Spring AI 功能測試

```bash
# Tool Calling - 當前時間
curl "http://localhost:8080/api/tool-calling/current-time"

# 企業數據分析
curl "http://localhost:8080/api/v1/enterprise/sales-ranking/2024-10"

# 天氣查詢
curl "http://localhost:8080/api/v1/weather/current/台北"
```

## 🔧 依賴說明

### Maven 依賴 (pom.xml)

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring AI OpenAI -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
    </dependency>
    
    <!-- Spring Boot Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- Spring Boot DevTools -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
</dependencies>
```

## 📝 重要注意事項

1. **Java 版本**
   - chapter5-9 需要 JDK 17 或更高版本（建議使用 JDK 21）
   - chapter1-3 支援 JDK 8+

2. **API Keys**
   - OpenAI API Key 為必須（大部分章節）
   - 建議使用環境變數設定，不要硬編碼在程式碼中

3. **包結構**
   - 請確保 Java 檔案放在正確的包結構中
   - 注意 package 聲明與目錄結構一致

4. **Lombok 設定**
   - 確保 IDE 已安裝 Lombok 插件
   - IntelliJ IDEA: Settings → Plugins → 搜尋 "Lombok"
   - Eclipse/STS: 下載 lombok.jar 並執行安裝

5. **配置檔案**
   - 建議建立 `application-dev.yml` 用於開發環境
   - 將敏感資訊加入 `.gitignore`

## 🎯 學習建議

1. **循序漸進**
   - 按章節順序學習，從 Spring Boot 基礎開始
   - 每個章節都有完整的可執行範例

2. **實際操作**
   - 不只閱讀程式碼，要親自執行和測試
   - 使用 curl、Postman 或瀏覽器測試 API

3. **理解原理**
   - 理解每個類別的職責和設計模式
   - 學習 Spring AI 的核心概念

4. **擴展練習**
   - 在範例基礎上添加新功能
   - 嘗試整合其他 AI 模型

5. **參考文件**
   - 每個章節都有詳細的 README
   - 建議同時參考官方文件

## 📚 相關文件與資源

- [Spring Boot 官方文件](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring AI 官方文件](https://docs.spring.io/spring-ai/reference/)
- [Spring Framework 參考文件](https://docs.spring.io/spring-framework/docs/current/reference/html/)
- [OpenAI API 文件](https://platform.openai.com/docs)
- [Bean Validation 規範](https://beanvalidation.org/)
- [Model Context Protocol (MCP)](https://modelcontextprotocol.io/)

## 🤝 貢獻

歡迎提交 Issue 和 Pull Request！

## 📄 授權

MIT License

---

**專案更新日期**: 2026-01-29

**版本**: 2.0.0

**書籍**: 《Spring AI 深入指南》