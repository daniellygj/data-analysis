package com.challenge.dataanalysis.model.converter;

import com.challenge.dataanalysis.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemConverter {
    public static Item convert(String[] data) {
        return Item.builder()
                .id(data[0])
                .quantity(Float.parseFloat(data[1]))
                .price(Float.parseFloat(data[2]))
                .build();
    }

    public static List<Item> stringToItemObject(String itemString) {
        List<Item> itemList = new ArrayList<>();
        List<String> itens = List.of(itemString.split(","));

        itens.forEach(item -> {
            String itemReplace = item.replace("[", "").replace("]", "");
            String[] itemData = itemReplace.split("-");
            Item newItem = convert(itemData);
            itemList.add(newItem);
        });

        return itemList;
    }
}
