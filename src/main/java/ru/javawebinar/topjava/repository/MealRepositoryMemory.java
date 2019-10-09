package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealRepositoryMemory implements MealRepository {
    private Map<Long, Meal> mealMap = new ConcurrentHashMap<>();
    private AtomicLong counter = new AtomicLong();

    {
        MealsUtil.meals.forEach(this::save);
    }


    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal getById(long id) {
        return mealMap.get(id);
    }

    @Override
    public Meal save(Meal meal) {
        if (!mealMap.containsKey(meal.getId())) {
            meal.setId(counter.incrementAndGet());
        }
        mealMap.put(meal.getId(), meal);

        return meal;
    }

    @Override
    public void delete(long id) {
        mealMap.remove(id);
    }
}
