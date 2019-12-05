package es.jorgesanz.salestaxes.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static es.jorgesanz.salestaxes.util.Constants.*;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TaxRepositoryImplTest {

    @InjectMocks
    private TaxRepositoryImpl taxRepositoryImpl;

    @Test
    public void findOk(){
        Double tax = taxRepositoryImpl.find(BOOK);
        assertEquals (new Double(NO_TAX),tax);

        tax = taxRepositoryImpl.find(MUSIC_CD);
        assertEquals (new Double(BASIC_TAX),tax);

        tax = taxRepositoryImpl.find(CHOCOLATE_BAR);
        assertEquals (new Double(NO_TAX),tax);

        tax = taxRepositoryImpl.find(BOX_OF_CHOCOLATES);
        assertEquals (new Double(NO_TAX),tax);

        tax = taxRepositoryImpl.find(BOTTLE_OF_PERFUME);
        assertEquals (new Double(BASIC_TAX),tax);

        tax = taxRepositoryImpl.find(PACKET_OF_HEADACHE_PILLS);
        assertEquals (new Double(NO_TAX),tax);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void findKo(){
        taxRepositoryImpl.find("wrong reference");
    }
}
