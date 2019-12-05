package es.jorgesanz.salestaxes.service;

import es.jorgesanz.salestaxes.model.OrderLine;
import es.jorgesanz.salestaxes.model.ShoppingCart;
import es.jorgesanz.salestaxes.repository.TaxRepository;
import es.jorgesanz.salestaxes.util.PriceUtil;
import es.jorgesanz.salestaxes.validator.OrderLineValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static es.jorgesanz.salestaxes.util.Constants.IMPORTED_GOOD_TAX;
import static es.jorgesanz.salestaxes.util.Constants.NO_TAX;

@Service
public class TaxServiceImpl implements TaxService{


    private TaxRepository taxRepository;
    private OrderLineValidator orderLineValidator;

    public TaxServiceImpl(@Autowired TaxRepository taxRepository, @Autowired OrderLineValidator orderLineValidator){
        this.taxRepository = taxRepository;
        this.orderLineValidator = orderLineValidator;

    }

    @Override
    public void applyTaxes(ShoppingCart shoppingCart) {
        processOrderLines(shoppingCart);
        processTotalAmounts(shoppingCart);
    }

    private void processTotalAmounts(ShoppingCart shoppingCart) {
        setSalesTaxes(shoppingCart);
        setTotalPrice(shoppingCart);
    }

    void setTotalPrice(ShoppingCart shoppingCart) {
        Double totalPrice = shoppingCart.getOrderlines().stream().mapToDouble(OrderLine::getTotalPrice).sum();
        shoppingCart.setTotalPrice(PriceUtil.round2Decimal(totalPrice));
    }

    void setSalesTaxes(ShoppingCart shoppingCart) {
        Double salesTaxes = shoppingCart.getOrderlines().stream().mapToDouble(OrderLine::getAppliedTaxes).sum();
        shoppingCart.setSalesTaxes(PriceUtil.round2Decimal(salesTaxes));
    }

    void processOrderLines(ShoppingCart shoppingCart) {
        for(OrderLine orderLine: shoppingCart.getOrderlines()){
            orderLine.setTaxRate(obtainTaxRate(orderLine));
            applyTaxRate(orderLine);
        }
    }

    Double obtainTaxRate(OrderLine orderLine) {
        Double baseTax = taxRepository.find(orderLine.getProductCategory());
        Double importedTax = obtainImportedTax(orderLine);
        return baseTax + importedTax;
    }

    Double obtainImportedTax(OrderLine orderLine) {
        return (orderLine.isImported()?IMPORTED_GOOD_TAX:NO_TAX);
    }

    void applyTaxRate(OrderLine orderLine) {

        orderLineValidator.validate(orderLine);
        Double appliedTaxes = PriceUtil.roundUp(orderLine.getBasePrice()*orderLine.getTaxRate()/100);
        orderLine.setAppliedTaxes(appliedTaxes);
        orderLine.setTotalPrice(PriceUtil.round2Decimal(orderLine.getBasePrice()+appliedTaxes));
    }


}
