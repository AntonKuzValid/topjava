package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000)
                .forEach(System.out::println);
        System.out.println();
        getFilteredWithExceededForEach(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000)
                .forEach(System.out::println);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateWithColoriesMap = mealList
                .stream()
                .collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories)));

        return mealList
                .stream()
                .filter((s) -> TimeUtil.isBetween(s.getDateTime().toLocalTime(), startTime, endTime))
                .map((s) -> createUserMealWithExceed(s, dateWithColoriesMap.get(s.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getFilteredWithExceededForEach(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateWithCaloriesMap = new HashMap<>();

        mealList.forEach(v -> dateWithCaloriesMap.merge(v.getDate(), v.getCalories(), (k, v2) -> v2 = v.getCalories() + dateWithCaloriesMap.getOrDefault(v.getDate(), 0)));

        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();

        mealList.forEach(u -> {
            if (TimeUtil.isBetween(u.getTime(), startTime, endTime)) {
                userMealWithExceedList.add(createUserMealWithExceed(u, dateWithCaloriesMap.get(u.getDate()) > caloriesPerDay));
            }
        });
        return userMealWithExceedList;
    }

    private static UserMealWithExceed createUserMealWithExceed(UserMeal userMeal, boolean isExceed) {
        return new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), isExceed);
    }
}
