package es.jorgesanz.salestaxes.util;

import org.apache.commons.math3.util.Precision;

import java.text.DecimalFormat;

public class PriceUtil {

    private static final DecimalFormat df = new DecimalFormat("####0.00");

    public static Double roundUp(double price) {
        return Math.ceil(price * 20) / 20;
    }

    public static Double round2Decimal(Double price){
        return Precision.round(price,2);
    }

    public static String roundAndFormat2Decimal(Double price){
        return df.format(round2Decimal(price)).replace(",",".");
    }

}
