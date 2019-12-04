package es.jorgesanz.salestaxes.service;

import es.jorgesanz.salestaxes.model.OrderLine;
import es.jorgesanz.salestaxes.model.ShoppingCart;
import es.jorgesanz.salestaxes.repository.TaxRepository;
import es.jorgesanz.salestaxes.util.PriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static es.jorgesanz.salestaxes.util.Constants.IMPORTED_GOOD_TAX;
import static java.util.Objects.isNull;

@Service
public class TaxService {

    @Autowired
    private TaxRepository taxRepository;

    public void applyTaxes(ShoppingCart shoppingCart) {
        for(OrderLine orderLine: shoppingCart.getOrderlines()){
            Double baseTax = taxRepository.find(orderLine.getSaleReference());
            if(orderLine.isImported()){
                baseTax += IMPORTED_GOOD_TAX;
            }
            orderLine.setTaxRate(baseTax);
            applyTaxRate(orderLine);
        }
        shoppingCart.setSalesTaxes(shoppingCart.getOrderlines().stream().mapToDouble(OrderLine::getAppliedTaxes).sum());
        shoppingCart.setTotalPrice(shoppingCart.getOrderlines().stream().mapToDouble(OrderLine::getBasePrice).sum()+shoppingCart.getSalesTaxes());
    }

    void applyTaxRate(OrderLine orderLine) {

        if(isNull(orderLine.getBasePrice()) || isNull(orderLine.getTaxRate())){
            throw new IllegalStateException("base price and tax rate must have value");
        }
        orderLine.setAppliedTaxes(PriceUtil.truncate2Decimal(orderLine.getBasePrice()*orderLine.getTaxRate()/100));
    }
}
