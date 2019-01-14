package org.beanlane;

import org.beanlane.NameExtractor.SnakeNameExtractor;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

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
        BeanLane lane = new BeanLane(new SnakeNameExtractor());
        Person p = lane.of(Person.class);
        assertEquals("first_name", lane.name(p::getFirstName));
        assertEquals("last_name", lane.name(p::getLastName));
        assertEquals("home.city", lane.name(() -> p.getHome().getCity()));
    }
}