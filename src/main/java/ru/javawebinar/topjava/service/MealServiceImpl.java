package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository){
        this.repository=repository;
    }

    @Override
    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id),id);
    }

    @Override
    public Meal get(int id) {
        return checkNotFoundWithId(repository.get(id),id);
    }

    @Override
    public void update(Meal meal) {
            checkNotFoundWithId(repository.save(meal),meal.getId());
    }

    @Override
    public Collection<Meal> getAll(LocalDate start, LocalDate end) {
        return repository.getAll(start,end);
    }
}