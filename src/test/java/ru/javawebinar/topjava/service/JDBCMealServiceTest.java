package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(profiles = Profiles.JDBC)
public class JDBCMealServiceTest extends MealServiceTest {
}
