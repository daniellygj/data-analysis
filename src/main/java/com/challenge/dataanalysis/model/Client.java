package com.challenge.dataanalysis.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Client {

    private String name;

    private String cnpj;

    private String business;

}
