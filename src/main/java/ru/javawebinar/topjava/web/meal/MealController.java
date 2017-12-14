package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.AuthorizedUser.*;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
@RequestMapping("/meals")
public class MealController {
    @Autowired
    private MealService mealService;

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("meals", MealsUtil.getWithExceeded(mealService.getAll(id()), getCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        Objects.requireNonNull(id);
        mealService.delete(id, id());
        model.addAttribute("meals", MealsUtil.getWithExceeded(mealService.getAll(id()), getCaloriesPerDay()));
        return "redirect:/meals";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Integer id, Model model) {
        Objects.requireNonNull(id);
        Meal meal = mealService.get(id, id());
        assureIdConsistent(meal, id);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/mealForm")
    public String doCreateOrUpdate(@ModelAttribute("meal") @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm") Meal meal, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) return "redirect:/mealForm";
        if (meal.isNew()) mealService.create(meal, id());
        else mealService.update(meal, id());
        model.addAttribute("meals", MealsUtil.getWithExceeded(mealService.getAll(id()), getCaloriesPerDay()));
        return "redirect:/meals";
    }

    @PostMapping()
    public String filter(@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                         @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                         @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
                         @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime endTime, Model model) {
        List<Meal> mealsDateFiltered = mealService.getBetweenDates(
                startDate != null ? startDate : DateTimeUtil.MIN_DATE,
                endDate != null ? endDate : DateTimeUtil.MAX_DATE, id());

        model.addAttribute("meals", MealsUtil.getFilteredWithExceeded(mealsDateFiltered,
                startTime != null ? startTime : LocalTime.MIN,
                endTime != null ? endTime : LocalTime.MAX,
                AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }
}
