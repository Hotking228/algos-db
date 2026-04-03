package com.hotking.algosdb.repository;

import com.hotking.algosdb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
