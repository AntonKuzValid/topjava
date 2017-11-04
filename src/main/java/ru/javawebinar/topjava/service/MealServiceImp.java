package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoImpForCollection;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class MealServiceImp implements MealService {
    private MealDao mealDao = new MealDaoImpForCollection();

    @Override
    public void addMeal(Meal meal) {
        mealDao.addMeal(meal);
    }

    @Override
    public void updateMeal(Meal meal) {
        mealDao.updateMeal(meal);
    }

    @Override
    public void removeMeal(int id) {
        mealDao.removeMeal(id);
    }

    @Override
    public Meal getMealById(int id) {
        return mealDao.getMealById(id);
    }

    @Override
    public List<Meal> listMeals() {
        return mealDao.listMeals();
    }
}
