package com.challenge.dataanalysis.service;

import com.challenge.dataanalysis.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.challenge.dataanalysis.model.converter.ConverterRedirect.lineToObject;

@Service
public class AnalysisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisService.class);

    public static String getResult(Result result) {
        StringBuilder output = new StringBuilder();

        output.append("Client quantity: ").append(result.getClientCount());
        output.append("\nSeller Quantity: ").append(result.getSellerCount());
        output.append("\nMost Expensive Sale ID: ").append(result.getMostExpensiveSaleId());
        output.append("\nWorst seller: ").append(result.getWorstSeller());

        return output.toString();
    }

    public Result analyze(List<String> lines) {
        Result result = new Result();

        List<Sell> sellList = new ArrayList<>();
        List<Seller> sellerList = new ArrayList<>();

        lines.forEach(line -> {
            Object obj = lineToObject(line);

            if (obj instanceof Seller) {
                sellerList.add((Seller) obj);
            } else if (obj instanceof Client) {
                result.setClientCount(result.getClientCount() + 1);
            } else if (obj instanceof Sell) {
                sellList.add((Sell) obj);
            }
        });

        result.setSellerCount(sellerList.size());
        validateSells(sellerList, sellList, result);
        getExpensiveSaleAndWorstSeller(sellList, result);

        return result;
    }

    private void validateSells(List<Seller> sellerList, List<Sell> sellList, Result result) {
        Iterator<Sell> it = sellList.iterator();

        while (it.hasNext()) {
            Sell sell = it.next();

            boolean sellerExists = sellerList.stream().anyMatch(seller -> seller.getName().equals(sell.getSalesman()));

            if (!sellerExists) {
                LOGGER.error("Invalid sell, seller " + sell.getSalesman() + " does not exists");
                it.remove();
                result.getInvalidSell().add(sell);
            }
        }
    }

    private void getExpensiveSaleAndWorstSeller(List<Sell> sellList, Result result) {
        Integer expensiveSellId = null;
        Float expensiveSellValue = null;

        Map<String, SellGeneric> stringSellGenericMap = new HashMap<>();

        for (Sell sell : sellList) {
            Float sum = getSellSum(sell).get();

            if (expensiveSellValue == null || expensiveSellValue < sum) {
                expensiveSellId = sell.getId();
                expensiveSellValue = sum;
            }

            SellGeneric sellGeneric = stringSellGenericMap.get(sell.getSalesman());

            if (sellGeneric == null) {
                SellGeneric newSellGeneric = SellGeneric.builder()
                        .id(sell.getId())
                        .sellQty(1)
                        .totalSellsValue(sum)
                        .totalItensQty(sell.getItemList().size())
                        .sellerName(sell.getSalesman())
                        .build();

                stringSellGenericMap.put(sell.getSalesman(), newSellGeneric);
            } else {
                sellGeneric.setSellQty(sellGeneric.getSellQty() + 1);
                sellGeneric.setTotalSellsValue(sellGeneric.getTotalSellsValue() + sum);
                sellGeneric.setTotalItensQty(sellGeneric.getTotalItensQty() + sell.getItemList().size());
            }
        }

        result.setMostExpensiveSaleId(expensiveSellId);
        result.setWorstSeller(findWorstSeller(stringSellGenericMap));
    }

    private String findWorstSeller(Map<String, SellGeneric> stringSellGenericMap) {
        SellGeneric worstSeller = new SellGeneric();

        for (Map.Entry<String, SellGeneric> sellGeneric : stringSellGenericMap.entrySet()) {

            if (worstSeller.getId() == null || worstSeller.getSellQty() > sellGeneric.getValue().getSellQty()) { // primeiro considera quem teve menos vendas
                worstSeller = sellGeneric.getValue();
            } else if (worstSeller.getSellQty() == sellGeneric.getValue().getSellQty() && worstSeller.getTotalSellsValue() > sellGeneric.getValue().getTotalSellsValue()) { // para desempatar, quem teve a venda com valor mais baixo
                worstSeller = sellGeneric.getValue();
            } else if (worstSeller.getTotalSellsValue() == sellGeneric.getValue().getTotalSellsValue() && worstSeller.getTotalSellsValue() == sellGeneric.getValue().getTotalSellsValue()
                    && worstSeller.getSellQty() > sellGeneric.getValue().getSellQty()) { // para desempatar, quem vendeu menos itens
                worstSeller = sellGeneric.getValue();
            }
        }

        return worstSeller.getSellerName();
    }

    private AtomicReference<Float> getSellSum(Sell sell) {
        AtomicReference<Float> sum = new AtomicReference<>((float) 0);
        sell.getItemList().forEach(item -> {
            sum.updateAndGet(v -> v + (item.getPrice() * item.getQuantity()));
        });

        return sum;
    }
}
