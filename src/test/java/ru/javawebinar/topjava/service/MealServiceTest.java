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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

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
        Meal meal = service.get(USER_MEAL_1.getId(), USER_ID);
        assertMatch(USER_MEAL_1, meal);
    }

    @Test(expected = NotFoundException.class)
    public void getOthersPersonMeal() throws Exception {
        service.get(USER_MEAL_1.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void delete() throws Exception {
        service.delete(USER_MEAL_1.getId(), USER_ID);
        service.get(USER_MEAL_1.getId(), USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteOthersPersonMeal() throws Exception {
        service.delete(USER_MEAL_1.getId(), ADMIN_ID);
    }


    @Test
    public void getBetweenDateTimes() throws Exception {
        List<Meal> mealListActual = service.getBetweenDateTimes(
                LocalDateTime.of(2017, Month.NOVEMBER, 18, 10, 0),
                LocalDateTime.of(2017, Month.NOVEMBER, 18, 20, 0),
                USER_ID);
        List<Meal> mealListExpected = Arrays.asList(
                USER_MEAL_1,
                USER_MEAL_2,
                USER_MEAL_3
        );
        assertMatch(mealListActual, mealListExpected);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> mealListActual = service.getAll(USER_ID);
        List<Meal> mealListExpected = Arrays.asList(
                USER_MEAL_1,
                USER_MEAL_2,
                USER_MEAL_3,
                USER_MEAL_4,
                USER_MEAL_5,
                USER_MEAL_6
        );
        assertMatch(mealListActual, mealListExpected);
    }

    @Test
    public void update() throws Exception {
        Meal newMeal = new Meal(USER_MEAL_1);
        newMeal.setDescription("new Meal");
        newMeal.setCalories(100);
        service.update(newMeal, USER_ID);
        Meal meal = service.get(USER_MEAL_1.getId(), USER_ID);
        assertMatch(newMeal, meal);
    }

    @Test(expected = NotFoundException.class)
    public void updateOthersPersonMeal() throws Exception {
        service.update(USER_MEAL_1, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal meal = new Meal(LocalDateTime.of(2010, Month.MAY, 10, 10, 00), "new Meal", 100);
        Meal newMeal = service.create(meal, USER_ID);
        assertMatch(newMeal, service.get(newMeal.getId(), USER_ID));
    }

}