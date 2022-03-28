package com.challenge.dataanalysis.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Item {

    private String id;

    private float price;

    private float quantity;

}
