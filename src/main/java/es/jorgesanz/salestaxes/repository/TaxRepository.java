package es.jorgesanz.salestaxes.repository;

import org.springframework.stereotype.Repository;

import static es.jorgesanz.salestaxes.util.Constants.*;

@Repository
public class TaxRepository {
    public Double find(String saleReference) {
        switch (saleReference){
            case MUSIC_CD:
            case BOTTLE_OF_PERFUME:
            return BASIC_TAX;
            default: return 0d;
        }
    }
}
