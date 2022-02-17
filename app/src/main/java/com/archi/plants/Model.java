package com.archi.plants;

public class Model{
    private int id;
    private byte[]image;
    private String name;
    private String type;
    private int wat;

    //constructor



    public Model(int id, String name, String type,    int wat ,byte[] image) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.type = type;
        this.wat = wat;
    }
    //getter and setter method

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getUName() {
        return name;
    }

    public void setUName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String name) {
        this.type = type;
    }

    public int getWat() {
        return wat;
    }

    public void setWat(int wat) {
        this.wat = wat;
    }

}