package org.beanlane;

import javax.xml.bind.annotation.XmlElement;

public interface FullName {
    @XmlElement(name = "FirstName")
    String getFirstName();
    @XmlElement(name = "LastName")
    String getLastName();
}
