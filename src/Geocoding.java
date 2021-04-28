import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

/**
 * Author: @Dmitriy_Khodykin
 * Licence: GPLv3
 *
 * Module is using geocoding service Mapbox
 * DaData https://dadata.ru/api/geocode/
 * Documentation: https://docs.mapbox.com/api/overview/
 */

public class Geocoding {

    public static void main(String[] args) {

        Scanner console = new Scanner(System.in, "UTF-8");
        String address = console.nextLine();
        System.out.println(address);

        Geocoding gc = new Geocoding();
        String result = gc.getResponse(address);
        System.out.println(result);
    }

    private static String getToken() {
        // Read authorisation data from file
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("src\\auth.properties");
            property.load(fis);
            return property.getProperty("mapboxToken");
        } catch (java.io.IOException e) {
            System.err.println("error: Property not found");
        }
        return null;
    }

    private String getResponse(String adress) {

        String mapboxGeocodingUrl = "https://api.mapbox.com/geocoding/v5/mapbox.places/";
        String mapboxGeoPoint = adress;
        String mapboxAccessParam = ".json?limit=2&access_token=";
        String mapboxToken = getToken();

        String query = mapboxGeocodingUrl + mapboxGeoPoint + mapboxAccessParam + mapboxToken;

        HttpURLConnection connection = null;

        try {
            // Create connection object
            connection = (HttpURLConnection) new URL(query).openConnection();

            // Connection properties
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(500);
            connection.setReadTimeout(500);

            connection.connect();

            StringBuilder sb = new StringBuilder();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();
            } else {
                System.out.println("fail: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }

        } catch (Throwable cause) {
            cause.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
