package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class JsonService {

    /**
     * @param url source for JSON as a String
     * @return  JSON in form of String from the URL
     */
    public final String getJsonFromUrl(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            InputStream inputStream = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            while((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
