package sg.edu.nus.iss.WS17.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import sg.edu.nus.iss.WS17.model.Currency;

@Service
public class CurrencyService {

    @Value("${currency.key}")
    private String apiKey;
   
    public List<Currency> getCurrencyList() {
        
        
        String url = UriComponentsBuilder
            .fromUriString("https://free.currconv.com/api/v7/countries")
            .queryParam("apiKey", apiKey)
            .toUriString();

            System.out.println(">>>> url: " + url);
 
            RequestEntity req = RequestEntity.get(url).build();
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> resp = template.exchange(req, String.class);

            // JsonArray dataArr = null;
            // InputStream isArr = new ByteArrayInputStream(resp.getBody().getBytes());
            // JsonReader readerArr = Json.createReader(isArr);
            // dataArr = readerArr.readArray();

            // System.out.println(">>>>dataArr: " + dataArr);

            JsonObject data = null;
            InputStream is = new ByteArrayInputStream(resp.getBody().getBytes());
            JsonReader reader = Json.createReader(is);
            data = reader.readObject();
            data = data.getJsonObject("results");

            // JsonArrayBuilder currencyArr = Json.createArrayBuilder();
            // currencyArr.add(data.toString());


            Collection<JsonValue> currencylist = new ArrayList<JsonValue>();
            currencylist = data.values();

            Iterator<JsonValue> itr = currencylist.iterator();
            List<JsonObject> l = new ArrayList<JsonObject>();
            while(itr.hasNext()){
                Currency currencies = new Currency();
                l.add((JsonObject) itr.next());
            }

            //System.out.println(">>>>currencylistL: " + l);
            
            List<Currency> currencyListBack = new ArrayList<Currency>();
            for(int i =0; i<l.size(); i++){
                
                Currency currencies = new Currency();
                currencies.setCurrencyName(l.get(i).getString("currencyName"));
                currencies.setCurrencySymbol(l.get(i).getString("currencySymbol"));
                currencies.setId(l.get(i).getString("id"));
                currencies.setNamePlusSymbol(l.get(i).getString("currencyName") +" " + l.get(i).getString("currencySymbol"));
                currencies.setCurrencyId(l.get(i).getString("currencyId"));
                currencyListBack.add(currencies);
            }
        return currencyListBack;
    }

    public Double getConversion(String from, String to, Double amt) {
        String url = UriComponentsBuilder
            .fromUriString("https://free.currconv.com/api/v7/convert")
            .queryParam("q", from + "_" + to)
            .queryParam("compact", "ultra")
            .queryParam("apiKey", apiKey)
            .toUriString();

            System.out.println(">>>> url: " + url);

            RequestEntity req = RequestEntity.get(url).build();
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> resp = template.exchange(req, String.class);
            

            InputStream is = new ByteArrayInputStream(resp.getBody().getBytes());
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();

            Collection<JsonValue> conversionRate = data.values();
            Iterator<JsonValue> itr = conversionRate.iterator();
            String rateStr = itr.next().toString();
            float rate = Float.parseFloat(rateStr);
            Double convertedAmt = amt * rate ;

        
            System.out.println(">>>> data: " + rate);

        return convertedAmt;
    }
}
