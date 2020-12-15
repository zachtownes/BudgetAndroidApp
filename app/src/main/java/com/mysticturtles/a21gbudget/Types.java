package com.mysticturtles.a21gbudget;

public class Types {

    private int typeID;
    private String typeName;

    public Types(int typeID, String typeName) {
        this.typeID = typeID;
        this.typeName = typeName;
    }


    public int getId() {
        return typeID;
    }

    public void setId(int typeID) {
        this.typeID = typeID;
    }

    public String getName() {
        return typeName;
    }

    public void setName(String typeName) {
        this.typeName = typeName;
    }


    //to display object as a string in spinner
    @Override
    public String toString() {
        return typeName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Types){
            Types c = (Types )obj;
            if(c.getName().equals(typeName) && c.getId()==typeID ) return true;
        }

        return false;
    }

}
