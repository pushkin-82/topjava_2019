package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getByIdAndMeals() {
        User user = service.getByIdAndMeals(UserTestData.USER_ID);
        UserTestData.assertMatch(user, UserTestData.USER);
        MealTestData.assertMatch(user.getMeals(), MealTestData.MEALS);
    }

    @Test
    public void getNotFoundByIdAndMeals() {
        thrown.expect(NotFoundException.class);
        service.getByIdAndMeals(1);
    }
}
