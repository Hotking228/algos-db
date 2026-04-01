package com.hotking.algosdb.service;

import com.hotking.algosdb.entity.Tag;
import com.hotking.algosdb.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Integer create(Tag tag){
        return tagRepository.saveAndFlush(tag).getId();
    }

    public List<Tag> getAll(){
        return tagRepository.findAll();
    }

    public List<Tag> getTagsByAlgo(Integer algoId){
        return tagRepository.findByAlgoId(algoId);
    }

    public Optional<Tag> getById(Integer id){
        //TODO: добавить исключение
        return Optional.of(tagRepository.findById(id))
                .orElseThrow();
    }

    public Integer update(Integer id, Tag tagFromController){
        //TODO: добавить исключение
        Tag tag = tagRepository.findById(id)
                .orElseThrow();
        tag.setAlgorithms(tagFromController.getAlgorithms());
        tag.setName(tagFromController.getName());
        tagRepository.saveAndFlush(tag);
        return tag.getId();
    }

    //Если возвращает -1 => что-то не так(сущность не удалилась)
    public Integer delete(Integer id){
        return tagRepository.findById(id)
                .map(algo -> {
                    tagRepository.delete(algo);
                    tagRepository.flush();
                    return id;
                })
                .orElse(-1);
    }
}
