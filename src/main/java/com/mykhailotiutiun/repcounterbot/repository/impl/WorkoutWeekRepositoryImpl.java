package com.mykhailotiutiun.repcounterbot.repository.impl;

import com.mykhailotiutiun.repcounterbot.mapper.UserMapper;
import com.mykhailotiutiun.repcounterbot.mapper.WorkoutWeekMapper;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutWeekRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class WorkoutWeekRepositoryImpl implements WorkoutWeekRepository {

    private final JdbcTemplate jdbcTemplate;

    public WorkoutWeekRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private boolean existsById(Long id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM workout_weeks WHERE id = ?)", Boolean.class, id));
    }

    @Override
    public Optional<WorkoutWeek> findById(Long id) {
        WorkoutWeek workoutWeek = jdbcTemplate.queryForObject("SELECT * FROM workout_weeks WHERE id = ?", new WorkoutWeekMapper(), id);
        if(workoutWeek != null){
            workoutWeek.setUser(jdbcTemplate.queryForObject("SELECT * FROM bot_users WHERE id = ?", new UserMapper(), workoutWeek.getUser().getId()));
        }
        return Optional.ofNullable(workoutWeek);
    }

    @Override
    public Optional<WorkoutWeek> findByUserIdAndCurrent(Long userId, Boolean isCurrent) {
        WorkoutWeek workoutWeek = jdbcTemplate.queryForObject("SELECT * FROM workout_weeks WHERE user_id = ? AND current = ?", new WorkoutWeekMapper(), userId, isCurrent);
        if(workoutWeek != null){
            workoutWeek.setUser(jdbcTemplate.queryForObject("SELECT * FROM bot_users WHERE id = ?", new UserMapper(), userId));
        }
        return Optional.ofNullable(workoutWeek);
    }

    @Override
    public WorkoutWeek save(WorkoutWeek workoutWeek) {
        if(existsById(workoutWeek.getId())){
            jdbcTemplate.update("UPDATE workout_weeks SET current = ?, week_start_date = ?, week_end_date = ?, user_id = ? WHERE id = ?",
                    workoutWeek.getCurrent(),
                    workoutWeek.getWeekStartDate(),
                    workoutWeek.getWeekEndDate(),
                    workoutWeek.getUser().getId(),
                    workoutWeek.getId());
        } else {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("workout_weeks").usingGeneratedKeyColumns("id");
            Map<String, Object> parameters = new HashMap<>(4);
            parameters.put("current", workoutWeek.getCurrent());
            parameters.put("week_start_date", workoutWeek.getWeekStartDate());
            parameters.put("week_end_date", workoutWeek.getWeekEndDate());
            parameters.put("user_id", workoutWeek.getUser().getId());
            workoutWeek.setId((Long) simpleJdbcInsert.executeAndReturnKey(parameters));
        }
        return workoutWeek;
    }
}
