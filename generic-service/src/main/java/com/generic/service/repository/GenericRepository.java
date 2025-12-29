package com.generic.service.repository;

import com.generic.service.entity.GenericEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface GenericRepository<T_ENTITY extends GenericEntity> extends JpaRepository<T_ENTITY, UUID>, JpaSpecificationExecutor<T_ENTITY> {

    Optional<T_ENTITY> findByIdAndDeletedFalse(UUID id);

    Page<T_ENTITY> findByDeletedFalse(Pageable pageable);

    Page<T_ENTITY> findAllByTenantIdAndDeletedFalse(UUID tenantId, Pageable pageable);
}
