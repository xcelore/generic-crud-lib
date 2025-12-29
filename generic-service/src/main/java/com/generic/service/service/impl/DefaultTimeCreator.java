package com.generic.service.service.impl;

import com.generic.service.service.GenericTimeCreator;
import com.generic.service.util.GenericTimeUtil;

import java.time.LocalDateTime;

public class DefaultTimeCreator implements GenericTimeCreator {
    @Override
    public LocalDateTime createLocalDateTime() {
        return GenericTimeUtil.getLocalDateTimeInIST();
    }
}
