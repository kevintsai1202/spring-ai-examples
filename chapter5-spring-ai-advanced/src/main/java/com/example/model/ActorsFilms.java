package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * 演員電影作品
 * 用於表示演員及其電影作品列表
 */
public record ActorsFilms(
                @JsonProperty("actor") String actor,
                @JsonProperty("movies") List<String> movies) {
}
