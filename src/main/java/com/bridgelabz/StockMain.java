package com.bridgelabz;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class StockMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        String apiKey = "S553X1Y9BFCAP8YP";
        System.out.println("Welcome to the NYSE stock market real time data monitor");
        System.out.println("NOTE : You need to create an API key for this program to work. The default API Key included in this program can only work 5 times a day");
        System.out.print("To use the default API key press 1 (may not work depending on number of usage per day) or To create a new API key press 2 : ");
        if (new Scanner(System.in).nextInt() == 2) {
            System.out.println("Your default browser will automatically open in 4 seconds, generate a new key and copy it");
            Thread.sleep(4000);
            try {
                Desktop.getDesktop().browse(new URI("https://www.alphavantage.co/support/#api-key"));
//              Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome https://www.alphavantage.co/support/#api-key"});
            } catch (Exception e) {
                System.out.println("Their was an error opening the browser go to the given link and generate a new key : https://www.alphavantage.co/support/#api-key");
                System.out.print("Paste the new API Key here : ");
                apiKey = new Scanner(System.in).nextLine();
                apiConnection(apiKey);
            }
            Thread.sleep(1000);
            System.out.print("Paste the new API Key here : ");
            apiKey = new Scanner(System.in).nextLine();
            apiConnection(apiKey);
        } else apiConnection(apiKey);
    }

    public static void apiConnection(String apiKey) throws IOException, InterruptedException {
        System.out.print("Enter NYSE symbol of the stock to know the details : ");
        String symbol = new Scanner(System.in).nextLine().toUpperCase();
        var url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=60min&apikey=" + apiKey;
        var request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
        var client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(response.body(), JsonObject.class);
        System.out.println("Press for :  1. Opening Price 2. Closing Price 3. High 4. Low 5. Volume ");
        switch (new Scanner(System.in).nextInt()) {
            case 1 -> System.out.println("Opening price : " + object.get("Time Series (60min)").getAsJsonObject().get("2021-09-17 20:00:00").getAsJsonObject().get("1. open"));
            case 2 -> System.out.println("Closing price : " + object.get("Time Series (60min)").getAsJsonObject().get("2021-09-17 20:00:00").getAsJsonObject().get("4. close"));
            case 3 -> System.out.println("High price : " + object.get("Time Series (60min)").getAsJsonObject().get("2021-09-17 20:00:00").getAsJsonObject().get("2. high"));
            case 4 -> System.out.println("Low price : " + object.get("Time Series (60min)").getAsJsonObject().get("2021-09-17 20:00:00").getAsJsonObject().get("3. low"));
            case 5 -> System.out.println("Volume price : " + object.get("Time Series (60min)").getAsJsonObject().get("2021-09-17 20:00:00").getAsJsonObject().get("5. volume"));
        }
    }
}