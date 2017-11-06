package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MealDaoImpForCollection implements MealDao {

    public static void main(String[] arg){

    }

    private static AtomicInteger atomicId = new AtomicInteger(0);
    private static Map<Integer,Meal> mealList = new ConcurrentHashMap<>();

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
        mealList.put(meal.getId(),meal);
    }

    @Override
    public void update(Meal meal) {
        mealList.put(meal.getId(),meal);
    }

    @Override
    public void remove(int id) {
        mealList.remove(id);
    }

    @Override
    public List<Meal> list() {
        return new ArrayList<>(mealList.values());
    }

    @Override
    public Meal getById(int id){
            return mealList.get(id);
    }

    public static AtomicInteger getAtomicId() {
        return atomicId;
    }
}
