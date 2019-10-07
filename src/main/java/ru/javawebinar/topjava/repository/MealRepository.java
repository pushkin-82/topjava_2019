package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository  {
    List<Meal> getAllMeals();
    Meal getMealById(long id);
    void saveMeal(Meal meal);
    void deleteMealById(long id);
}
