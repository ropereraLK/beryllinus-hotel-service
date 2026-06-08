package com.beryllinus.hotel_service.dto.response;

import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Getter
public class HealthDTO {

    private final String message = "Service Up and running";
    private final Instant timestamp = Instant.now();

}
