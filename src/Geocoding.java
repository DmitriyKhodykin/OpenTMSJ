import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Author: @Dmitriy_Khodykin
 * Licence: GPLv3
 *
 * Module is using geocoding service DaData
 * Documentation: https://dadata.ru/api/geocode/
 */

public class Geocoding {

    public static void main(String[] args) {
        // Address input from terminal
        Scanner console = new Scanner(System.in, "UTF-8");
        String address = console.nextLine();

        Geocoding gc = new Geocoding();
        String result = gc.getResponse(address);
        System.out.println(result);
    }

    private String getResponse(String address) {

        address = "[ \"" + address + "\" ]"; // Format DaData service: [ "address" ]
        String urlAddress = "https://cleaner.dadata.ru/api/v1/clean/address";
        URL url;
        HttpURLConnection httpURLConnection;
        OutputStream os = null;
        InputStreamReader isr = null;
        BufferedReader bfr = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // Create byte-object for output stream
            byte[] message = address.getBytes(StandardCharsets.UTF_8);

            // Create connection object
            url = new URL(urlAddress);
            httpURLConnection = (HttpURLConnection)url.openConnection();

            // Connection properties
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(500);
            httpURLConnection.setReadTimeout(500);

            // Request properties = Headers
            httpURLConnection.addRequestProperty("authorization", GetToken.getToken("daToken"));
            httpURLConnection.addRequestProperty("x-secret", GetToken.getToken("daSecret"));
            httpURLConnection.addRequestProperty("content-type", "application/json");
            httpURLConnection.addRequestProperty("cache-control", "no-cache");

            httpURLConnection.connect();

            try {
                os = httpURLConnection.getOutputStream();
                os.write(message); // byte-object
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                isr = new InputStreamReader(httpURLConnection.getInputStream());
                bfr = new BufferedReader(isr);
                String response;
                while ((response=bfr.readLine()) != null) {
                    stringBuilder.append(response);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bfr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}
