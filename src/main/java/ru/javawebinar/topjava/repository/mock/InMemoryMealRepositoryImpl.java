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
        MealsUtil.MEALS.forEach(this::save);
        repository.get(1).setUserId(2);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        Meal mealInRep=repository.get(meal.getId());
        if (mealInRep!=null&&mealInRep.getUserId()!=AuthorizedUser.id()) return null;
        repository.put(meal.getId(), meal);
        meal.setUserId(AuthorizedUser.id());
        return meal;
    }

    @Override
    public boolean delete(int id) {
        Meal meal = repository.get(id);
        if (meal == null || meal.getUserId() != AuthorizedUser.id()) return false;
        repository.remove(id);
        return true;
    }

    @Override
    public Meal get(int id) {
        Meal meal = repository.get(id);
        if (meal == null || meal.getUserId() != AuthorizedUser.id()) return null;
        return meal;
    }

    @Override
    public Collection<Meal> getAll(LocalDate start, LocalDate end) {
        return repository.values().stream()
                .filter(m -> m.getUserId() == AuthorizedUser.id())
                .filter(m-> (DateTimeUtil.isBetween(m.getDate(),start,end)))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

