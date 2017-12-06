package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.Comparator;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJPAUserServiceTest extends UserServiceTest {

    @Test
    public void getUserWithMeals(){
        User actualUser = service.getWithMeals(USER_ID);
        List<Meal> mealList=actualUser.getMeals();
        mealList.sort(Comparator.comparing(Meal::getDateTime).reversed());
        assertMatch(actualUser,USER);
        MealTestData.assertMatch(mealList,MEALS);
    }
}
