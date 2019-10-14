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

@Service
public class MealService {

    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(int userId, Meal meal) {
        return repository.save(userId, meal);
    }

    public void delete(int userId, int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(userId, id), id);
    }

    public Meal get(int userId, int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(userId, id), id);
    }

    public List<Meal> getAll(int userId) {
        List<Meal> resultList = repository.getAll(userId);
        return resultList == null ? Collections.emptyList() : resultList;
    }

    public List<Meal> getAllFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        return repository.getAllFilteredWithDate(userId, startDate, endDate);
    }

    public void update(int userId, Meal meal) throws NotFoundException {
        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }

}