package com.mykhailotiutiun.repcounterbot.service.Impl;

import com.mykhailotiutiun.repcounterbot.cache.SelectedLanguageCache;
import com.mykhailotiutiun.repcounterbot.exception.EntityAlreadyExistsException;
import com.mykhailotiutiun.repcounterbot.exception.EntityNotFoundException;
import com.mykhailotiutiun.repcounterbot.language.SelectedLanguageProvider;
import com.mykhailotiutiun.repcounterbot.model.User;
import com.mykhailotiutiun.repcounterbot.model.WorkoutWeek;
import com.mykhailotiutiun.repcounterbot.repository.UserRepository;
import com.mykhailotiutiun.repcounterbot.service.UserService;
import com.mykhailotiutiun.repcounterbot.service.WorkoutWeekService;
import com.mykhailotiutiun.repcounterbot.util.LocalDateWeekUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
public class UserServiceImpl implements UserService, SelectedLanguageProvider {

    private final UserRepository userRepository;
    private final WorkoutWeekService workoutWeekService;
    private final LocalDateWeekUtil localDateWeekUtil;
    private final SelectedLanguageCache selectedLanguageCache;

    public UserServiceImpl(UserRepository userRepository, WorkoutWeekService workoutWeekService, LocalDateWeekUtil localDateWeekUtil, SelectedLanguageCache selectedLanguageCache) {
        this.userRepository = userRepository;
        this.workoutWeekService = workoutWeekService;
        this.localDateWeekUtil = localDateWeekUtil;
        this.selectedLanguageCache = selectedLanguageCache;
    }


    @Override
    public User getById(Long id) {
        log.trace("Get User by id: {}", id);
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public void create(User user) throws EntityAlreadyExistsException {
        if (userRepository.existsById(user.getId())) {
            throw new EntityAlreadyExistsException(String.format("User with id(%d) already exists", user.getId()));
        }

        log.trace("Create User: {}", user);
        save(user);

        workoutWeekService.create(WorkoutWeek.builder()
                .user(user)
                .current(true)
                .weekStartDate(localDateWeekUtil.getFirstDateOfWeekFromDate(LocalDate.now()))
                .weekEndDate(localDateWeekUtil.getLastDateOfWeekFromDate(LocalDate.now()))
                .build());
    }

    private void save(User user) {
        log.trace("Save User: {}", user);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void setUserLang(Long userId, String localTag) {
        User user = getById(userId);

        log.trace("Set localTag for User: {}", user);

        user.setLocalTag(localTag);
        save(user);
        selectedLanguageCache.setSelectedLanguage(String.valueOf(userId), localTag);
    }

    @Override
    public String getLocaleTag(String chatId) {
        log.trace("Get User's local-tag by id: {}", chatId);
        return userRepository.findById(Long.valueOf(chatId)).orElseThrow(EntityNotFoundException::new).getLocalTag();
    }
}
