package com.mykhailotiutiun.repcounterbot.repository;

import com.mykhailotiutiun.repcounterbot.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long> {

}
