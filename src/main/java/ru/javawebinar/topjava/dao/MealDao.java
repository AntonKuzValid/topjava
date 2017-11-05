package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    public void add(Meal meal);

    public void updatel(Meal meal);

    public void removel(int id);

    public Meal getById(int id);

    public List<Meal> list();
}
