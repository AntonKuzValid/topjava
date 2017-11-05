package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
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


import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private MealService mealService = new MealServiceImp();

    List<Meal> meals = mealService.listMeals();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals");

        List<MealWithExceed> mealWithExceeds = MealsUtil.getFilteredWithExceeded(meals, LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("mealWithExceeds", mealWithExceeds);

        request.getRequestDispatcher("meal.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        String mealIdForRemove = req.getParameter("mealIdForRemove");
        if (mealIdForRemove != null) {
            log.debug("remove meal");
            mealService.removeMeal(Integer.parseInt(mealIdForRemove));
        }

        log.debug("Get params");
        String date = req.getParameter("Date");
        String description = req.getParameter("Description");
        String calories = req.getParameter("Calories");
        String mealIdForEdit = req.getParameter("mealIdForEdit");
        boolean isEdit = mealIdForEdit != null;
        Meal meal;
        if (date != null && description != null && calories != null) {
            meal = new Meal(LocalDateTime.parse(date), description, Integer.parseInt(calories));

            if (isEdit  || (req.getParameter("isEdit") != null && req.getParameter("isEdit").equals("true"))) {
                log.debug("Edit meal");
                meal.setId(Integer.parseInt(req.getParameter("mealId")));
                mealService.updateMeal(meal);
            } else {
                log.debug("Add meal");
                mealService.addMeal(meal);
            }
        }
        req.setAttribute("isEdit", isEdit);
        if (mealIdForEdit != null && !mealIdForEdit.isEmpty()) {
            req.setAttribute("mealId", mealIdForEdit);
        }

        doGet(req, resp);
    }
}
