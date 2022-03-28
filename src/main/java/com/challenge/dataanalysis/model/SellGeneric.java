package com.challenge.dataanalysis.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellGeneric {

    private Integer id;

    private int sellQty;

    private float totalSellsValue;

    private int totalItensQty;

    private String sellerName;

}
