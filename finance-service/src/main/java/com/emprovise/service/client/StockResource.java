package com.emprovise.service.client;

import com.emprovise.service.api.SSLUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/rest/stock")
public class StockResource {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${alphavantage.apikey}")
    private String apiKey;

    private static final String ALPHA_VANTAGE_URL = "https://www.alphavantage.co/query";
    private static final String TIME_SERIES_INTRADAY = "TIME_SERIES_INTRADAY";

    @GetMapping("/symbol/{symbol}/interval/{interval}")
    public JsonObject getStockDetails(@PathVariable("symbol") String symbol,
                                @PathVariable("interval") String interval) throws KeyManagementException, NoSuchAlgorithmException {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(ALPHA_VANTAGE_URL)
                .queryParam("function", TIME_SERIES_INTRADAY)
                .queryParam("symbol", symbol)
                .queryParam("interval", interval)
                .queryParam("apikey", apiKey);

        SSLUtil.turnOffSslChecking();
        String response = restTemplate.getForObject(builder.toUriString(), String.class);

        if(response != null) {
            JsonParser parser = new JsonParser();
            return parser.parse(response).getAsJsonObject();
        }

        return new JsonObject();
    }

    @GetMapping("/greeting")
    public String greeting() {
        return "Hello World";
    }

}