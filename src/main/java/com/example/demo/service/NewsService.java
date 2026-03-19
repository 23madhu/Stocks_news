package com.example.demo.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class NewsService {

    private final String API_KEY = "OeJ4xsu8N5eiMBLVNNQB4WdPLKgaVY565moNfJ0h";
    private final String BASE_URL = "https://api.marketaux.com/v1/news/all";

    // Fetch raw news JSON
    public String getStockNews() {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("countries", "IN")
                .queryParam("categories", "business")
                .queryParam("filter_entities", "true")
                .queryParam("limit", "10")
                .queryParam("api_token", API_KEY)
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }

    // Filter + Transform news for UI
    public List<Map<String, Object>> filterStockNews(String rawJson) {

        List<Map<String, Object>> stockNews = new ArrayList<>();

        JSONObject obj = new JSONObject(rawJson);
        JSONArray data = obj.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {

            JSONObject newsItem = data.getJSONObject(i);
            JSONArray entities = newsItem.optJSONArray("entities");

            if (entities != null && entities.length() > 0) {

                Map<String, Object> map = new HashMap<>();

                // Basic fields
                map.put("title", newsItem.optString("title"));
                map.put("highlight", newsItem.optString("description"));
                map.put("url", newsItem.optString("url"));
                map.put("image", newsItem.optString("image_url"));
                map.put("date", newsItem.optString("published_at"));

                // 🔥 Stock impact lists
                List<String> upStocks = new ArrayList<>();
                List<String> downStocks = new ArrayList<>();

                for (int j = 0; j < entities.length(); j++) {

                    JSONObject entity = entities.getJSONObject(j);

                    String symbol = entity.optString("symbol");
                    double sentiment = entity.optDouble("sentiment_score", 0);

                    if (symbol != null && !symbol.isEmpty()) {

                        if (sentiment > 0) {
                            upStocks.add(symbol);
                        } else if (sentiment < 0) {
                            downStocks.add(symbol);
                        }
                    }
                }

                map.put("upStocks", upStocks);
                map.put("downStocks", downStocks);

                stockNews.add(map);
            }
        }

        return stockNews;
    }

    public List<Map<String, Object>> searchNews(String query) {

        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("search", query) // 🔥 dynamic search
                .queryParam("language", "en")
                .queryParam("limit", "10")
                .queryParam("api_token", API_KEY)
                .toUriString();

        String rawJson = restTemplate.getForObject(url, String.class);

        return filterStockNews(rawJson); // reuse your existing logic
    }
}