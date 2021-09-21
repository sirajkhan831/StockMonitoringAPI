package com.bridgelabz;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Scanner;

public class StockMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        String apiKey = "UFR1P6WOBTSIH4J8";
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
        String localDate = LocalDate.now().minusDays(1).toString();
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case 2 -> localDate = LocalDate.now().minusDays(3).toString();
            case 1 -> localDate = LocalDate.now().minusDays(2).toString();
        }
        System.out.println("\nAll the stock details of " + symbol + " is listed below for the last market open date " + localDate);
        System.out.println("\nOpening price -> " + object.get("Time Series (60min)").getAsJsonObject().get("" + localDate + " 05:00:00").getAsJsonObject().get("1. open"));
        System.out.println("Closing price -> " + object.get("Time Series (60min)").getAsJsonObject().get("" + localDate + " 05:00:00").getAsJsonObject().get("4. close"));
        System.out.println("High price -> " + object.get("Time Series (60min)").getAsJsonObject().get("" + localDate + " 05:00:00").getAsJsonObject().get("2. high"));
        System.out.println("Low price -> " + object.get("Time Series (60min)").getAsJsonObject().get("" + localDate + " 05:00:00").getAsJsonObject().get("3. low"));
        System.out.println("Volumes -> " + object.get("Time Series (60min)").getAsJsonObject().get("" + localDate + " 05:00:00").getAsJsonObject().get("5. volume"));

    }
}