package service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonService {

    /**
     * @param url source for JSON as a String
     * @return  JSON from the URL
     */
    public JSONObject getJsonFromUrl(String url) throws ParseException {
        StringBuilder stringBuilder = new StringBuilder();
        JSONParser parser = new JSONParser();
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
        return (JSONObject) parser.parse(stringBuilder.toString());
    }

    /**
     * @param jsonObject value of selector from the user as a String
     * @param selector value of selector from the user as a String
     * @param selectorList value of selector from the user as a String
     * @return populated selectorList of JSON Objects by recursively searching the given JSONObject
     */
    private List<JSONObject> find(JSONObject jsonObject, String selector, List<JSONObject> selectorList) {

        if (jsonObject.containsKey("contentView")) {
            find((JSONObject) jsonObject.get("contentView"), selector, selectorList);
        }

        if (jsonObject.containsKey("control")) {
            find((JSONObject) jsonObject.get("control"), selector, selectorList);
        }

        if (jsonObject.containsKey("subviews")) {
            for (int i = 0; i < ((JSONArray) jsonObject.get("subviews")).size(); i++)
                find((JSONObject) ((JSONArray) jsonObject.get("subviews")).get(i), selector, selectorList);
        }

        if (jsonObject.containsKey("class")) {
            if (jsonObject.get("class").equals(selector)) {
                selectorList.add(jsonObject);
            }
        }

        if (jsonObject.containsKey("identifier")) {
            if (jsonObject.get("identifier").equals(selector)) {
                selectorList.add(jsonObject);
            }
        }

        if (jsonObject.containsKey("classNames")) {
            JSONArray classnames = (JSONArray) jsonObject.get("classNames");
            for (Object classname : classnames) {
                if (classname.equals(selector)) {
                    selectorList.add(jsonObject);
                }
            }
        }
        return selectorList;
    }
}
