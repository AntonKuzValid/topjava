package ru.javawebinar.topjava.web;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class CustomDateEditorRegistrar implements PropertyEditorRegistrar {
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(LocalTime.class, new CustomDateEditor(new SimpleDateFormat("HH:mm"), false));
        registry.registerCustomEditor(LocalDate.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),false));
        registry.registerCustomEditor(LocalDateTime.class,new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm"), false));
    }
}
