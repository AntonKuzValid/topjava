package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Controller
@RequestMapping("/meals")
public class MealController extends AbstractMealController {

    public MealController(MealService service) {
        super(service);
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        delete(id);
        model.addAttribute("meals", getAll());
        return "redirect:/meals";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Integer id, Model model) {
        Objects.requireNonNull(id);
        model.addAttribute("meal", get(id));
        return "mealForm";
    }

    @PostMapping("/mealForm")
    public String doCreateOrUpdate(@ModelAttribute("meal") @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm") Meal meal, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) return "redirect:/mealForm";
        if (meal.isNew()) create(meal);
        else update(meal, meal.getId());
        model.addAttribute("meals", getAll());
        return "redirect:/meals";
    }

    @PostMapping()
    public String filter(@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                         @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                         @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
                         @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime endTime, Model model) {
        model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}
