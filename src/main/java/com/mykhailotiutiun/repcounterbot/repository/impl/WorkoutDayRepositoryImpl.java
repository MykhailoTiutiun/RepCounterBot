package com.mykhailotiutiun.repcounterbot.repository.impl;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.mapper.WorkoutDayMapper;
import com.mykhailotiutiun.repcounterbot.mapper.WorkoutWeekMapper;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutDayRepository;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutWeekRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class WorkoutDayRepositoryImpl implements WorkoutDayRepository {

    private final JdbcTemplate jdbcTemplate;
    private final WorkoutWeekRepository workoutWeekRepository;

    public WorkoutDayRepositoryImpl(JdbcTemplate jdbcTemplate, WorkoutWeekRepository workoutWeekRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.workoutWeekRepository = workoutWeekRepository;
    }

    private boolean existsById(Long id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM workout_days WHERE id = ?)", Boolean.class, id));
    }

    @Override
    public Optional<WorkoutDay> findById(Long id) {
        WorkoutDay workoutDay = jdbcTemplate.queryForObject("SELECT * FROM workout_days WHERE id = ?", new WorkoutDayMapper(), id);
        if (workoutDay != null) {
            workoutDay.setWorkoutWeek(workoutWeekRepository.findById(workoutDay.getWorkoutWeek().getId()).orElseThrow(EntityNotFoundException::new));
        }
        return Optional.ofNullable(workoutDay);
    }

    @Override
    public List<WorkoutDay> findAllByWorkoutWeek(WorkoutWeek workoutWeek) {
        List<WorkoutDay> workoutDays = jdbcTemplate.query("SELECT * FROM workout_days WHERE workout_week_id = ?", new WorkoutDayMapper(), workoutWeek.getId());
        if (!workoutDays.isEmpty()){
            WorkoutWeek workoutWeekFromDb = jdbcTemplate.queryForObject("SELECT * FROM workout_weeks where id = ?", new WorkoutWeekMapper(), workoutWeek.getId());
            workoutDays.forEach(workoutDay -> workoutDay.setWorkoutWeek(workoutWeekFromDb));
        }
        return workoutDays;
    }

    @Override
    public WorkoutDay save(WorkoutDay workoutDay) {
        if(existsById(workoutDay.getId())){
            jdbcTemplate.update("UPDATE workout_days SET name = ?, is_workout_day = ?, date = ?, workout_week_id = ? WHERE id = ?",
                    workoutDay.getName(),
                    workoutDay.getIsWorkoutDay(),
                    workoutDay.getDate(),
                    workoutDay.getWorkoutWeek().getId(),
                    workoutDay.getId());
        } else {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("workout_days").usingGeneratedKeyColumns("id");
            Map<String, Object> parameters = new HashMap<>(4);
            parameters.put("name", workoutDay.getName());
            parameters.put("date", workoutDay.getDate());
            parameters.put("is_workout_day", workoutDay.isWorkoutDay());
            parameters.put("workout_week_id", workoutDay.getWorkoutWeek().getId());
            workoutDay.setId((Long) simpleJdbcInsert.executeAndReturnKey(parameters));
        }
        return workoutDay;
    }
}
