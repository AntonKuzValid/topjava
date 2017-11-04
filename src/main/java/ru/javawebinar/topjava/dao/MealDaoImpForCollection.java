package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MealDaoImpForCollection implements MealDao {

    private static List<Meal> mealList = new CopyOnWriteArrayList();

    static {
        mealList.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        mealList.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        mealList.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        mealList.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        mealList.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        mealList.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    @Override
    public void addMeal(Meal meal) {
        mealList.add(meal);
    }

    @Override
    public void updateMeal(Meal meal) {
        mealList.set(mealList.indexOf(getMealById(meal.getId())), meal);
    }

    @Override
    public void removeMeal(int id) {
        mealList.remove(getMealById(id));
    }

    @Override
    public Meal getMealById(int id) {
        return mealList.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .get();
    }

    @Override
    public List<Meal> listMeals() {
        return mealList;
    }
}
