package com.skillbridge.search.controller;

import com.skillbridge.search.dto.EmployeeSearchResponse;
import com.skillbridge.search.service.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/skill")
    public List<EmployeeSearchResponse> searchBySkill(@RequestParam String name) {
        return searchService.searchEmployeesBySkill(name);
    }

    @GetMapping("/availability/{employeeId}")
    public EmployeeSearchResponse getAvailability(@PathVariable Long employeeId) {
        return searchService.getEmployeeAvailability(employeeId);
    }


}
