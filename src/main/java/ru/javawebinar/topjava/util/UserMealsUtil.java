package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
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
                                                                    LocalTime startTime, LocalTime endTime,
                                                                    int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesPerDay = new HashMap<>();

        for (UserMeal userMeal : mealList) {
            LocalDate userMealDate = userMeal.getDateTime().toLocalDate();
            int calories = totalCaloriesPerDay.getOrDefault(userMealDate, 0);
            totalCaloriesPerDay.put(userMealDate, calories + userMeal.getCalories());
        }

        List<UserMealWithExceed> resultList = new ArrayList<>();

        for (UserMeal userMeal : mealList) {
            LocalDate userMealDate = userMeal.getDateTime().toLocalDate();

            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                resultList.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(),
                        userMeal.getCalories(), totalCaloriesPerDay.get(userMealDate) > caloriesPerDay));
            }
        }

        return resultList;
    }

    public static List<UserMealWithExceed>  getFilteredWithExceededStreams(List<UserMeal> mealList,
                                                                    LocalTime startTime, LocalTime endTime,
                                                                    int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesPerDay = mealList.stream().
                collect(Collectors.toMap(meal -> meal.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));


        int totalCalories = mealList.stream().map(UserMeal::getCalories).reduce(0, Integer::sum);

        return mealList.stream().filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)).
                map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), totalCaloriesPerDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay)).
                collect(Collectors.toList());
    }
}
