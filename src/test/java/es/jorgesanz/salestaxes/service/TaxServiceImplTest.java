package es.jorgesanz.salestaxes.service;

import es.jorgesanz.salestaxes.model.OrderLine;
import es.jorgesanz.salestaxes.model.ShoppingCart;
import es.jorgesanz.salestaxes.repository.TaxRepositoryImpl;
import es.jorgesanz.salestaxes.validator.OrderLineValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static es.jorgesanz.salestaxes.util.Constants.*;
import static java.util.Objects.isNull;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TaxServiceImpl.class, TaxRepositoryImpl.class})
public class TaxServiceImplTest {

    @Autowired
    private TaxServiceImpl taxService;

    @MockBean
    private OrderLineValidator orderLineValidator;

    @Test
    public void input1Ok(){

        OrderLine book = OrderLine.builder().basePrice(12.49).productCategory(BOOK).quantity(1).build();
        OrderLine musicCd = OrderLine.builder().basePrice(14.99).productCategory(MUSIC_CD).quantity(1).build();
        OrderLine chocolateBar = OrderLine.builder().basePrice(0.85).productCategory(CHOCOLATE_BAR).quantity(1).build();

        List<OrderLine> orderLines = new ArrayList<>(Arrays.asList(book,musicCd,chocolateBar));
        ShoppingCart shoppingCart = ShoppingCart.builder().orderlines(orderLines).build();

        taxService.applyTaxes(shoppingCart);

        assertEquals(new Double(1.50d), shoppingCart.getSalesTaxes());
        assertEquals(new Double(29.83d), shoppingCart.getTotalPrice());
    }

    @Test
    public void input2Ok(){

        OrderLine importedBoxOfChocolates = OrderLine.builder().basePrice(10.00).productCategory(BOX_OF_CHOCOLATES).imported(true).quantity(1).build();
        OrderLine importedBottleOfPerfume = OrderLine.builder().basePrice(47.50).productCategory(BOTTLE_OF_PERFUME).imported(true).quantity(1).build();

        List<OrderLine> orderLines = new ArrayList<>(Arrays.asList(importedBoxOfChocolates,importedBottleOfPerfume));
        ShoppingCart shoppingCart = ShoppingCart.builder().orderlines(orderLines).build();

        taxService.applyTaxes(shoppingCart);

        assertEquals(new Double(7.65d), shoppingCart.getSalesTaxes());
        assertEquals(new Double(65.15d), shoppingCart.getTotalPrice());
    }

    @Test
    public void input3Ok(){

        OrderLine importedBottleOfPerfume = OrderLine.builder().basePrice(27.99).productCategory(BOTTLE_OF_PERFUME).imported(true).quantity(1).build();
        OrderLine bottleOfPerfume = OrderLine.builder().basePrice(18.99).productCategory(BOTTLE_OF_PERFUME).quantity(1).build();
        OrderLine packetOfheadachePills = OrderLine.builder().basePrice(9.75).productCategory(PACKET_OF_HEADACHE_PILLS).quantity(1).build();
        OrderLine importedBoxOfChocolates = OrderLine.builder().basePrice(11.25).productCategory(BOX_OF_CHOCOLATES).imported(true).quantity(1).build();

        List<OrderLine> orderLines = new ArrayList<>(Arrays.asList(importedBoxOfChocolates, bottleOfPerfume, packetOfheadachePills, importedBottleOfPerfume));
        ShoppingCart shoppingCart = ShoppingCart.builder().orderlines(orderLines).build();

        taxService.applyTaxes(shoppingCart);

        assertEquals(new Double(6.70d), shoppingCart.getSalesTaxes());
        assertEquals(new Double(74.68d), shoppingCart.getTotalPrice());
    }

    @Test
    public void setTotalPriceOk(){
        OrderLine orderLine1 = OrderLine.builder().totalPrice(10.0001d).build();
        OrderLine orderLine2 = OrderLine.builder().totalPrice(20d).build();
        List<OrderLine> orderLines = new ArrayList<>(Arrays.asList(orderLine1,orderLine2));
        ShoppingCart shoppingCart = ShoppingCart.builder().orderlines(orderLines).build();
        taxService.setTotalPrice(shoppingCart);
        assertEquals(new Double(30d),shoppingCart.getTotalPrice());
    }

