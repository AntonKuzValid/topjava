package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoImpForCollection implements MealDao {

    private static AtomicInteger atomicId = new AtomicInteger(0);
    private static List<Meal> mealList = new CopyOnWriteArrayList();

    {
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    @Override
    public void add(Meal meal) {
        meal.setId(atomicId.incrementAndGet());
        mealList.add(meal);
    }

    @Override
    public void updatel(Meal meal) {
        mealList.set(mealList.indexOf(getById(meal.getId())), meal);
    }

    @Override
    public void removel(int id) {
        mealList.remove(getById(id));
    }

    @Override
    public Meal getById(int id) {
        return mealList.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .get();
    }

    @Override
    public List<Meal> list() {
        return mealList;
    }

    public static AtomicInteger getAtomicId() {
        return atomicId;
    }
}
