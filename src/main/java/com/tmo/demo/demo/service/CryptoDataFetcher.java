package com.tmo.demo.demo.service;

import com.tmo.demo.demo.model.CryptoCoin;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

@Component
public class CryptoDataFetcher implements DataFetcher<CryptoCoin> {

    @Override
    public CryptoCoin get(DataFetchingEnvironment arg0) throws Exception {
        final String url = "https://api2.binance.com/api/v3/ticker/24hr";
        RestTemplate client = new RestTemplate();
        CryptoCoin resp = client.getForObject(url, CryptoCoin.class);

        return resp;
    }

}
