package com.mykhailotiutiun.repcounterbot.service;

import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final WorkoutWeekService workoutWeekService;
    private final LocalDateWeekService localDateWeekService;


    public UserService(UserRepository userRepository, WorkoutWeekService workoutWeekService, LocalDateWeekService localDateWeekService) {
        this.userRepository = userRepository;
        this.workoutWeekService = workoutWeekService;
        this.localDateWeekService = localDateWeekService;
    }


    public User getUserById(Long id){
        log.trace("Get User with id: {}", id);
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<User> getAllUsers(){
        log.trace("Get all Users");
        return userRepository.findAll();
    }

    public void create(User user) throws EntityAlreadyExistsException{
        if(userRepository.existsById(user.getId())){
            throw new EntityAlreadyExistsException(String.format("User with this id(%d) already exists", user.getId()));
        }

        log.trace("Create User: {}", user);
        save(user);

        workoutWeekService.create(new WorkoutWeek(user, localDateWeekService.getFirstDateOfWeekFromDate(LocalDate.now()), localDateWeekService.getLastDateOfWeekFromDate(LocalDate.now())));
    }

    public void save(User user){
        log.trace("Save User: {}", user);
        userRepository.save(user);
    }

    public void deleteById(Long id){
        log.trace("Delete User with id: {}", id);
        userRepository.deleteById(id);
    }

}
