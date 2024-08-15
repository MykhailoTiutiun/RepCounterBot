package com.mykhailotiutiun.repcounterbot.repository.impl;

import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.mapper.WorkoutExerciseMapper;
import com.mykhailotiutiun.repcounterbot.model.WorkoutDay;
import com.mykhailotiutiun.repcounterbot.model.WorkoutExercise;
import com.mykhailotiutiun.repcounterbot.model.WorkoutSet;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutDayRepository;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutExerciseRepository;
import com.mykhailotiutiun.repcounterbot.repository.WorkoutSetRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class WorkoutExerciseRepositoryImpl implements WorkoutExerciseRepository {

    private final JdbcTemplate jdbcTemplate;
    private final WorkoutDayRepository workoutDayRepository;
    private final WorkoutSetRepository workoutSetRepository;

    public WorkoutExerciseRepositoryImpl(JdbcTemplate jdbcTemplate, WorkoutDayRepository workoutDayRepository, WorkoutSetRepository workoutSetRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.workoutDayRepository = workoutDayRepository;
        this.workoutSetRepository = workoutSetRepository;
    }

    @Override
    public Optional<WorkoutExercise> findById(Long id) {
        WorkoutExercise workoutExercise = jdbcTemplate.queryForObject("SELECT * FROM workout_exercises WHERE id = ?", new WorkoutExerciseMapper(), id);
        if (workoutExercise != null) {
            workoutExercise.setWorkoutDay(workoutDayRepository.findById(workoutExercise.getWorkoutDay().getId()).orElseThrow(EntityNotFoundException::new));
            workoutExercise.setWorkoutSets(workoutSetRepository.findAllByWorkoutExercise(workoutExercise));
        }
        return Optional.ofNullable(workoutExercise);
    }

    @Override
    public Optional<WorkoutExercise> findByNumberAndWorkoutDay(Byte number, WorkoutDay workoutDay) {
        WorkoutExercise workoutExercise = jdbcTemplate.queryForObject("SELECT * FROM workout_exercises WHERE number = ? AND workout_day_id = ?", new WorkoutExerciseMapper(), number, workoutDay.getId());
        if (workoutExercise != null) {
            workoutExercise.setWorkoutDay(workoutDayRepository.findById(workoutExercise.getWorkoutDay().getId()).orElseThrow(EntityNotFoundException::new));
            workoutExercise.setWorkoutSets(workoutSetRepository.findAllByWorkoutExercise(workoutExercise));
            workoutExercise.setPrevWorkoutSets(workoutSetRepository.findAllPrevSetsByWorkoutExercise(workoutExercise));
        }
        return Optional.ofNullable(workoutExercise);
    }

    @Override
    public List<WorkoutExercise> findAllByWorkoutDayOrderByNumberDesc(WorkoutDay workoutDay) {
        return jdbcTemplate.query("SELECT * FROM workout_exercises WHERE workout_day_id = ? ORDER BY number DESC", new WorkoutExerciseMapper(), workoutDay.getId());
    }

    private boolean existsById(Long id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM workout_exercises WHERE id = ?)", Boolean.class, id));
    }

    @Override
    public WorkoutExercise save(WorkoutExercise workoutExercise) {
        if(existsById(workoutExercise.getId())){
            jdbcTemplate.update("UPDATE workout_exercises SET number = ?, name = ?, workout_day_id = ? WHERE id = ?",
                    workoutExercise.getNumber(),
                    workoutExercise.getName(),
                    workoutExercise.getWorkoutDay().getId(),
                    workoutExercise.getId());
        } else {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("workout_exercises").usingGeneratedKeyColumns("id");
            Map<String, Object> parameters = new HashMap<>(3);
            parameters.put("number", workoutExercise.getNumber());
            parameters.put("workout_day_id", workoutExercise.getWorkoutDay().getId());
            parameters.put("name", workoutExercise.getName());
            workoutExercise.setId((Long) simpleJdbcInsert.executeAndReturnKey(parameters));
        }
        updateWorkoutSets(workoutExercise);
        updatePrevWorkoutSets(workoutExercise);
        return workoutExercise;
    }

    private void updateWorkoutSets(WorkoutExercise workoutExercise){
        if (workoutExercise.getWorkoutSets() == null || workoutExercise.getWorkoutSets().isEmpty()){
            return;
        }
        List<WorkoutSet> workoutSets = workoutExercise.getWorkoutSets();
        String sql = "UPDATE workout_sets SET workout_ex_id = ? WHERE id IN (" +
                workoutSets.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";
        Object[] params = new Object[workoutSets.size() + 1];
        params[0] = workoutExercise.getId();
        for (int i = 0; i < workoutSets.size(); i++){
            params[i + 1] = workoutSets.get(i).getId();
        }
        jdbcTemplate.update(sql, params);
    }

    private void updatePrevWorkoutSets(WorkoutExercise workoutExercise) {
        if (workoutExercise.getPrevWorkoutSets() == null || workoutExercise.getPrevWorkoutSets().isEmpty()){
            return;
        }
        List<WorkoutSet> workoutSets = workoutExercise.getPrevWorkoutSets();
        String sql = "UPDATE workout_sets SET prev_workout_ex_id = ? WHERE id IN (" +
                workoutSets.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";
        Object[] params = new Object[workoutSets.size() + 1];
        params[0] = workoutExercise.getId();
        for (int i = 0; i < workoutSets.size(); i++){
            params[i + 1] = workoutSets.get(i).getId();
        }
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM workout_exercises WHERE id = ?", id);
    }
}
