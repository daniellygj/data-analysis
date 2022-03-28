package com.challenge.dataanalysis.model.converter;


import com.challenge.dataanalysis.model.Client;

public class ClientConverter {

    public static Client convert(String[] data) {
        return Client.builder()
                .cnpj(data[1])
                .name(data[2])
                .business(data[3])
                .build();
    }
}
