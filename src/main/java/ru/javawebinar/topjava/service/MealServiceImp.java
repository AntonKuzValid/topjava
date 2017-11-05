package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoImpForCollection;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class MealServiceImp implements MealService {
    private MealDao mealDao = new MealDaoImpForCollection();

    @Override
    public void add(Meal meal) {
        mealDao.add(meal);
    }

    @Override
    public void update(Meal meal) {
        mealDao.updatel(meal);
    }

    @Override
    public void remove(int id) {
        mealDao.removel(id);
    }

    @Override
    public Meal getById(int id) {
        return mealDao.getById(id);
    }

    @Override
    public List<Meal> list() {
        return mealDao.list();
    }
}
