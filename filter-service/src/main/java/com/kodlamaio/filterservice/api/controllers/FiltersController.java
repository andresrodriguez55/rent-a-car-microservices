package com.kodlamaio.filterservice.api.controllers;

import com.kodlamaio.filterservice.business.abstracts.FilterService;
import com.kodlamaio.filterservice.business.dto.responses.GetAllFiltersResponse;
import com.kodlamaio.filterservice.business.dto.responses.GetFilterResponse;
import com.kodlamaio.filterservice.entities.Filter;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/filters")
public class FiltersController
{
    private final FilterService service;

    @PostConstruct
    public void createDb()
    {
        System.err.println("post construct!!!!!");
        //var filter = new Filter();
        //filter.setBrandName("fsgs");
        //filter.setModelName("gdsgds");
        //service.add(filter);
    }

    @GetMapping
    public List<GetAllFiltersResponse> getAll()
    {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public GetFilterResponse getById(@PathVariable UUID id)
    {
        return service.getById(id);
    }

}
