package org.beanlane;

public class Address {
    private int zip;
    private String country;
    private String city;
    private String street;
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
