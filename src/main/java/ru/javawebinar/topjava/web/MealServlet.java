package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImp;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.List;


import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private MealService mealService = MealServiceImp.init();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals");

        String action = request.getParameter("action");
        switch (action == null ? "info" : action) {
            case "delete": {
                log.debug("delete meal");
                String mealIdForDelete = request.getParameter("mealIdForDelete");
                mealService.remove(Integer.parseInt(mealIdForDelete));

                response.sendRedirect("http://localhost:8080/topjava/meal");
                return;
            }
            case "isupdate": {
                log.debug("Put data for update");
                String mealIdForUpdate = request.getParameter("mealIdForUpdate");
                request.setAttribute("mealIdForUpdate", mealIdForUpdate);
                request.setAttribute("isUpdate", true);
                Meal meal = mealService.getById(Integer.parseInt(mealIdForUpdate));
                request.setAttribute("meal",meal==null?new Meal(LocalDateTime.now(),"",0):meal);
                break;
            }
            case "info":
            default:
        }
        log.debug("Forward to meal.jsp from GET");
        request.setAttribute("mealList", MealsUtil.getFilteredWithExceeded(mealService.list(), LocalTime.MIN, LocalTime.MAX, 2000));
        request.getRequestDispatcher("/meal.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        log.debug("Get params");
        String date = req.getParameter("Date");
        String description = req.getParameter("Description");
        String calories = req.getParameter("Calories");
        String mealIdForUpdate = req.getParameter("mealIdForUpdate");
        Meal meal = new Meal(LocalDateTime.parse(date), description, Integer.parseInt(calories));
        ;

        String action = req.getParameter("action");
        switch (action == null ? "info" : action) {
            case "add": {
                log.debug("Add meal");
                mealService.add(meal);
                resp.sendRedirect("http://localhost:8080/topjava/meal");
                return;
            }
            case "update": {
                log.debug("Update meal");
                meal.setId(Integer.parseInt(mealIdForUpdate));
                mealService.update(meal);
                resp.sendRedirect("http://localhost:8080/topjava/meal");
                return;
            }
            case "info":
            default:
        }
        log.debug("Forward to meal.jsp from POST");
        req.setAttribute("mealList", MealsUtil.getFilteredWithExceeded(mealService.list(), LocalTime.MIN, LocalTime.MAX, 2000));
        req.getRequestDispatcher("/meal.jsp").forward(req, resp);
    }
}
