package org.beanlane;

import javax.xml.bind.annotation.XmlElement;
import java.util.Map;

public class Address extends PostalAddress {
    @XmlElement(name = "Country")
    private String country;
    @XmlElement(name = "City")
    private String city;
    @XmlElement(name = "Street")
    private String street;
    @XmlElement(name = "StreetNumber")
    private String streetNumber;
    private boolean capital;
    private Map.Entry<String, String> attribute;

    public Address(int zip, String country, String city, String street, String streetNumber, boolean capital) {
        super(zip);
        this.country = country;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.capital = capital;
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

    public boolean isCapital() {
        return capital;
    }

    public Map.Entry<String, String> getAttribute() {
        return attribute;
    }
}
