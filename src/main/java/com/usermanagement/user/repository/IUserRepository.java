package com.usermanagement.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.usermanagement.user.entity.User;

@Repository
public interface IUserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}