package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealRepositoryMemoryImpl implements MealRepository {
    private Map<Long, Meal> mealMap = new ConcurrentHashMap<>();
    private static AtomicLong counter = new AtomicLong();

    {
        MealsUtil.meals.forEach(this::saveMeal);
    }


    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal getMealById(long id) {
        return mealMap.get(id);
    }

    @Override
    public void saveMeal(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(counter.incrementAndGet());
        }
        mealMap.put(meal.getId(), meal);
    }

    @Override
    public void deleteMealById(long id) {
        mealMap.remove(id);
    }
}
