package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.getAuthUserId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(getAuthUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getAllFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAll filtered by date & time");

        LocalDate startLD = startDate == null ? LocalDate.MIN : startDate;
        LocalDate endLD = endDate == null ? LocalDate.MAX : endDate;
        LocalTime startLT = startTime == null ? LocalTime.MIN : startTime;
        LocalTime endLT = endTime == null ? LocalTime.MAX : endTime;

        return MealsUtil.getFilteredTos(service.getAllFiltered(getAuthUserId(), startLD, endLD),
                authUserCaloriesPerDay(), startLT, endLT);
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(getAuthUserId(), id);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(getAuthUserId(), id);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(getAuthUserId(), meal);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(getAuthUserId(), meal);
    }

}