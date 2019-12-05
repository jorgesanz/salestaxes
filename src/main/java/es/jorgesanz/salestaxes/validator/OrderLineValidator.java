package es.jorgesanz.salestaxes.validator;

import es.jorgesanz.salestaxes.model.OrderLine;

public interface OrderLineValidator {

    public void validate(OrderLine orderLine);
}
