package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() throws Exception {
        Meal meal = service.get(USERS_MEAL_ID,USER_ID);
        Assert.assertEquals(USER_MEAL,meal);
    }

    @Test(expected = NotFoundException.class)
    public void getOthersPersonMeal() throws Exception {
        service.get(USERS_MEAL_ID,ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void delete() throws Exception {
        service.delete(USERS_MEAL_ID,USER_ID);
        Meal meal=service.get(USERS_MEAL_ID,USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteOthersPersonMeal() throws Exception {
        service.delete(USERS_MEAL_ID,ADMIN_ID);
    }


    @Test
    public void getBetweenDateTimes() throws Exception {
        List<Meal> mealList=service.getBetweenDateTimes(
                LocalDateTime.of(2017, Month.NOVEMBER, 18, 10, 0),
                LocalDateTime.of(2017, Month.NOVEMBER, 18, 20, 0),
                USER_ID);
        Assert.assertEquals(USER_FILTERED_MEALS,mealList);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> mealList=service.getAll(USER_ID);
        Assert.assertEquals(USER_MEALS,mealList);
    }

    @Test
    public void update() throws Exception {
        service.update(NEW_MEAL,USER_ID);
        Meal meal=service.get(USERS_MEAL_ID,USER_ID);
        Assert.assertEquals(NEW_MEAL,meal);
    }

    @Test(expected = NotFoundException.class)
    public void updateOthersPersonMeal() throws Exception {
        service.update(NEW_MEAL,ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        service.create(new Meal(LocalDateTime.of(2010,Month.MAY,10,10,00),"new Meal", 100),USER_ID);
        Assert.assertEquals(NEW_USER_MEALS,service.getAll(USER_ID));
    }

}