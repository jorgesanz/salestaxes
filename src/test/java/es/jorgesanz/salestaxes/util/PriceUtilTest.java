package es.jorgesanz.salestaxes.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PriceUtilTest {

    @Test
    public void roundUp(){

        assertEquals(new Double(0.0d),PriceUtil.roundUp(0.0));
        assertEquals(new Double(5.40d),PriceUtil.roundUp(5.4));
        assertEquals(new Double(1.50d),PriceUtil.roundUp(1.499));
        assertEquals(new Double(0.5d),PriceUtil.roundUp(0.5));
        assertEquals(new Double(7.15d),PriceUtil.roundUp(7.125));
        assertEquals(new Double(0.60d),PriceUtil.roundUp(0.5625));
        assertEquals(new Double(1.90d),PriceUtil.roundUp(1.89899999999999998));
        assertEquals(new Double(4.2d),PriceUtil.roundUp(4.198419999999999999));

    }

    @Test
    public void round2Decimal(){

        assertEquals(new Double(0.0d),PriceUtil.round2Decimal(0.0));
        assertEquals(new Double(1.5d),PriceUtil.round2Decimal(1.5));
        assertEquals(new Double(29.83d),PriceUtil.round2Decimal(29.83000000000000002));
        assertEquals(new Double(7.66d),PriceUtil.round2Decimal(7.659));

    }
}
