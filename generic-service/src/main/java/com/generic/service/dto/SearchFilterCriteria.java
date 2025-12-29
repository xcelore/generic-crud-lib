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
public class SearchFilterCriteria {
    @NotNull
    @NotBlank
    private String filterKey;

    private List<Object> value;

    @NotNull
    private String operation;
}
