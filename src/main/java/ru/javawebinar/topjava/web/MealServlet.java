package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryMemoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletConfig;
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

    private MealRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new MealRepositoryMemoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("GET request");
        String action = (request.getParameter("action"));

        if (action == null) {
            action = "getall";
        } else {
            action = action.toLowerCase();
        }

        switch (action) {
            case "delete": {
                long mealId = Long.parseLong(request.getParameter("id"));
                repository.delete(mealId);
                response.sendRedirect("meals");

                break;
            }
            case "create": {
                Meal meal = new Meal(-1L, LocalDateTime.now(), "default", 0);

                request.setAttribute("meal", meal);
                request.setAttribute("formatter", TimeUtil.getDateTimeFormatter());
                request.getRequestDispatcher("/update.jsp").forward(request, response);

                break;
            }
            case "update": {
                long id = Long.parseLong(Objects.requireNonNull(request.getParameter("id")));

                Meal meal = repository.getById(id);

                request.setAttribute("meal", meal);
                request.setAttribute("formatter", TimeUtil.getDateTimeFormatter());
                request.getRequestDispatcher("/update.jsp").forward(request, response);

                break;
            }
            case "getall":
            default:
                List<MealTo> meals = MealsUtil.getMealToList(repository.getAll());
                request.setAttribute("meals", meals);
                request.setAttribute("formatter", TimeUtil.getDateTimeFormatter());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);

                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("POST request");
        request.setCharacterEncoding("UTF-8");

        long id = Long.parseLong(Objects.requireNonNull(request.getParameter("id")));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        Meal newMeal = new Meal(id, dateTime, description, calories);

        repository.save(newMeal);

        response.sendRedirect("meals");
    }
}