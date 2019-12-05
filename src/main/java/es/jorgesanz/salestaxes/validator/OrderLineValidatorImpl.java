package es.jorgesanz.salestaxes.validator;

import es.jorgesanz.salestaxes.model.OrderLine;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class OrderLineValidatorImpl implements OrderLineValidator {

    @Override
    public void validate(OrderLine orderLine) {
        validateNullValues(orderLine);
        validateBasePrice(orderLine);
        validateTaxRate(orderLine);
    }

    private void validateTaxRate(OrderLine orderLine) {
        if(orderLine.getTaxRate()<0){
            throw new IllegalStateException("tax rate must be positive");
        }
    }

    private void validateBasePrice(OrderLine orderLine) {
        if(orderLine.getBasePrice()<=0 ){
            throw new IllegalStateException("base price must be greater than 0");
        }
    }

    private void validateNullValues(OrderLine orderLine) {
        if(isNull(orderLine.getBasePrice()) || isNull(orderLine.getTaxRate())){
            throw new IllegalStateException("base price and tax rate must have value");
        }
    }

}
