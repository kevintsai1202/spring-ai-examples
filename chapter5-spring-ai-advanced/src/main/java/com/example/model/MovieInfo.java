package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 電影資訊
 * 用於表示電影的詳細資訊
 */
public record MovieInfo(
                @JsonProperty("title") String title,
                @JsonProperty("director") String director,
                @JsonProperty("year") int year,
                @JsonProperty("genre") String genre,
                @JsonProperty("rating") double rating,
                @JsonProperty("plot") String plot) {
}
