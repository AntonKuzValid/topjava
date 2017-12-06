package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJPAMealServiceTest extends MealServiceTest {

    @Test
    @Transactional
    public void getMealWithUser() {
        Meal actualMeal = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        User actualUser = actualMeal.getUser();
        assertMatch(actualMeal, ADMIN_MEAL1);
        UserTestData.assertMatch(actualUser, ADMIN);
    }
}
