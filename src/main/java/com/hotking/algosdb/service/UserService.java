package com.hotking.algosdb.service;

import com.hotking.algosdb.entity.Tag;
import com.hotking.algosdb.entity.User;
import com.hotking.algosdb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public Integer create(User user){
        return userRepository.saveAndFlush(user).getId();
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public Optional<User> getById(Integer id){
        //TODO: добавить исключение
        return Optional.of(userRepository.findById(id))
                .orElseThrow();
    }

    public Integer update(Integer id, User userFromController){
        //TODO: добавить исключение
        User user = userRepository.findById(id)
                .orElseThrow();
        user.setEmail(userFromController.getEmail());
        user.setRole(userFromController.getRole());
        user.setPassword(userFromController.getPassword());
        user.setUsername(userFromController.getUsername());
        userRepository.saveAndFlush(user);
        return user.getId();
    }

    //Если возвращает -1 => что-то не так(сущность не удалилась)
    public Integer delete(Integer id){
        return userRepository.findById(id)
                .map(algo -> {
                    userRepository.delete(algo);
                    userRepository.flush();
                    return id;
                })
                .orElse(-1);
    }
}
