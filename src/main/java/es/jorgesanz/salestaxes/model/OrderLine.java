package es.jorgesanz.salestaxes.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderLine {

    private String productCategory;
    private Integer quantity;
    private boolean imported;
    private Double basePrice;
    private Double totalPrice;
    private Double taxRate;
    private Double appliedTaxes;

}
