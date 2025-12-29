package com.generic.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenericPaginationRes<CONTENT_TYPE> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int pageNumber;

    private long pageSize;

    private int totalElements;

    private int totalPages;

    private boolean lastPage;

    private List<CONTENT_TYPE> content;
}
