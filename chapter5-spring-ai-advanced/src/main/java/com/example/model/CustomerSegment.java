package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 客戶細分
 * 用於表示客戶群體的特徵資訊
 */
public record CustomerSegment(
                @JsonProperty("name") String name,
                @JsonProperty("size") int size,
                @JsonProperty("characteristics") List<String> characteristics,
                @JsonProperty("value") String value) {
}
