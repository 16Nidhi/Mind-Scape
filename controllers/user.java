package controllers;

public class user {
    private String name;
    private int age;
    private String email;

    public user(String name, int age, String email) {
        this.name = name;
        this.age  = age;
        this.email = email;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }

    public String getProfile() {
        return "👤 Name: " + name + "\nAge: " + age + "\nEmail: " + email;
    }
}
