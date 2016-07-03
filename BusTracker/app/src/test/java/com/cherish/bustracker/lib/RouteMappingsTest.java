package com.cherish.bustracker.lib;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;

/**
 * Created by Falcon on 7/3/2016.
 */
public class RouteMappingsTest {

    /* ROUTE_ORDER */
    @Test
    public void Order_getValidRoute() throws Exception {
        String route = "Buff Bus";
        int index = RouteMappings.ROUTE_ORDER.get(route, -1);
        assertThat(index, is(both(greaterThanOrEqualTo(0))
                .and(lessThanOrEqualTo(RouteMappings.MAX_ROUTES))));
    }
    @Test
    public void Order_getInvalidRoute() throws Exception {
        String route = "Fake route";
        int index = RouteMappings.ROUTE_ORDER.get(route, -1);
        assertThat(index, not(both(greaterThanOrEqualTo(0))
                .and(lessThanOrEqualTo(RouteMappings.MAX_ROUTES))));
    }
    @Test
    public void Order_getCaseIncorrectRoute() throws Exception {
        String route = "buFf BUs";
        int index = RouteMappings.ROUTE_ORDER.get(route, -1);
        assertThat(index, is(both(greaterThanOrEqualTo(0))
                .and(lessThanOrEqualTo(RouteMappings.MAX_ROUTES))));
    }
    @Test
    public void Order_getDefaultRoute() throws Exception {
        int defaultResponse = -1;
        String route = "Fake route";
        int index = RouteMappings.ROUTE_ORDER.get(route, defaultResponse);
        assertThat(index, not(both(greaterThanOrEqualTo(0))
                .and(lessThanOrEqualTo(RouteMappings.MAX_ROUTES))));

    }
    @Test
    public void Order_getValidDefaultRoute() throws Exception {
        int defaultResponse = -1;
        String route = "Buff Bus";
        int index = RouteMappings.ROUTE_ORDER.get(route, defaultResponse);
        assertThat(index, is(both(greaterThanOrEqualTo(0))
                .and(lessThanOrEqualTo(RouteMappings.MAX_ROUTES))));

    }


    /* ROUTE_POLYLINE */
    @Test
    public void Polyline_getValidRoute() throws Exception {
        String route = "Buff Bus";
        String lineData = RouteMappings.ROUTE_POLYLINE.get(route, null);
        assertThat(lineData, not(equalTo(null)));
    }
    @Test
    public void Polyline_getInvalidRoute() throws Exception {
        String route = "Fake route";
        String lineData = RouteMappings.ROUTE_POLYLINE.get(route, null);
        assertThat(lineData, is(equalTo(null)));
    }
    @Test
    public void Polyline_getCaseIncorrectRoute() throws Exception {
        String route = "buFf BUs";
        String lineData = RouteMappings.ROUTE_POLYLINE.get(route, null);
        assertThat(lineData, not(equalTo(null)));
    }
    @Test
    public void Polyline_getDefaultRoute() throws Exception {
        String defaultResponse = "route not found";
        String route = "Fake route";
        String lineData = RouteMappings.ROUTE_POLYLINE.get(route, defaultResponse);
        assertThat(lineData, is(equalTo(defaultResponse)));
    }
    @Test
    public void Polyline_getValidDefaultRoute() throws Exception {
        String defaultResponse = "route not found";
        String route = "Buff Bus";
        String lineData = RouteMappings.ROUTE_POLYLINE.get(route, defaultResponse);
        assertThat(lineData, not(equalTo(defaultResponse)));
    }
}
