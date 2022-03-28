package com.challenge.dataanalysis.model.converter;


import com.challenge.dataanalysis.model.Sell;

public class SellConverter {

    public static Sell convert(String[] data) {
        return Sell.builder()
                .id(Integer.parseInt(data[1]))
                .itemList(ItemConverter.stringToItemObject(data[2]))
                .salesman(data[3])
                .build();
    }
}
