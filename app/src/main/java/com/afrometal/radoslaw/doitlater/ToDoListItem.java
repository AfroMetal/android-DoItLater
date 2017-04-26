package com.afrometal.radoslaw.doitlater;

/**
 * Created by radoslaw on 26.04.17.
 */

public class ToDoListItem {
    Long id;
    String title;
    String date;

    public ToDoListItem(Long id, String title, String date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }
}
