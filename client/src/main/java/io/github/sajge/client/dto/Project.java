package io.github.sajge.client.dto;

public class Project {
    private int id;
    private String name;
    private String data;

    public int getId() {
        return id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getData() {
        return data;
    }

    public void setData(String d) {
        this.data = d;
    }
}
