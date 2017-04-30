package com.afrometal.radoslaw.doitlater;

/**
 * Created by radoslaw on 30.04.17.
 */

public class ToDoDetailsItem {
    Long id;
    String title;
    String details;
    Long due;

    public ToDoDetailsItem(Long id, String title, String details, Long due) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.due = due;
    }
}
