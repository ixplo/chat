package lesson8.ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Probe {
    public static void main(String[] args) {
        String site = "https://ru.wikipedia.org/wiki/MIME";
        try {
            URL url = new URL(site);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
            StringBuilder result = new StringBuilder();
            String input;
            while((input = bufferedReader.readLine()) != null){result.append(input).append("\n");}
            System.out.println(result);
            System.out.println(responseCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
