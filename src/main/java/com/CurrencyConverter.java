package com;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

public class CurrencyConverter {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Type currency to convert from");
        String convertFrom = scanner.nextLine();
        System.out.println("Type currency to convert to");
        String convertTo = scanner.nextLine();
        System.out.println("Type quantity to convert");
        BigDecimal quantity = scanner.nextBigDecimal();

        // Fixed the URL with the '=' sign
        String urlString = "https://api.exchangerate.host/latest?base=" + convertFrom.toUpperCase();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String stringResponse = response.body().string();

        // Handling case where the "rates" key might not be found in the response
        JSONObject jsonObject = new JSONObject(stringResponse);
        if (jsonObject.has("rates")) {
            JSONObject ratesObject = jsonObject.getJSONObject("rates");
            if (ratesObject.has(convertTo.toUpperCase())) {
                BigDecimal rate = ratesObject.getBigDecimal(convertTo.toUpperCase());
                BigDecimal result = rate.multiply(quantity);
                System.out.println("Converted amount: " + result);
            } else {
                System.out.println("Currency not found: " + convertTo);
            }
        } else {
            System.out.println("Error: Rates data not available in the response.");
        }
    }
}
