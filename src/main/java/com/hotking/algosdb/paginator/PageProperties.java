package com.hotking.algosdb.paginator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@ConfigurationProperties(prefix = "page")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageProperties {

    private int size;
    private int num;
}
