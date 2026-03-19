package com.example.demo.controller;

import com.example.demo.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news")
    public String getStockNews(Model model) {

        List<Map<String, Object>> newsList;

        try {
            // Step 1: Call API
            String raw = newsService.getStockNews();

            // Step 2: Filter & process
            newsList = newsService.filterStockNews(raw);

            // Debug (optional)
            System.out.println("News Count: " + newsList.size());

        } catch (Exception e) {

            // If API fails → avoid crash
            System.out.println("Error fetching news: " + e.getMessage());

            newsList = Collections.emptyList();
        }

        // Send to UI
        model.addAttribute("newsList", newsList);

        return "news"; // Thymeleaf page
    }

    @GetMapping("/search")
    public String searchNews(@RequestParam("query") String query, Model model) {

        List<Map<String, Object>> newsList;

        try {
            newsList = newsService.searchNews(query);
        } catch (Exception e) {
            newsList = Collections.emptyList();
        }

        model.addAttribute("newsList", newsList);
        model.addAttribute("query", query);

        return "news";
    }
}