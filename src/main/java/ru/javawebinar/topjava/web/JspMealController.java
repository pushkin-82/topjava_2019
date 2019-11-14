package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Controller
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
        int id = Integer.parseInt(request.getParameter("id"));
        mealService.delete(id, userId);

        return "redirect:meals";
    }

//    @GetMapping("/create")
//    public void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int userId = SecurityUtil.authUserId();
//        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
//        request.setAttribute("meal", meal);
//        request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
//
////        return "redirect:create";
//    }

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
            mealService.update(meal, userId);
        }

        return "redirect:meals";
    }
}
