package org.beanlane;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class BeanLaneTest {
    private final BeanLane lane = new BeanLane();
    @Test
    void test() {
        Person p = lane.of(Person.class);
        assertEquals("firstName", lane.name(p::getFirstName));
        assertEquals("lastName", lane.name(p::getLastName));
        assertEquals("home.city", lane.name(() -> p.getHome().getCity()));
    }
}