package org.beanlane;

import javax.xml.bind.annotation.XmlElement;

public class Person implements FullName{

    private String firstName;
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

    @XmlElement(name = "Letters")
    public PostalAddress getCorrespondence() {
        return correspondence;
    }

    @XmlElement(name = "spouse")
    public FullName getSpouse() {
        return spouse;
    }
}
