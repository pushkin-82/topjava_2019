package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository  {
    List<Meal> getAll();
    Meal get(int id);
    void save(Meal meal);
    void delete(int id);
}
