package com.mycompany.app;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Task3 {
    public static void startTask(WebDriver webDriver) {
        try {
            webDriver.get("https://api.open-meteo.com/v1/forecast?latitude=56&longitude=44&hourly=temperature_2m,rain&current=cloud_cover&timezone=Europe%2FMoscow&forecast_days=1&wind_speed_unit=ms");
            WebElement elem = webDriver.findElement(By.tagName("pre"));

            ArrayList<String> result = parseMeteo(elem);
            System.out.println(String.join("\n", result));

            new java.io.File("result").mkdir();
            try (PrintWriter writer = new PrintWriter(new FileWriter("result/forecast.txt"))) {
                writer.println((String.join("\n", result)));
            }
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.toString());
        }
    }

    private static ArrayList<String> parseMeteo(WebElement elem) throws ParseException {
        String json_str = elem.getText();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(json_str);

        JSONObject hourly = (JSONObject) obj.get("hourly");
        JSONArray time = (JSONArray) hourly.get("time");
        JSONArray temperature = (JSONArray) hourly.get("temperature_2m");
        JSONArray rain = (JSONArray) hourly.get("rain");

        ArrayList<String> result = new ArrayList<>();

        result.add("%-3s %-20s %-12s %-10s".formatted( "№","Дата/время","Температура","Осадки (мм)"));
        for (int i = 1; i != time.size(); i++) {
            result.add("%-3s %-20s %-12s %-10s".formatted(i, time.get(i), temperature.get(i), rain.get(i)));
        }
        return result;
    }
}
