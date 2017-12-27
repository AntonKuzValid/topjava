package ru.javawebinar.topjava.web.formatters;

import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.text.SimpleDateFormat;

public class MealsDateEditor extends CustomDateEditor {

    public MealsDateEditor() {
        super(new SimpleDateFormat("yyyy-MM-dd"), true);
    }
}
