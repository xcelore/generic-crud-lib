package com.generic.service.controller;

import com.generic.service.dto.GenericPaginationRes;
import com.generic.service.dto.SearchFilter;
import com.generic.service.entity.GenericEntity;
import com.generic.service.service.impl.GenericService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public abstract class GenericController<T_REQ, T_RES, T_ENTITY extends GenericEntity> {

    private final GenericService<T_REQ, T_RES, T_ENTITY> service;

    @GetMapping
    public ResponseEntity<GenericPaginationRes<T_RES>> getPage(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNum,
                                                               @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                                                               @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortFieldName,
                                                               @RequestParam(name = "sortOrder", defaultValue = "ASC") Sort.Direction sortDirection) {
        return ResponseEntity.ok(service.getAllPage(PageRequest.of(pageNum, pageSize, Sort.by(sortDirection, sortFieldName))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<T_RES> getOne(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<T_RES> update(@Valid @RequestBody T_REQ updateReq, @PathVariable("id") UUID id) {
        return ResponseEntity.ok(service.update(updateReq, id));
    }

    @PostMapping
    public ResponseEntity<T_RES> create(@Valid @RequestBody T_REQ request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<T_RES> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping("/search")
    public ResponseEntity<GenericPaginationRes<T_RES>> search(@Valid @RequestBody SearchFilter searchFilter, @RequestParam(name = "pageNumber", defaultValue = "0") int pageNum, @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(service.search(searchFilter, PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.fromString(Optional.ofNullable(searchFilter.getSortOrder()).orElse("ASC")), Optional.ofNullable(searchFilter.getSortBy()).orElse("createdAt")))));
    }
}
