# Code Review Report: Chapters 1-3

**Reviewer:** Jules (Senior Java Engineer)
**Date:** 2024-05-23
**Target:** Chapter 1, 2, 3

## 總結 (Summary)

整體程式碼結構清晰，遵循 Spring Boot 與 REST API 的最佳實踐。分層架構 (Controller, Service, Repository, DTO) 實作完善，且代碼品質高。
然而，存在幾個**嚴重影響建置 (Build)** 與**專案整潔度**的問題，建議優先修正。

---

## 全域問題 (Global Issues)

### 1. `pom.xml` 中的硬編碼路徑 (Critical)
在所有章節的 `pom.xml` 中，`maven-compiler-plugin` 與 `maven-enforcer-plugin` 皆包含了硬編碼的本機路徑：
```xml
<executable>D:\java\jdk-21\bin\javac</executable>
```
這會導致在其他開發者的機器或 CI/CD 環境中**建置失敗**。
**建議**：移除此配置，改為依賴 `JAVA_HOME` 環境變數。

### 2. Spring Boot 版本疑慮
專案使用 `3.5.7` 版本：
```xml
<version>3.5.7</version>
```
根據 Context7 確認，目前 Spring Boot 主流穩定版本為 `3.2.x` 或 `3.3.x` (3.4.x 為開發中)。`3.5.x` 似乎過於超前或不存在於標準 Maven Central Repository。
**建議**：確認版本號是否正確，建議降級至最新的穩定版本 (如 `3.3.0`) 以確保相容性。

### 3. SpringDoc OpenApi 版本過舊
Chapter 3 使用了 `springdoc-openapi-starter-webmvc-ui` 版本 `2.2.0`。
**建議**：升級至 `2.8.15` 或更新版本，以獲得更好的 Spring Boot 3 相容性與 Bug 修復。

---

## 章節詳細審查 (Detailed Review)

### Chapter 1: Spring Boot Basics
*   **優點**：
    *   專案結構標準。
    *   Controller 正確使用 `@RestController` 與 DTO 模式。
    *   `UserService` 使用記憶體模擬資料庫，符合 "Basics" 章節的教學目的。
    *   HTTP Status Code 使用正確 (201 Created, 204 No Content)。
*   **建議**：無重大問題。

### Chapter 2: Spring MVC API
*   **優點**：
    *   **REST 設計優秀**：`createProduct` 回傳 `Location` header，這是很多開發者會忽略的最佳實踐。
    *   **HTTP 動詞精準**：正確區分 `PUT` (全量更新) 與 `PATCH` (部分更新)。
    *   **異常處理**：`GlobalExceptionHandler` 結構良好，回傳格式統一。
    *   **交易管理**：`ProductServiceImpl` 正確使用 `@Transactional(readOnly = true)`。
*   **建議**：
    *   `PATCH` 的實作使用了 `Map<String, Object>` 與 `switch` case，雖然可行但型別安全性較低。在更複雜的專案中可考慮使用 `JsonPatch` 或類似策略。

### Chapter 3: Enterprise Features
*   **嚴重問題**：
    *   專案根目錄 (`chapter3-enterprise-features/`) 存在多個**游離的 Java 檔案** (`FileUploadController.java`, `StrongPassword.java` 等)，這些檔案與 `src/main/java` 中的內容重複且版本較舊。
    *   **建議**：立即刪除根目錄下的 `.java` 檔案，只保留 `src/` 內的程式碼。
*   **優點**：
    *   **檔案上傳**：`FileStorageService` 實作了安全性檢查 (清潔路徑、檢查 Content-Type)，並使用 UUID 防止檔名衝突，非常專業。
    *   **自定義驗證**：`StrongPasswordValidator` 邏輯清晰且使用正則表達式正確。
    *   **OpenAPI 配置**：`OpenApiConfig` 設定完整。

---

## 下一步行動建議 (Action Items)

1.  **修正 POM**：移除所有 `D:\java\jdk-21` 路徑設定。
2.  **清理檔案**：刪除 Chapter 3 根目錄下的 `.java` 檔案。
3.  **版本確認**：將 Spring Boot 版本調整為主流穩定版 (建議 3.3.x)。
