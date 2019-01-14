package org.beanlane;

public class Person {
    private String firstName;
    private String lastName;
    private Address home;

    public Person(String firstName, String lastName, Address home) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.home = home;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Address getHome() {
        return home;
    }
}
