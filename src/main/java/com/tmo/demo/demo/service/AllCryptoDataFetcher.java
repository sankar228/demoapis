package com.tmo.demo.demo.service;

import java.util.List;

import com.tmo.demo.demo.model.CryptoCoin;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

@Component
public class AllCryptoDataFetcher implements DataFetcher<List<CryptoCoin>> {

    @Override
    public List<CryptoCoin> get(DataFetchingEnvironment arg0) throws Exception {

        final String url = "https://api2.binance.com/api/v3/ticker/24hr";
        RestTemplate client = new RestTemplate();
        List<CryptoCoin> resp = client.getForObject(url, List.class);

        return resp;
    }

}
