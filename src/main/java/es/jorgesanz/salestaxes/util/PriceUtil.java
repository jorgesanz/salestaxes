package es.jorgesanz.salestaxes.util;

import org.apache.commons.math3.util.Precision;

public class PriceUtil {

    public static Double roundUp(double price) {
        return Math.ceil(price * 20) / 20;
    }

    public static Double round2Decimal(Double price){
        return Precision.round(price,2);
    }

}
