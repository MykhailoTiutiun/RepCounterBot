package com.mykhailotiutiun.repcounterbot;

import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.UserRepository;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutWeekRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class RepositoriesTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    WorkoutWeekRepository workoutWeekRepository;

    @Test
    public void workoutWeekRepositoryTest(){
        WorkoutWeek workoutWeek = new WorkoutWeek();
        workoutWeekRepository.save(workoutWeek);

        assertTrue(workoutWeekRepository.findById(workoutWeek.getId()).isPresent());

        workoutWeekRepository.delete(workoutWeek);
    }

    @Test
    public void workoutWeekRepositoryAdvancedTest(){
        Random random = new Random();
        User user = new User(random.nextLong(), "TestUser");

        userRepository.save(user);

        WorkoutWeek workoutWeek = new WorkoutWeek(user, LocalDate.now(), LocalDate.now());
        workoutWeekRepository.save(workoutWeek);

        assertTrue(workoutWeekRepository.findById(workoutWeek.getId()).isPresent());

        workoutWeekRepository.delete(workoutWeek);

        userRepository.delete(user);
    }
}
