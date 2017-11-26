package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;


    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.getUser().setId(userId);
            em.persist(meal);
            return meal;
        } else {
            return meal.getUser().getId()==userId? em.merge(meal):null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        User user= em.getReference(Meal.class,id).getUser();
        return user.getId() == userId && em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal=em.getReference(Meal.class, id);
        User user=em.getReference(User.class, userId);
        if (meal.getUser().getId().equals(user.getId()) ) return em.find(Meal.class,id);
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        User user=em.getReference(User.class,userId);
        return em.createNamedQuery(Meal.ALL_SORTED)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        User user=em.getReference(User.class,userId);
        return em.createNamedQuery(Meal.BETWEEN_SORTED)
                .setParameter("user", user)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}