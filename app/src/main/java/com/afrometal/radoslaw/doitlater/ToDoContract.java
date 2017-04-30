package com.afrometal.radoslaw.doitlater;

import android.provider.BaseColumns;

/**
 * Created by radoslaw on 30.04.17.
 */

public final class ToDoContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ToDoContract() {}

    /* Inner class that defines the table contents */
    public static class ToDoEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DETAILS = "details";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_DUE = "due";
    }
}
