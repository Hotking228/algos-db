package com.hotking.algosdb.paginator;

import com.hotking.algosdb.entity.Algorithm;
import com.hotking.algosdb.enums.TagOperator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
@RequiredArgsConstructor
public class AlgosPaginator {
    private List<String> tags = new ArrayList<>();
    private List<String> complexities = new ArrayList<>();
    private String tagOperator = TagOperator.OR.name();
    private Integer pageNums;

    private final PageProperties pageProperties;

    public List<Algorithm> paginate(List<Algorithm> result){
        result = result.stream()
                .skip((long) pageProperties.getNum() * pageProperties.getSize())
                .limit(pageProperties.getSize())
                .collect(Collectors.toList());
        return  result;
    }
}
