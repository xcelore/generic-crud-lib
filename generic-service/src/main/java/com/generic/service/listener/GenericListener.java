package com.generic.service.listener;

import com.generic.service.entity.GenericEntity;
import com.generic.service.service.GenericTimeCreator;
import com.generic.service.service.IdGenerationStrategy;
import com.generic.service.util.RequestContext;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "dev", "test", "staging", "prod", "qa", "uat"})
@AllArgsConstructor
public class GenericListener {
    private final IdGenerationStrategy idGenerationStrategy;
    private final GenericTimeCreator genericTimeCreator;

    @PrePersist
    protected void beforePersist(GenericEntity genericEntity) {
        genericEntity.setId(idGenerationStrategy.generateId());
        genericEntity.setCreatedAt(genericTimeCreator.createLocalDateTime());
        genericEntity.setUpdatedAt(genericTimeCreator.createLocalDateTime());
        genericEntity.setCreatedBy(RequestContext.getUserFromRequestContextHolder().getUserId());
        genericEntity.setUpdatedBy(RequestContext.getUserFromRequestContextHolder().getUserId());
        genericEntity.setTenantId(RequestContext.getUserFromRequestContextHolder().getTenantId());
    }

    @PreUpdate
    protected void beforeUpdate(GenericEntity genericEntity) {
        genericEntity.setUpdatedAt(genericTimeCreator.createLocalDateTime());
        genericEntity.setUpdatedBy(RequestContext.getUserFromRequestContextHolder().getUserId());
    }

}

