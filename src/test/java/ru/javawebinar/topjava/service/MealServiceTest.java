package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
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
    public void get() throws Exception {
        Meal userMeal = service.get(MEAL_ID + 2, USER_ID);
        assertMatch(userMeal, USER_MEAL_1);

        Meal adminMeal = service.get(MEAL_ID + 8, ADMIN_ID);
        assertMatch(adminMeal, ADMIN_MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        Meal userMeal = service.get(MEAL_ID + 2, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getAnotherUserMeal() throws Exception {
        Meal userMeal = service.get(3, USER_ID);
    }

    @Test
    public void delete() throws Exception {
        service.delete(MEAL_ID + 2, USER_ID);
        List<Meal> newUserMeals = Arrays.asList(USER_MEAL_6, USER_MEAL_5, USER_MEAL_4,
                USER_MEAL_3, USER_MEAL_2);

        assertMatch(service.getAll(USER_ID), newUserMeals);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        service.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAnotherUserMeal() throws Exception {
        service.delete(MEAL_ID + 8, USER_ID);
    }

    @Test
    public void getBetweenDates() {
        LocalDate startDate = LocalDate.of(2015, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2015, Month.NOVEMBER, 1);

        List<Meal> getFilteredUserMeal = service.getBetweenDates(startDate, endDate, USER_ID);
        assertMatch(getFilteredUserMeal, USER_MEALS);

        List<Meal> getDefaultFilteredUserMeal = service.getBetweenDates(null, null, USER_ID);
        assertMatch(getDefaultFilteredUserMeal, USER_MEALS);

        startDate = LocalDate.of(2015, Month.MAY, 31);
        List<Meal> getFilteredUserMeal_2 = service.getBetweenDates(startDate, null, USER_ID);
        assertMatch(getFilteredUserMeal_2, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4);

        endDate = LocalDate.of(2015, Month.MAY, 30);
        List<Meal> getFilteredUserMeal_3 = service.getBetweenDates(null, endDate, USER_ID);
        assertMatch(getFilteredUserMeal_3, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);

        startDate = LocalDate.of(2015, Month.MAY, 31);
        endDate = LocalDate.of(2015, Month.MAY, 30);
        List<Meal> getEmptyFilteredUserMeal = service.getBetweenDates(startDate, endDate, USER_ID);
        assertMatch(getEmptyFilteredUserMeal, Collections.emptyList());
    }

    @Test
    public void getAll() {
        List<Meal> getUserMeals = service.getAll(USER_ID);
        assertMatch(getUserMeals, USER_MEALS);

        List<Meal> getAdminMeals = service.getAll(ADMIN_ID);
        List<Meal> adminMeals = Arrays.asList(ADMIN_MEAL_2, ADMIN_MEAL_1);
        assertMatch(getAdminMeals, adminMeals);
    }

    @Test
    public void update() {
        Meal updated = new Meal(USER_MEAL_3);
        updated.setDescription("New description");
        updated.setCalories(150);
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_ID + 4, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateAnotherUserMeal() throws Exception {
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
}