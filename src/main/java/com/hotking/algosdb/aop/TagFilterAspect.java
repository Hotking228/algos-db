package com.hotking.algosdb.aop;

import com.hotking.algosdb.entity.Algorithm;
import com.hotking.algosdb.entity.Tag;
import com.hotking.algosdb.enums.TagOperator;
import com.hotking.algosdb.paginator.AlgosPaginator;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class TagFilterAspect {

    private final AlgosPaginator algosPaginator;

    @AfterReturning(value = "execution(* com.hotking.algosdb.service.AlgoService.getAlgosByTagsAndComps(..))", returning = "result")
    public void filterByTags(List<Algorithm> result){
        var origin = result;
        if(algosPaginator.getTags().isEmpty()) return;

        result = result.stream()
                .filter(algo -> {
                    boolean res;
                    if(algosPaginator.getTagOperator().equals(TagOperator.AND.name())){
                        res = true;
                        for (int i = 0; i < algosPaginator.getTags().size(); i++) {
                            if(!algo.getTags().stream()
                                    .map(Tag::getName)
                                    .toList()
                                .contains(algosPaginator.getTags().get(i))){
                                res = false;
                            }
                        }
                    } else {
                        res = false;
                        for(int i = 0; i < algosPaginator.getTags().size(); i++){
                            if(algo.getTags().stream()
                                    .map(Tag::getName)
                                    .toList()
                                .contains(algosPaginator.getTags().get(i))){
                                res = true;
                            }
                        }
                    }

                    return res;
                })
                .toList();

        origin.clear();
        origin.addAll(result);
    }
}
