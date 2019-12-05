package es.jorgesanz.salestaxes.repository;

import org.springframework.stereotype.Repository;

import static es.jorgesanz.salestaxes.util.Constants.*;

@Repository
public class TaxRepositoryImpl implements TaxRepository {

    @Override
    public Double find(String saleReference) {
        switch (saleReference) {
            case MUSIC_CD:
            case BOTTLE_OF_PERFUME:
                return BASIC_TAX;
            case BOOK:
            case CHOCOLATE_BAR:
            case BOX_OF_CHOCOLATES:
            case PACKET_OF_HEADACHE_PILLS:
                return NO_TAX;
            default:
                throw new IllegalArgumentException(String.format("Reference %s not found", saleReference));
        }
    }
}

