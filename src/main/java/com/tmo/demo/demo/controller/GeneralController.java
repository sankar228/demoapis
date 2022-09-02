package com.tmo.demo.demo.controller;

import java.net.http.HttpResponse;
import java.util.List;

import com.tmo.demo.demo.model.CryptoCoin;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class GeneralController {
    @PostMapping(path = "/cry", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CryptoCoin>> getCrypto(@RequestBody String query) {

        final String url = "https://api2.binance.com/api/v3/ticker/24hr";
        RestTemplate client = new RestTemplate();
        List<CryptoCoin> resp = client.getForObject(url, List.class);

        return new ResponseEntity<List<CryptoCoin>>(resp, HttpStatus.OK);
    }
}
