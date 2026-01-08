package com.example.classroom;

public class User {
    public String userId;
    public String firstName;
    public String lastName;
    public String email;
    public String password;

    public User() {
        // Required empty constructor
    }

    public User(String userId, String firstName, String lastName,
                String email, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
