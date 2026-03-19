package com.example.demo.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsService {

    // Replace with your actual API key
    private final String API_KEY = "OeJ4xsu8N5eiMBLVNNQB4WdPLKgaVY565moNfJ0h";
    private final String BASE_URL = "https://api.marketaux.com/v1/news/all";

    // Fetch raw news JSON
    public String getStockNews() {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("countries", "IN") // Indian news
                .queryParam("categories", "business")// business news
                .queryParam("filter_entities", "true")
                .queryParam("limit", "10")
                .queryParam("api_token", API_KEY)
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }

    // Filter news that have stock entities and convert to Map
    public List<Map<String, Object>> filterStockNews(String rawJson) {
        List<Map<String, Object>> stockNews = new ArrayList<>();

        JSONObject obj = new JSONObject(rawJson);
        JSONArray data = obj.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
            JSONObject newsItem = data.getJSONObject(i);
            JSONArray entities = newsItem.getJSONArray("entities");

            if (entities.length() > 0) {
                // Convert JSONObject to Map for Thymeleaf
                Map<String, Object> map = new HashMap<>();
                map.put("title", newsItem.optString("title"));
                map.put("description", newsItem.optString("description"));
                map.put("url", newsItem.optString("url"));
                map.put("image_url", newsItem.optString("image_url"));
                map.put("published_at", newsItem.optString("published_at"));
                stockNews.add(map);
            }
        }

        return stockNews;
    }
}
