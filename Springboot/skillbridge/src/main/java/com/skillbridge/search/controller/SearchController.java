package com.skillbridge.search.controller;

import com.skillbridge.search.dto.EmployeeSearchResponse;
import com.skillbridge.search.service.SearchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeSearchResponse searchEmployee(@PathVariable Long employeeId) {
        return searchService.getEmployeeDetails(employeeId);
    }
}
