package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-jdbc.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal userMeal = service.get(USER_MEAL_1.getId(), USER_ID);
        assertMatch(userMeal, USER_MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(3, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getAnotherUserMeal() {
        service.get(USER_MEAL_1.getId(), ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_1.getId(), USER_ID);
        List<Meal> newUserMeals = Arrays.asList(USER_MEAL_6, USER_MEAL_5, USER_MEAL_4,
                USER_MEAL_3, USER_MEAL_2);

        assertMatch(service.getAll(USER_ID), newUserMeals);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAnotherUserMeal() {
        service.delete(ADMIN_MEAL_1.getId(), USER_ID);
    }

    @Test
    public void getBetweenDates() {
        LocalDate startDate = LocalDate.of(2015, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2015, Month.NOVEMBER, 1);

        List<Meal> getFilteredUserMeals = service.getBetweenDates(startDate, endDate, USER_ID);
        assertMatch(getFilteredUserMeals, USER_MEALS);
    }

    @Test
    public void getBetweenDatesNullStartDate() {
        LocalDate endDate = LocalDate.of(2015, Month.MAY, 30);

        List<Meal> getFilteredUserMeals = service.getBetweenDates(null, endDate, USER_ID);
        assertMatch(getFilteredUserMeals, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void getBetweenDatesNullEndDate() {
        LocalDate startDate = LocalDate.of(2015, Month.MAY, 31);

        List<Meal> getFilteredUserMeals = service.getBetweenDates(startDate, null, USER_ID);
        assertMatch(getFilteredUserMeals, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4);
    }

    @Test
    public void getBetweenDatesNullDates() {
        List<Meal> getFilteredUserMeals = service.getBetweenDates(null, null, USER_ID);
        assertMatch(getFilteredUserMeals, USER_MEALS);
    }

    @Test
    public void getBetweenDatesWrongDateOrder() {
        LocalDate startDate = LocalDate.of(2015, Month.MAY, 31);
        LocalDate endDate = LocalDate.of(2015, Month.MAY, 30);

        List<Meal> getFilteredUserMeals = service.getBetweenDates(startDate, endDate, USER_ID);
        assertMatch(getFilteredUserMeals, Collections.emptyList());
    }

    @Test
    public void getAll() {
        List<Meal> getUserMeals = service.getAll(USER_ID);
        assertMatch(getUserMeals, USER_MEALS);
    }

    @Test
    public void update() {
        Meal updated = new Meal(USER_MEAL_3);
        updated.setDescription("New description");
        updated.setCalories(150);
        service.update(updated, USER_ID);
        assertMatch(service.get(USER_MEAL_3.getId(), USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateAnotherUserMeal() {
        Meal updated = new Meal(USER_MEAL_3);
        updated.setDescription("New description");
        updated.setCalories(150);
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(null, LocalDateTime.now(), "Dinner", 500);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        List<Meal> newUserMeals = Arrays.asList(newMeal, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4,
                USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
        assertMatch(service.getAll(USER_ID), newUserMeals);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateTimeCreate() {
        Meal newMeal = new Meal(null,
                LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Lunch", 500);
        service.create(newMeal, USER_ID);
    }

}