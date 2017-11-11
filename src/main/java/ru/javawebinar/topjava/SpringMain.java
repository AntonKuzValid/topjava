package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));

            //Test AdminRestController
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email", "password", Role.ROLE_ADMIN));
            System.out.println(adminUserController.get(1));
            adminUserController.update(new User(null, "userName2", "email2", "password2", Role.ROLE_ADMIN), 1);
            adminUserController.getAll().forEach(System.out::println);
            adminUserController.delete(1);
            adminUserController.getAll().forEach(System.out::println);
            adminUserController.create(new User(null, "userName", "email", "password", Role.ROLE_ADMIN));
            System.out.println(adminUserController.getByMail("email"));

            //Test MealRestController
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            System.out.println("TEST MEALREST CONTROLLER______________________________________");
            mealRestController.getAll().forEach(System.out::println);
//            mealRestController.update(new Meal(LocalDateTime.now(),"new meal",120),1);
            mealRestController.update(new Meal(LocalDateTime.now(), "new meal", 120), 3);
//            System.out.println(mealRestController.get(1));
            System.out.println(mealRestController.get(2));
//            mealRestController.delete(1);
            mealRestController.delete(2);
            mealRestController.create(new Meal(LocalDateTime.now(),"new meal", 110));

            mealRestController.getAll().forEach(System.out::println);

            mealRestController.getAll(LocalDate.of(2015, Month.MAY, 30),LocalDate.of(2015, Month.MAY, 30)).forEach(System.out::println);

            mealRestController.getAll(LocalTime.of(10, 0),LocalTime.of(13, 0)).forEach(System.out::println);

            mealRestController.getAll(LocalDate.of(2015, Month.MAY, 30),LocalDate.of(2015, Month.MAY, 30),
                    LocalTime.of(10, 0),LocalTime.of(13, 0)).forEach(System.out::println);
//        }
        }
    }
}
