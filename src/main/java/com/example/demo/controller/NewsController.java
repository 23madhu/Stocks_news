package com.example.demo.controller;

import com.example.demo.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        String raw = newsService.getStockNews();
        List<Map<String, Object>> newsList = newsService.filterStockNews(raw);

        model.addAttribute("newsList", newsList);
        return "news"; // Thymeleaf template
    }
}
