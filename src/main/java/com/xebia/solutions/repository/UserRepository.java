package com.xebia.solutions.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.xebia.solutions.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
