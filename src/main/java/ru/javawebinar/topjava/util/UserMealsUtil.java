package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );

        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);

//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList,
                                                                    LocalTime startTime,
                                                                    LocalTime endTime,
                                                                    int caloriesPerDay) {
        int totalCalories = 0;

        for (UserMeal userMeal : mealList) {
            totalCalories += userMeal.getCalories();
        }

        List<UserMealWithExceed> resultList = new ArrayList<>();

            for (UserMeal userMeal : mealList) {
                if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                    resultList.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(),
                            userMeal.getCalories(), totalCalories > caloriesPerDay));
                }
            }

        return resultList;
    }

    public static List<UserMealWithExceed>  getFilteredWithExceededStreams(List<UserMeal> mealList,
                                                                    LocalTime startTime,
                                                                    LocalTime endTime,
                                                                    int caloriesPerDay) {

        int totalCalories = mealList.stream().map(UserMeal::getCalories).reduce(0, Integer::sum);

        return mealList.stream().filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)).
                map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), totalCalories > caloriesPerDay)).collect(Collectors.toList());
    }

}
