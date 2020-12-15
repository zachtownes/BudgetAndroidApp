package com.mysticturtles.a21gbudget;

public class Users {
    private int userID;
    private String name;

    public Users(int userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    public int getUserID() { return userID; }

    public void setUserID(int userID) { this.userID = userID; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name;}

    //

    @Override
    public String toString() { return name;}

    @Override
    public boolean equals (Object obj) {
        if(obj instanceof Users){
            Users c = (Users )obj;
            if(c.getName().equals(name) && c.getUserID()==userID) return true;
        }
        return false;
    }
}
