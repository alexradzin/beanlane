package org.beanlane;

import org.beanlane.extractor.PropertyNameAnnotationExtractor;
import org.beanlane.extractor.PropertyNameExtractor;
import org.beanlane.formatter.GetterFormatter;
import org.beanlane.formatter.ToSnakeCaseFormatter;
import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlElement;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@VisibleForPackage
class BeanLaneTest {

    @Test
    void beanNames() {
        BeanLane lane = new BeanLane();
        Person p = lane.of(Person.class);
        assertEquals("firstName", lane.name(p::getFirstName));
        assertEquals("lastName", lane.name(p::getLastName));
        assertEquals("home.city", lane.name(() -> p.getHome().getCity()));
    }

    @Test
    void snakeNames() {
        BeanLane lane = new BeanLane(new PropertyNameExtractor(new ChainedFunction<>(Arrays.asList(new GetterFormatter(), new ToSnakeCaseFormatter()))));
        Person p = lane.of(Person.class);
        assertEquals("first_name", lane.name(p::getFirstName));
        assertEquals("last_name", lane.name(p::getLastName));
        assertEquals("home.city", lane.name(() -> p.getHome().getCity()));
    }

    @Test
    void snakeUpperCaseNames() {
        BeanLane lane = new BeanLane(new PropertyNameExtractor(new ChainedFunction<>(Arrays.asList(new GetterFormatter(), new ToSnakeCaseFormatter(true)))));
        Person p = lane.of(Person.class);
        assertEquals("FIRST_NAME", lane.name(p::getFirstName));
        assertEquals("LAST_NAME", lane.name(p::getLastName));
        assertEquals("HOME.CITY", lane.name(() -> p.getHome().getCity()));
    }

    @Test
    void urlNames() {
        BeanLane lane = new BeanLane("/", new PropertyNameExtractor(new ChainedFunction<>(Arrays.asList(new GetterFormatter(), new ToSnakeCaseFormatter("-")))));
        Person p = lane.of(Person.class);
        assertEquals("first-name", lane.name(p::getFirstName));
        assertEquals("last-name", lane.name(p::getLastName));
        assertEquals("home/city", lane.name(() -> p.getHome().getCity()));
    }


    @Test
    void namesInAnnotations() {
        BeanLane lane = new BeanLane("/", new PropertyNameAnnotationExtractor(XmlElement.class, "name"));
        Person p = lane.of(Person.class);
        assertEquals("FirstName", lane.name(p::getFirstName));
        assertEquals("LastName", lane.name(p::getLastName));
        assertEquals("HomeAddress/City", lane.name(() -> p.getHome().getCity()));
    }

}