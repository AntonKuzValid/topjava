package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("mealServiceImpl")
    private MealService service;


    public Meal create(Meal meal){
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal);
    }

    public void delete(int id){
        log.info("delete {}", id);
        service.delete(id);
    }

    public Meal get(int id){
        log.info("get {}", id);
        return service.get(id);
    }

    public void update(Meal meal, int id){
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal,id);
        service.update(meal);
    }

    public List<MealWithExceed> getAll(){
        log.info("getAll");
        return MealsUtil.getWithExceeded(new ArrayList<Meal>(service.getAll(LocalDate.MIN,LocalDate.MAX)), AuthorizedUser.getCaloriesPerDay());
    }

    public List<MealWithExceed> getAll(LocalDate start, LocalDate end){
        log.info("getAllwithFilter");
        return MealsUtil.getWithExceeded(new ArrayList<Meal>(service.getAll(start, end)), AuthorizedUser.getCaloriesPerDay());
    }

    public List<MealWithExceed> getAll(LocalTime start, LocalTime end){
        log.info("getAllwithFilter");
        return MealsUtil.getFilteredWithExceeded(new ArrayList<Meal>(service.getAll(LocalDate.MIN,LocalDate.MAX)),start,end,AuthorizedUser.getCaloriesPerDay());
    }

    public List<MealWithExceed> getAll(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime){
        log.info("getAllwithFilter");
        return MealsUtil.getFilteredWithExceeded(new ArrayList<Meal>(service.getAll(startDate, endDate)),startTime,endTime, AuthorizedUser.getCaloriesPerDay());
    }

}