    @Test
    public void setSalesTaxesOk(){
        OrderLine orderLine1 = OrderLine.builder().appliedTaxes(1.001d).build();
        OrderLine orderLine2 = OrderLine.builder().appliedTaxes(5.03d).build();
        List<OrderLine> orderLines = new ArrayList<>(Arrays.asList(orderLine1,orderLine2));
        ShoppingCart shoppingCart = ShoppingCart.builder().orderlines(orderLines).build();
        taxService.setSalesTaxes(shoppingCart);
        assertEquals(new Double(6.03d),shoppingCart.getSalesTaxes());
    }


    @Test
    public void processOrderLinesOk(){
        OrderLine orderLine1 = OrderLine.builder().basePrice(10d).productCategory(CHOCOLATE_BAR).build();
        OrderLine orderLine2 = OrderLine.builder().basePrice(15d).productCategory(BOOK).imported(true).build();
        List<OrderLine> orderLines = new ArrayList<>(Arrays.asList(orderLine1,orderLine2));
        ShoppingCart shoppingCart = ShoppingCart.builder().orderlines(orderLines).build();
        taxService.processOrderLines(shoppingCart);
        assertTrue(shoppingCart.getOrderlines().stream().noneMatch(orderLine -> isNull(orderLine.getTaxRate())));
        assertTrue(shoppingCart.getOrderlines().stream().noneMatch(orderLine -> isNull(orderLine.getAppliedTaxes())));
        assertTrue(shoppingCart.getOrderlines().stream().noneMatch(orderLine -> isNull(orderLine.getTotalPrice())));
    }


    @Test
    public void obtainTaxRateOkNoTax(){
        OrderLine orderLine = OrderLine.builder().productCategory(BOOK).build();
        assertEquals(new Double(NO_TAX), taxService.obtainTaxRate(orderLine));
    }

    @Test
    public void obtainTaxRateOkImportedTax(){
        OrderLine orderLine = OrderLine.builder().productCategory(BOOK).imported(true).build();
        assertEquals(new Double(IMPORTED_GOOD_TAX), taxService.obtainTaxRate(orderLine));
    }

    @Test
    public void obtainTaxRateOkBasicTax(){
        OrderLine orderLine = OrderLine.builder().productCategory(BOTTLE_OF_PERFUME).build();
        assertEquals(new Double(BASIC_TAX), taxService.obtainTaxRate(orderLine));
    }

    @Test
    public void obtainTaxRateOkBasicAndImportedTax(){
        OrderLine orderLine = OrderLine.builder().productCategory(BOTTLE_OF_PERFUME).imported(true).build();
        assertEquals(new Double(IMPORTED_GOOD_TAX + BASIC_TAX), taxService.obtainTaxRate(orderLine));
    }


    @Test
    public void obtainImportedTaxOk(){
        assertEquals(new Double(NO_TAX), taxService.obtainImportedTax(OrderLine.builder().imported(false).build()));
        assertEquals(new Double(NO_TAX), taxService.obtainImportedTax(OrderLine.builder().build()));
        assertEquals(new Double(IMPORTED_GOOD_TAX), taxService.obtainImportedTax(OrderLine.builder().imported(true).build()));
    }

    @Test
    public void  applyTaxRateOkBaseCase(){
        OrderLine orderLine = OrderLine.builder().basePrice(10d).taxRate(10d).build();
        taxService.applyTaxRate(orderLine);
        assertEquals(new Double(1d), orderLine.getAppliedTaxes());
        assertEquals(new Double(11d),orderLine.getTotalPrice());
    }

    @Test
    public void  applyTaxRateOkRondingNeededCase(){
        OrderLine orderLine = OrderLine.builder().basePrice(10d).taxRate(10.123231564d).build();
        taxService.applyTaxRate(orderLine);
        assertEquals(new Double(1.05d), orderLine.getAppliedTaxes());
        assertEquals(new Double(11.05d),orderLine.getTotalPrice());
    }



}
