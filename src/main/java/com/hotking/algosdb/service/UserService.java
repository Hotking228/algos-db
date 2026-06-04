package com.hotking.algosdb.service;

import com.hotking.algosdb.entity.User;
import com.hotking.algosdb.enums.Status;
import com.hotking.algosdb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
        user.setMailpassword(userFromController.getMailpassword());
        user.setStatus(userFromController.getStatus());
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

    public Status isUserExists(String username) {
        AtomicReference<Status> exists = new AtomicReference<>(Status.NOT_EXISTS);
        userRepository.findAll().stream()
                .forEach(u -> {
                    if(username.equals(u.getUsername())){
                        exists.set(u.getStatus());
                    }
                });
        return exists.get();
    }

    public Status isEmailExists(String value) {
        AtomicReference<Status> exists = new AtomicReference<>(Status.NOT_EXISTS);
        userRepository.findAll().stream()
                .forEach(u -> {
                    if(u.getEmail().equals(value)){
                        exists.set(u.getStatus());
                    }
                });

        return exists.get();
    }

    public int save(User user) {
        return userRepository.saveAndFlush(user).getId();
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
