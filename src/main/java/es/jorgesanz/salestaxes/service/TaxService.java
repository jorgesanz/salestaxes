package es.jorgesanz.salestaxes.service;

import es.jorgesanz.salestaxes.model.ShoppingCart;

public interface TaxService {

    void applyTaxes(ShoppingCart shoppingCart);
}
