package com.generic.service.service.impl;

import com.generic.service.dto.GenericPaginationRes;
import com.generic.service.dto.SearchFilter;
import com.generic.service.dto.SearchFilterCriteria;
import com.generic.service.entity.GenericEntity;
import com.generic.service.exception.GenericException;
import com.generic.service.mapper.GenericMapper;
import com.generic.service.repository.GenericRepository;
import com.generic.service.util.RequestContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public abstract class GenericService<T_REQ, T_RES, T_ENTITY extends GenericEntity> {

    private final GenericRepository<T_ENTITY> repository;

    private final Class<T_RES> tResClass;

    private final Class<T_ENTITY> tEntityClass;

    public GenericPaginationRes<T_RES> getAllPage(Pageable pageable) {
        final Page<T_ENTITY> tEntityPage = repository.findByDeletedFalse(pageable);
        return GenericPaginationRes.<T_RES>builder()
                .totalPages(tEntityPage.getTotalPages())
                .totalElements(tEntityPage.getNumberOfElements())
                .pageSize(tEntityPage.getSize())
                .pageNumber(tEntityPage.getNumber())
                .lastPage(tEntityPage.isLast())
                .content(tEntityPage.getContent().stream().map(tEntity -> GenericMapper.map(tEntity, tResClass)).toList())
                .build();
    }

    public GenericPaginationRes<T_RES> search(SearchFilter searchFilter, Pageable pageable) {
        final Page<T_ENTITY> tEntityPage = repository.findAll(buildSpecification(searchFilter.getSearchCriteria()), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), searchFilter.getSortBy() != null && searchFilter.getSortOrder() != null ? Sort.by(Sort.Direction.fromString(searchFilter.getSortOrder()), searchFilter.getSortBy()) : Sort.unsorted()));
        return GenericPaginationRes.<T_RES>builder()
                .totalPages(tEntityPage.getTotalPages())
                .totalElements(tEntityPage.getNumberOfElements())
                .pageSize(tEntityPage.getSize())
                .pageNumber(tEntityPage.getNumber())
                .lastPage(tEntityPage.isLast())
                .content(tEntityPage.getContent().stream().map(tEntity -> GenericMapper.map(tEntity, tResClass)).toList())
                .build();
    }

    public T_RES getById(UUID id) {
        return GenericMapper.map(repository.findByIdAndDeletedFalse(id).orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND.value(), "Record not found with id " + id)), tResClass);
    }

    /**
     * Gets current logged-in user tenant if @param useThisTenantIdIfNotNull is null else current logged-in tenant and then fetch records accordingly
     *
     * @param useThisTenantIdIfNotNull
     * @param pageable
     * @return
     */
    public GenericPaginationRes<T_RES> getAllByTenantIdAndWithPageable(UUID useThisTenantIdIfNotNull, Pageable pageable) {
        if (Objects.isNull(useThisTenantIdIfNotNull) && Objects.isNull(RequestContext.getUserFromRequestContextHolder().getTenantId())) {
            throw new GenericException(HttpStatus.BAD_REQUEST.value(), "Tenant id must not be null");
        }
        final Page<T_ENTITY> tEntityPage = repository.findAllByTenantIdAndDeletedFalse(Objects.nonNull(useThisTenantIdIfNotNull) ? useThisTenantIdIfNotNull : RequestContext.getUserFromRequestContextHolder().getTenantId(), pageable);
        return GenericPaginationRes.<T_RES>builder()
                .totalPages(tEntityPage.getTotalPages())
                .totalElements(tEntityPage.getNumberOfElements())
                .pageSize(tEntityPage.getSize())
                .pageNumber(tEntityPage.getNumber())
                .lastPage(tEntityPage.isLast())
                .content(tEntityPage.getContent().stream().map(tEntity -> GenericMapper.map(tEntity, tResClass)).toList())
                .build();
    }

    private T_ENTITY getInternal(UUID id) {
        return repository.findByIdAndDeletedFalse(id).orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND.value(), "Record not found with id " + id));
    }

    @Transactional
    public T_RES update(T_REQ updateReq, UUID id) {
        getInternal(id);
        final T_ENTITY tEntity = GenericMapper.map(updateReq, tEntityClass);
        tEntity.setId(id);
        return GenericMapper.map(repository.saveAndFlush(tEntity), tResClass);
    }

    @Transactional
    public T_RES create(T_REQ createReq) {
        return GenericMapper.map(repository.saveAndFlush(GenericMapper.map(createReq, tEntityClass)), tResClass);
    }

    @Transactional
    public T_RES delete(UUID id) {
        final T_ENTITY dbEntity = getInternal(id);
        dbEntity.setDeleted(true);
        return GenericMapper.map(repository.saveAndFlush(dbEntity), tResClass);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    private Specification<T_ENTITY> buildSpecification(List<SearchFilterCriteria> criteriaList) {
        return (Root<T_ENTITY> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            for (SearchFilterCriteria criteria : criteriaList) {
                String key = criteria.getFilterKey();
                List<Object> values = criteria.getValue();
                String op = criteria.getOperation().toLowerCase();
                Path<Object> path = root.get(key);
                switch (op) {
                    case "=", "eq":
                        predicates.add(cb.equal(path, values.get(0)));
                        break;

                    case "!=", "ne":
                        predicates.add(cb.notEqual(path, values.get(0)));
                        break;
                    case ">", "gt":
                        predicates.add(cb.greaterThan(path.as(Comparable.class), (Comparable) values.get(0)));
                        break;

                    case "<", "lt":
                        predicates.add(cb.lessThan(path.as(Comparable.class), (Comparable) values.get(0)));
                        break;

                    case ">=", "gte":
                        predicates.add(cb.greaterThanOrEqualTo(path.as(Comparable.class), (Comparable) values.get(0)));
                        break;

                    case "<=", "lte":
                        predicates.add(cb.lessThanOrEqualTo(path.as(Comparable.class), (Comparable) values.get(0)));
                        break;

                    case "like":
                        predicates.add(cb.like(cb.lower(path.as(String.class)), "%" + values.get(0).toString().toLowerCase() + "%"));
                        break;

                    case "in":
                        CriteriaBuilder.In<Object> inClause = cb.in(path);
                        for (Object val : values) {
                            inClause.value(val);
                        }
                        predicates.add(inClause);
                        break;

                    case "between":
                        if (values.size() >= 2 && values.get(0) instanceof Comparable && values.get(1) instanceof Comparable) {
                            predicates.add(cb.between(path.as(Comparable.class), (Comparable) values.get(0), (Comparable) values.get(1)));
                        }
                        break;

                    default:
                        throw new UnsupportedOperationException("Unsupported operation: " + op);
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
