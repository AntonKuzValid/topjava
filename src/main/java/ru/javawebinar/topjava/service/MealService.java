package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealService {
    void add(Meal meal);

    void update(Meal meal);

    void remove(int id);

    Meal getById(int id);

    List<Meal> list();
}
