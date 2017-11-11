package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            mealRestController = appCtx.getBean(MealRestController.class);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String userId=request.getParameter("user");
        if (userId!=null){
            AuthorizedUser.setId(Integer.parseInt(userId));
            response.sendRedirect("meals");
            return;
        }

        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (id.isEmpty() )mealRestController.create(meal);
        else mealRestController.update(meal,Integer.valueOf(id));
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        LocalDate startLocalDate=request.getParameter("startDate")!=null&&!request.getParameter("startDate").isEmpty()?
                LocalDate.parse(request.getParameter("startDate")):null;
        LocalDate endLocalDate=request.getParameter("endDate")!=null&&!request.getParameter("endDate").isEmpty()?
                LocalDate.parse(request.getParameter("endDate")):null;
        LocalTime startLocalTime=request.getParameter("startTime")!=null&&!request.getParameter("startTime").isEmpty()?
                LocalTime.parse(request.getParameter("startTime")):null;
        LocalTime endLocalTime=request.getParameter("endTime")!=null&&!request.getParameter("endTime").isEmpty()?
                LocalTime.parse(request.getParameter("endTime")):null;

        String action = request.getParameter("action");

        if (startLocalDate!=null&&endLocalDate!=null) {
                action="filterDate";
        }
        if (startLocalTime!=null&&endLocalTime!=null){
            action="filterTime";
        }
        if (startLocalDate!=null&&endLocalDate!=null&&startLocalTime!=null&&endLocalTime!=null){
                action="filterDateandTime";
        }

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filterDateandTime":
                log.info("getAllfilterDateandTime");
                request.setAttribute("meals",
                        mealRestController.getAll(startLocalDate,endLocalDate,startLocalTime,endLocalTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "filterDate":
                log.info("getAllfilterDate");
                request.setAttribute("meals",
                        mealRestController.getAll(startLocalDate,endLocalDate));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "filterTime":
                log.info("getAllfilterTime");
                request.setAttribute("meals",
                        mealRestController.getAll(startLocalTime,endLocalTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
