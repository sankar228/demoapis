package com.tmo.demo.demo.model;

import lombok.Data;

@Data
public class CryptoCoin {
    String symbol;
    String priceChange;
    String priceChangePercent;
    String weightedAvgPrice;
    String prevClosePrice;
    String lastPrice;
    String lastQty;
    String bidPrice;
    String bidQty;
    String askPrice;
    String askQty;
    String openPrice;
    String highPrice;
    String lowPrice;
    String volume;
    String quoteVolume;
    float openTime;
    float closeTime;
    float firstId;
    float lastId;
    float count;
}
