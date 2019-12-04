package es.jorgesanz.salestaxes.service;

import es.jorgesanz.salestaxes.model.OrderLine;
import es.jorgesanz.salestaxes.model.ShoppingCart;
import es.jorgesanz.salestaxes.repository.TaxRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static es.jorgesanz.salestaxes.util.Constants.*;
import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TaxService.class, TaxRepository.class})
public class TaxServiceTest {

    @Autowired
    private TaxService taxService;

    @Test
    public void input1Ok(){

        OrderLine book = OrderLine.builder().basePrice(12.49).saleReference(BOOK).quantity(1).build();
        OrderLine musicCd = OrderLine.builder().basePrice(14.99).saleReference(MUSIC_CD).quantity(1).build();
        OrderLine chocolateBar = OrderLine.builder().basePrice(0.85).saleReference(CHOCOLATE_BAR).quantity(1).build();

        List<OrderLine> orderLines = new ArrayList<>(Arrays.asList(book,musicCd,chocolateBar));
        ShoppingCart shoppingCart = ShoppingCart.builder().orderlines(orderLines).build();

        taxService.applyTaxes(shoppingCart);

        assertEquals(new Double(1.50d), shoppingCart.getSalesTaxes());
        assertEquals(new Double(29.83d), shoppingCart.getTotalPrice());
    }

    @Test
    public void input2Ok(){

        OrderLine importedBoxOfChocolates = OrderLine.builder().basePrice(10.00).saleReference(BOX_OF_CHOCOLATES).imported(true).quantity(1).build();
        OrderLine importedBottleOfPerfume = OrderLine.builder().basePrice(47.50).saleReference(BOTTLE_OF_PERFUME).imported(true).quantity(1).build();

        List<OrderLine> orderLines = new ArrayList<>(Arrays.asList(importedBoxOfChocolates,importedBottleOfPerfume));
        ShoppingCart shoppingCart = ShoppingCart.builder().orderlines(orderLines).build();

        taxService.applyTaxes(shoppingCart);

        assertEquals(new Double(7.65d), shoppingCart.getSalesTaxes());
        assertEquals(new Double(65.15d), shoppingCart.getTotalPrice());
    }

    @Test
    public void input3Ok(){

        OrderLine importedBottleOfPerfume = OrderLine.builder().basePrice(27.99).saleReference(BOTTLE_OF_PERFUME).imported(true).quantity(1).build();
        OrderLine bottleOfPerfume = OrderLine.builder().basePrice(18.99).saleReference(BOTTLE_OF_PERFUME).quantity(1).build();
        OrderLine packetOfheadachePills = OrderLine.builder().basePrice(9.75).saleReference(PACKET_OF_HEADACHE_PILLS).quantity(1).build();
        OrderLine importedBoxOfChocolates = OrderLine.builder().basePrice(11.25).saleReference(BOX_OF_CHOCOLATES).imported(true).quantity(1).build();

        List<OrderLine> orderLines = new ArrayList<>(Arrays.asList(importedBoxOfChocolates, bottleOfPerfume, packetOfheadachePills, importedBottleOfPerfume));
        ShoppingCart shoppingCart = ShoppingCart.builder().orderlines(orderLines).build();

        taxService.applyTaxes(shoppingCart);

        assertEquals(new Double(6.70d), shoppingCart.getSalesTaxes());
        assertEquals(new Double(74.68d), shoppingCart.getTotalPrice());
    }


    @Test(expected = IllegalStateException.class)
    public void applyTaxRateKoNoTax(){

        OrderLine orderLine = OrderLine.builder().basePrice(1.45d).saleReference(BOX_OF_CHOCOLATES).quantity(1).build();
        taxService.applyTaxRate(orderLine);
    }

    @Test(expected = IllegalStateException.class)
    public void applyTaxRateKoNoBasePrice(){

        OrderLine orderLine = OrderLine.builder().saleReference(BOX_OF_CHOCOLATES).taxRate(BASIC_TAX).quantity(1).build();
        taxService.applyTaxRate(orderLine);
    }


}
