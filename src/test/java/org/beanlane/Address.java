package org.beanlane;

import javax.xml.bind.annotation.XmlElement;

public class Address {
    @XmlElement(name = "ZIP")
    private int zip;
    @XmlElement(name = "Country")
    private String country;
    @XmlElement(name = "City")
    private String city;
    @XmlElement(name = "Street")
    private String street;
    @XmlElement(name = "StreetNumber")
    private String streetNumber;

    public Address(int zip, String country, String city, String street, String streetNumber) {
        this.zip = zip;
        this.country = country;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
    }

    public int getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }
}
