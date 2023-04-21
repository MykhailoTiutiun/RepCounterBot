package com.mykhailotiutiun.repcounterbot.service.Impl;

import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.UserRepository;
import com.mykhailotiutiun.repcounterbot.service.LocalDateWeekService;
import com.mykhailotiutiun.repcounterbot.service.UserService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WorkoutWeekService workoutWeekService;
    private final LocalDateWeekService localDateWeekService;


    public UserServiceImpl(UserRepository userRepository, WorkoutWeekService workoutWeekService, LocalDateWeekService localDateWeekService) {
        this.userRepository = userRepository;
        this.workoutWeekService = workoutWeekService;
        this.localDateWeekService = localDateWeekService;
    }


    @Override
    public User getUserById(Long id) {
        log.trace("Get User with id: {}", id);
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<User> getAllUsers() {
        log.trace("Get all Users");
        return userRepository.findAll();
    }

    @Override
    public void create(User user) throws EntityAlreadyExistsException {
        if (userRepository.existsById(user.getId())) {
            throw new EntityAlreadyExistsException(String.format("User with this id(%d) already exists", user.getId()));
        }

        log.trace("Create User: {}", user);
        save(user);

        workoutWeekService.create(new WorkoutWeek(user, localDateWeekService.getFirstDateOfWeekFromDate(LocalDate.now()), localDateWeekService.getLastDateOfWeekFromDate(LocalDate.now())));
    }

    @Override
    public void save(User user) {
        log.trace("Save User: {}", user);
        userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        log.trace("Delete User with id: {}", id);
        userRepository.deleteById(id);
    }

}
