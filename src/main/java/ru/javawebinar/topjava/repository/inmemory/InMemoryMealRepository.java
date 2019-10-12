package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but not present in storage

        return meal.getUserId() ==  authUserId() ? repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int id) {
        return repository.get(id).getUserId() == authUserId() && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id) {
        return repository.get(id).getUserId() == authUserId() ? repository.get(id) : null;
    }

    @Override
    public Collection<Meal> getAll() {
        List<Meal> mealList = new ArrayList<>(repository.values());
        mealList.sort(Collections.reverseOrder(Comparator.comparing(Meal::getDate)));

        return mealList;
    }
}

