package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
//@RequestMapping("/meals")
public class JspMealController {
    @Autowired
    MealService mealService;

    @GetMapping("/meals")
    public String getMeals(Model model) {
        int userId = SecurityUtil.authUserId();
        int userCalories = SecurityUtil.authUserCaloriesPerDay();
        model.addAttribute("meals", MealsUtil.getTos(mealService.getAll(userId), userCalories));
        return "meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        int id = getId(request);
        mealService.delete(id, userId);

        return "redirect:meals";
    }

    @GetMapping("/create")
    public String create(HttpServletRequest request) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        request.setAttribute("meal", meal);

        return "mealForm";
    }

    @GetMapping("/update")
    public String update(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        int id = getId(request);
        Meal meal = mealService.get(id, userId);
        request.setAttribute("meal", meal);

        return "mealForm";
    }

    @GetMapping("/filter")
    public String filter(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        int userCalories = SecurityUtil.authUserCaloriesPerDay();

        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        List<Meal> mealsDateFiltered = mealService.getBetweenDates(startDate, endDate, userId);
        model.addAttribute("meals", MealsUtil.getFilteredTos(mealsDateFiltered, userCalories, startTime, endTime));

        return "meals";
    }

    @PostMapping("/meals")
    public String setMeal(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.isEmpty(request.getParameter("id"))) {
            mealService.create(meal, userId);
        } else {
            meal.setId(getId(request));
            mealService.update(meal, userId);
        }

        return "redirect:meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
