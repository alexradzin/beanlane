package org.beanlane;

import javax.xml.bind.annotation.XmlElement;

public abstract class PostalAddress {
    @XmlElement(name = "ZIP")
    private int zip;

    public PostalAddress(int zip) {
        this.zip = zip;
    }

    public int getZip() {
        return zip;
    }


    public String zipCode() {
        return "" + zip;
    }

    public String getZipCode() {
        return "" + zip;
    }
}
