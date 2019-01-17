package org.beanlane;

import javax.xml.bind.annotation.XmlElement;

public class Person {

    private String firstName;
    @XmlElement(name = "LastName")
    private String lastName;
    private Address home;

    public Person(String firstName, String lastName, Address home) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.home = home;
    }

    @XmlElement(name = "FirstName")
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @XmlElement(name = "HomeAddress")
    public Address getHome() {
        return home;
    }
}
