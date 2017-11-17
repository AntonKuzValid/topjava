package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int USERS_MEAL_ID =START_SEQ+2;

    public static final List<Meal> USER_MEALS = Arrays.asList(
            new Meal(START_SEQ+2,LocalDateTime.of(2017, Month.NOVEMBER, 18, 20, 0), "Ужин", 1000),
            new Meal(START_SEQ+3,LocalDateTime.of(2017, Month.NOVEMBER, 18, 13, 0), "Обед", 1000),
            new Meal(START_SEQ+4,LocalDateTime.of(2017, Month.NOVEMBER, 18, 10, 0), "Завтрак", 1000),
            new Meal(START_SEQ+5,LocalDateTime.of(2017, Month.NOVEMBER, 17, 20, 0), "Ужин", 400),
            new Meal(START_SEQ+6,LocalDateTime.of(2017, Month.NOVEMBER, 17, 13, 0), "Обед", 500),
            new Meal(START_SEQ+7,LocalDateTime.of(2017, Month.NOVEMBER, 17, 10, 0), "Завтрак", 1000)
    );

    public static final List<Meal> USER_FILTERED_MEALS = Arrays.asList(
            new Meal(START_SEQ+2,LocalDateTime.of(2017, Month.NOVEMBER, 18, 20, 0), "Ужин", 1000),
            new Meal(START_SEQ+3,LocalDateTime.of(2017, Month.NOVEMBER, 18, 13, 0), "Обед", 1000),
            new Meal(START_SEQ+4,LocalDateTime.of(2017, Month.NOVEMBER, 18, 10, 0), "Завтрак", 1000)
    );

    public static final List<Meal> NEW_USER_MEALS = Arrays.asList(
            new Meal(START_SEQ+2,LocalDateTime.of(2017, Month.NOVEMBER, 18, 20, 0), "Ужин", 1000),
            new Meal(START_SEQ+3,LocalDateTime.of(2017, Month.NOVEMBER, 18, 13, 0), "Обед", 1000),
            new Meal(START_SEQ+4,LocalDateTime.of(2017, Month.NOVEMBER, 18, 10, 0), "Завтрак", 1000),
            new Meal(START_SEQ+5,LocalDateTime.of(2017, Month.NOVEMBER, 17, 20, 0), "Ужин", 400),
            new Meal(START_SEQ+6,LocalDateTime.of(2017, Month.NOVEMBER, 17, 13, 0), "Обед", 500),
            new Meal(START_SEQ+7,LocalDateTime.of(2017, Month.NOVEMBER, 17, 10, 0), "Завтрак", 1000),
            new Meal(START_SEQ+10,LocalDateTime.of(2010,Month.MAY,10,10,00),"new Meal", 100)
    );

    public static final Meal USER_MEAL=USER_MEALS.get(0);

    public static final List<Meal> ADMIN_MEALS =Arrays.asList(
            new Meal(LocalDateTime.of(2017, Month.NOVEMBER, 17, 13, 0), "Полдник", 1000),
            new Meal(LocalDateTime.of(2017, Month.NOVEMBER, 17, 10, 0), "Завтрак", 1000)
    );

    public static final Meal NEW_MEAL =new Meal(START_SEQ+2,LocalDateTime.of(2010,Month.MAY,10,10,00),"new Meal", 100);
}
