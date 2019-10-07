package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryMemoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;


public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealRepository repository = new MealRepositoryMemoryImpl();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("GET request");
        String action = request.getParameter("action");

        if (action == null) {
            action = "getAll";
        }

        if (action.equalsIgnoreCase("delete")) {
            long mealId = Long.parseLong(request.getParameter("id"));
            repository.deleteMealById(mealId);
            response.sendRedirect("meals");

        } else if (action.equalsIgnoreCase("get")) {
            long mealId = Long.parseLong(request.getParameter("id"));
            repository.getMealById(mealId);
            response.sendRedirect("meals");

        } else if (action.equalsIgnoreCase("getAll")) {
            List<MealTo> meals = MealsUtil.getMealToList(repository.getAllMeals());
            request.setAttribute("meals", meals);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);

        } else if (action.equalsIgnoreCase("create")) {
            Meal meal = new Meal(LocalDateTime.now(), "default", 0);

            repository.saveMeal(meal);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/update.jsp").forward(request, response);

        } else if (action.equalsIgnoreCase("update")) {
            long id = Long.parseLong(Objects.requireNonNull(request.getParameter("id")));

            Meal meal = repository.getMealById(id);

            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/update.jsp").forward(request, response);

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("POST request");
        request.setCharacterEncoding("UTF-8");

        long id = Long.parseLong(request.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        Meal newMeal = new Meal(id, dateTime, description, calories);

        repository.saveMeal(newMeal);

        response.sendRedirect("meals");
    }
}