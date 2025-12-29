package com.generic.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchFilter {

    @NotBlank
    private String sortBy;

    @NotBlank
    private String sortOrder;

    @NotNull
    private List<SearchFilterCriteria> searchCriteria;

}
