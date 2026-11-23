package model;

public class Artist {

    private int id;
    private final String name;


    public Artist(String name) {
        this.name = name;
        this.id = 0;
    }

    public Artist(String name, int id) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
}
