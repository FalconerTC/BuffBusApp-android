package com.cherish.bustracker.lib;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Falcon on 7/3/2016.
 */
public class PolylinesTest {

    @Test
    public void getValidRoute() throws Exception {
        String valid = "emesFdyjaS@vB`AtF^hDfJIRDDdLNzCAvKEbC}AvLS~Eo@tMA`ZGzLTd@LwO?_MBmKLoBj@yOh@_DdAoHFsUIkc@e@}FsCZiB|@eAr@aAbAg@bA_@~@_@rC@N";
        int  route = 11;
        String line_data = Polylines.POLYLINE_MAP.get(route);
        assertThat(valid, CoreMatchers.is(equalTo(line_data)));
    }
}
