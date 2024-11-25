package com.example.habithero;

public class ListItem {
    private boolean checked;
    private String title;
    private String description;

    // can make Habit class a member or refactor this class into it

    public ListItem(boolean checked, String title, String description) {
        this.checked = checked;
        this.title = title;
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

