package com.hotking.algosdb.paginator;

import com.hotking.algosdb.enums.TagOperator;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class AlgosPaginator {
    private List<String> tags = new ArrayList<>();
    private List<String> complexities = new ArrayList<>();
    private String tagOperator = TagOperator.OR.name();

}
