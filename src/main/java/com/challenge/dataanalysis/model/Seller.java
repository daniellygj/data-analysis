package com.challenge.dataanalysis.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Seller {

    private String name;

    private String cpf;

    private Double salary;

}
