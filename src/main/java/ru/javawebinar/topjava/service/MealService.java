package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;
import static ru.javawebinar.topjava.web.SecurityUtil.getAuthUserId;

@Service
public class MealService {

    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        return repository.save(getAuthUserId(), meal);
    }

    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(getAuthUserId(), id), id);
    }

    public Meal get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(getAuthUserId(), id), id);
    }

    public List<Meal> getAll() {
        return repository.getAll(getAuthUserId());
    }

    public List<Meal> getAllFiltered(LocalDate startDate, LocalDate endDate) {
        return repository.getAllFilteredWithDate(getAuthUserId(), startDate, endDate);
    }

    public void update(Meal meal) throws NotFoundException {
        checkNotFoundWithId(repository.save(getAuthUserId(), meal), meal.getId());
    }

}