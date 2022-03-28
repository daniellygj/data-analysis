package com.challenge.dataanalysis.model.converter;


import com.challenge.dataanalysis.model.Seller;

public class SellerConverter {

    public static Seller convert(String[] data) {
        return Seller.builder()
                .cpf(data[1])
                .name(data[2])
                .salary(Double.parseDouble(data[3]))
                .build();
    }

}
