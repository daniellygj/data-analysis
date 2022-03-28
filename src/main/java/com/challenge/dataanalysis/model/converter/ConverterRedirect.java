package com.challenge.dataanalysis.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConverterRedirect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterRedirect.class);

    public static Object lineToObject(String line) {
        String[] data = line.split("ç");

        switch (data[0]) {
            case "001":
                return SellerConverter.convert(data);

            case "002":
                return ClientConverter.convert(data);

            case "003":
                return SellConverter.convert(data);

            default:
                LOGGER.error("Identificador invalido: " + "data[0]");
        }

        return null;
    }
}
