package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User getUserById(Long id){
        log.trace("Get user with id: {}", id);
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<User> getAllUsers(){
        log.trace("Get all users");
        return userRepository.findAll();
    }


    public void save(User user){
        log.trace("Save user: {}", user);
        userRepository.save(user);
    }

    public void deleteById(Long id){
        log.trace("Delete user with id: {}", id);
        userRepository.deleteById(id);
    }
}
