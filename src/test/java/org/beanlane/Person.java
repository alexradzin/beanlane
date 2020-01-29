package org.beanlane;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;

public class Person implements FullName{

    private String firstName;
    @JsonProperty("last-name")
    @XmlElement(name = "LastName")
    private String lastName;
    private Address home;
    private PostalAddress correspondence;
    private FullName spouse;

    public Person(String firstName, String lastName, Address home) {
        this(firstName, lastName, home, home);
    }

    public Person(String firstName, String lastName, Address home, PostalAddress correspondence) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.home = home;
        this.correspondence = correspondence;
    }

    @JsonProperty("first-name")
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

    @JsonIgnore
    @XmlElement(name = "Letters")
    public PostalAddress getCorrespondence() {
        return correspondence;
    }

    @XmlElement(name = "spouse")
    public FullName getSpouse() {
        return spouse;
    }
}
