package com.maverick.tcs.innovations.talentengagment.beans;

/**
 * Created by abhi on 3/1/2016.
 */
public class MenuBean {
    int id;
    private String menuItem;
    private String menuBegin;

    public MenuBean() {
    }

    public MenuBean(int id, String menuItem) {
        this.menuItem = menuItem;
        this.id=id;
    }

    public String getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(String menuItem) {
        this.menuItem = menuItem;
    }

    public String getMenuBegin() {
        return menuBegin;
    }

    public void setMenuBegin(String menuBegin) {
        this.menuBegin = menuBegin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
