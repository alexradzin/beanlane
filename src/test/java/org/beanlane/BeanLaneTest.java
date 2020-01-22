package org.beanlane;

import org.beanlane.extractor.BeanNameExtractor;
import org.beanlane.formatter.ToSnakeCaseFormatter;
import org.junit.jupiter.api.Test;

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
        BeanLane lane = new BeanLane(new BeanNameExtractor(new ToSnakeCaseFormatter()));
        Person p = lane.of(Person.class);
        assertEquals("first_name", lane.name(p::getFirstName));
        assertEquals("last_name", lane.name(p::getLastName));
        assertEquals("home.city", lane.name(() -> p.getHome().getCity()));
    }

    @Test
    void snakeUpperCaseNames() {
        BeanLane lane = new BeanLane(new BeanNameExtractor(new ToSnakeCaseFormatter(true)));
        Person p = lane.of(Person.class);
        assertEquals("FIRST_NAME", lane.name(p::getFirstName));
        assertEquals("LAST_NAME", lane.name(p::getLastName));
        assertEquals("HOME.CITY", lane.name(() -> p.getHome().getCity()));
    }
}