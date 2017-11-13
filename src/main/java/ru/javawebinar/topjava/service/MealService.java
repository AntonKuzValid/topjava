package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.Collection;

public interface MealService {

    Meal create(Meal meal, int userId);

    void delete(int id, int userId);

    Meal get(int id, int userId);

    void update(Meal meal, int userId);

    Collection<Meal> getAllWithFilter(int userId);

    Collection<Meal> getAllWithFilter(LocalDate start, LocalDate end, int userId);
}