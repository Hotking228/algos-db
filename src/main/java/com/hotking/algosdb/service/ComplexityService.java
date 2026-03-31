package com.hotking.algosdb.service;

import com.hotking.algosdb.entity.Algorithm;
import com.hotking.algosdb.entity.Complexity;
import com.hotking.algosdb.repository.ComplexityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComplexityService {
    ComplexityRepository compRepository;

    public Integer create(Complexity complexity){
        return compRepository.saveAndFlush(complexity).getId();
    }

    public Optional<Complexity> getById(Integer id){
        //TODO: добавить исключение
        return Optional.of(compRepository.findById(id))
                .orElseThrow();
    }

    public Integer update(Integer id, Complexity complexity){
        //TODO: добавить исключение
        Complexity comp = compRepository.findById(id)
                .orElseThrow();
        comp.setComp(complexity.getComp());
        comp.setAlgos(complexity.getAlgos());
        compRepository.saveAndFlush(comp);
        return comp.getId();
    }

    //Если возвращает -1 => что-то не так(сущность не удалилась)
    public Integer delete(Integer id){
        return compRepository.findById(id)
                .map(algo -> {
                    compRepository.delete(algo);
                    compRepository.flush();
                    return id;
                })
                .orElse(-1);
    }
}
