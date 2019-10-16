package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.web.SecurityUtil.getAuthUserId;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS_USER_1.forEach(meal -> save(1, meal));
        MealsUtil.MEALS_USER_2.forEach(meal -> save(2, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {} for {}", meal, userId);

        Map<Integer, Meal> currentUserMeals = new HashMap<>();

        if (repository.containsKey(userId)) {
            currentUserMeals = repository.get(userId);
        } else {
            repository.put(userId, currentUserMeals);
        }

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            currentUserMeals.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but not present in storage

        return currentUserMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {} for {}", id, userId);

        Map<Integer, Meal> currentUserMeals = repository.getOrDefault(userId, Collections.emptyMap());

        Meal meal = currentUserMeals.get(id);

        return meal != null && currentUserMeals.remove(id, meal);
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get {} for {}", id, userId);

        Map<Integer, Meal> tmpMap = repository.getOrDefault(userId, Collections.emptyMap());

        return tmpMap.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");

        return getAllFiltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllFilteredWithDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAll filtered by date and time");

        return getAllFiltered(userId, meal ->
                 DateTimeUtil.isBetween(meal.getDate(), startDate, endDate));
    }

    private List<Meal> getAllFiltered(Integer userId, Predicate<Meal> filter) {
        return userId == null ? Collections.emptyList() : repository.get(userId).values()
                .stream()
//                .filter(meal -> meal.getUserId().equals(userId))
                .filter(filter)
                .sorted(Collections.reverseOrder(Comparator.comparing(Meal::getDate)))
                .collect(Collectors.toList());
    }
}

