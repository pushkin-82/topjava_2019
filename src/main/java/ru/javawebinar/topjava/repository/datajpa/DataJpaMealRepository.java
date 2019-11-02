package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.getEndExclusive;
import static ru.javawebinar.topjava.util.DateTimeUtil.getStartInclusive;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_DATE_TIME = Sort.by(Sort.Direction.DESC, "dateTime");

    @Autowired
    private CrudMealRepository crudRepository;

    @Autowired
    private DataJpaUserRepository dataJpaUserRepository;

    @Override
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        meal.setUser(dataJpaUserRepository.get(userId));

        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudRepository.findById(id).orElse(null);
        return meal != null && meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAll(SORT_DATE_TIME)
                .stream()
                .filter(meal -> meal.getUser().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetweenInclusive(LocalDate startDate, LocalDate endDate, int userId) {
        return crudRepository.getAllBetweenInclusive(getStartInclusive(startDate), getEndExclusive(endDate), userId);
    }
}
