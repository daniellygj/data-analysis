package com.challenge.dataanalysis.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private int sellerCount;

    private int clientCount;

    private String worstSeller;

    private int mostExpensiveSaleId;

    private List<Sell> invalidSell = new ArrayList<>();
}
