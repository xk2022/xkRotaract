package com.xk.common.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by yuan on 2024/08/01
 */
@Component
public class GoogleApiGeocode {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleApiGeocode.class);
    private static final String API_KEY = "AIzaSyDgI-sfjvHKCWBnVoDDeRBOYxo0oTY45tQ"; // 替换为你的 API 密钥
    private static final String GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    public static void main(String args) {
        String address = "1600 Amphitheatre Parkway, Mountain View, CA"; // 替换为你想查询的地址
        try {
            String url = String.format("%s?address=%s&key=%s", GEOCODING_URL,
                    java.net.URLEncoder.encode(address, "UTF-8"), API_KEY);

            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            String json = EntityUtils.toString(response.getEntity());

            // 解析 JSON 响应
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.getString("status").equals("OK")) {
                JSONObject location = jsonObject.getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");
                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");

                System.out.printf("Latitude: %f, Longitude: %f%n", lat, lng);
            } else {
                System.out.println("Error: " + jsonObject.getString("status"));
            }

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String code(String address) {
        String result = "";

        try {
            String url = String.format("%s?address=%s&key=%s", GEOCODING_URL,
                    java.net.URLEncoder.encode(address, "UTF-8"), API_KEY);

            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            String json = EntityUtils.toString(response.getEntity());

            // 解析 JSON 响应
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.getString("status").equals("OK")) {
                JSONObject location = jsonObject.getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");
                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");

                System.out.printf("Latitude: %f, Longitude: %f%n", lat, lng);
                result = lat + "," + lng;
            } else {
                System.out.println("Error: " + jsonObject.getString("status"));
            }

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
