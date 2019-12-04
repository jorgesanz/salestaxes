package es.jorgesanz.salestaxes.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShoppingCart {

    private List<OrderLine> orderlines;
    private Double salesTaxes;
    private Double totalPrice;
}
