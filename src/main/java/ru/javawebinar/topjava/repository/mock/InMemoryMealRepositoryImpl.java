package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, AuthorizedUser.id()));
        repository.get(1).setUserId(2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        Meal mealInRep=repository.get(meal.getId());
        if (mealInRep!=null&&mealInRep.getUserId()!=userId) return null;
        meal.setUserId(userId);
        return repository.put(meal.getId(), meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return get(id,userId) != null && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        if (meal == null || meal.getUserId() != userId) return null;
        return meal;
    }

    @Override
    public Collection<Meal> getAllWithFilter(int userId) {
       return getAllWithFilter(LocalDate.MIN,LocalDate.MAX,userId);
    }

    @Override
    public Collection<Meal> getAllWithFilter(LocalDate start, LocalDate end, int userId) {
        return repository.values().stream()
                .filter(m -> m.getUserId() == userId)
                .filter(m-> (DateTimeUtil.isBetween(m.getDate(),start,end)))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

