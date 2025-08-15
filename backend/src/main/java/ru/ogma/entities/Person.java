package ru.ogma.entities;

import java.util.Objects;

public class Person {

    private Integer id;
    private String username, email;

    public Person(String username, String email) {
        this.id = null;
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(username, person.username) && Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}
