package es.jorgesanz.salestaxes.validator;

import es.jorgesanz.salestaxes.model.OrderLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static es.jorgesanz.salestaxes.util.Constants.BASIC_TAX;

@RunWith(MockitoJUnitRunner.class)
public class OrderLineValidatorTest {

    @InjectMocks
    private OrderLineValidatorImpl orderLineValidator;

    @Test(expected = IllegalStateException.class)
    public void validateKoNoTax(){

        OrderLine orderLine = OrderLine.builder().basePrice(1.45d).build();
        orderLineValidator.validate(orderLine);
    }

    @Test(expected = IllegalStateException.class)
    public void validateKoNoBasePrice(){

        OrderLine orderLine = OrderLine.builder().taxRate(BASIC_TAX).build();
        orderLineValidator.validate(orderLine);
    }

    @Test(expected = IllegalStateException.class)
    public void validateKoNegativePrice(){

        OrderLine orderLine = OrderLine.builder().taxRate(BASIC_TAX).basePrice(-5d).build();
        orderLineValidator.validate(orderLine);
    }

    @Test(expected = IllegalStateException.class)
    public void validateKoZeroPrice(){

        OrderLine orderLine = OrderLine.builder().taxRate(BASIC_TAX).basePrice(0d).build();
        orderLineValidator.validate(orderLine);
    }

    @Test(expected = IllegalStateException.class)
    public void validateKoNegativeTaxPrice(){

        OrderLine orderLine = OrderLine.builder().taxRate(-1d).basePrice(10d).build();
        orderLineValidator.validate(orderLine);
    }

    @Test
    public void validateOk(){

        OrderLine orderLine = OrderLine.builder().taxRate(BASIC_TAX).basePrice(10d).build();
        orderLineValidator.validate(orderLine);
    }

}
