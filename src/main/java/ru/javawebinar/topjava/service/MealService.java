package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.Collection;

public interface MealService {

    Meal create(Meal meal);

    void delete(int id);

    Meal get(int id);

    void update(Meal meal);

    Collection<Meal> getAll(LocalDate start, LocalDate end);
}