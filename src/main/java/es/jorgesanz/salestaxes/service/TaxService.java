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
        Double salesTaxes = shoppingCart.getOrderlines().stream().mapToDouble(OrderLine::getAppliedTaxes).sum();
        shoppingCart.setSalesTaxes(PriceUtil.round2Decimal(salesTaxes));


        Double totalPrice = shoppingCart.getOrderlines().stream().mapToDouble(OrderLine::getBasePrice).sum()+shoppingCart.getSalesTaxes();
        shoppingCart.setTotalPrice(PriceUtil.round2Decimal(totalPrice));
    }

    void applyTaxRate(OrderLine orderLine) {

        if(isNull(orderLine.getBasePrice()) || isNull(orderLine.getTaxRate())){
            throw new IllegalStateException("base price and tax rate must have value");
        }
        orderLine.setAppliedTaxes(PriceUtil.roundUp(orderLine.getBasePrice()*orderLine.getTaxRate()/100));
    }
}
