package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal.getUserId(), meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {} for {}", meal, userId);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but not present in storage

        if (meal.getUserId() != userId) {
            throw new IllegalArgumentException("Wrong user id!");
        }

        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {} for {}", id, userId);

        Meal meal = checkNotFoundWithId(repository.get(id), id);

        if (meal.getUserId() != userId) {
            throw new IllegalArgumentException("Wrong user id!");
        }

        return repository.remove(id, meal);
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get {} for {}", id, userId);

        Meal meal = checkNotFoundWithId(repository.get(id), id);

        if (meal.getUserId() != userId) {
            throw new IllegalArgumentException("Wrong user id!");
        }

        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");

        return getAllFiltered(userId, meal -> meal.getUserId() == userId);
    }

    @Override
    public List<Meal> getAllFilteredWithDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAll filtered by date and time");

        return getAllFiltered(userId, meal ->
                meal.getUserId() == userId && DateTimeUtil.isBetween(meal.getDate(), startDate, endDate));
    }

    private List<Meal> getAllFiltered(int userId, Predicate<Meal> filter) {
        return repository.values()
                .stream()
                .filter(filter)
                .sorted(Collections.reverseOrder(Comparator.comparing(Meal::getDate)))
                .collect(Collectors.toList());
    }
}

