package com.challenge.dataanalysis.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class Sell {

    private int id;

    private List<Item> itemList;

    private String salesman;

}
