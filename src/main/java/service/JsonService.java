package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonService {

    private HashMap<String, List<JSONObject>> hashMap = new HashMap<String, List<JSONObject>>();
    /**
     * @param url source for JSON as a String
     * @return  JSON from the URL
     */
    public JSONObject getJsonFromUrl(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        JSONParser parser = new JSONParser();
        String line;
        JSONObject jsonObject = null;
        try {
            InputStream inputStream = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            while((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
            jsonObject = (JSONObject) parser.parse(stringBuilder.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * @return populated selectorList of JSON Objects by recursively searching the given JSONObject
     */
    public List<JSONObject> find(JSONObject jsonObject, String selector) {
        List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
        if(jsonObject == null || selector.equals("")){
            System.out.println("Please enter a valid selector.");
            return jsonObjectList;
        }
        if(getCacheResults(selector)!=null) {
            return getCacheResults(selector);
        } else {
            jsonObjectList = find(jsonObject, selector, jsonObjectList);
            addCacheResults(selector, jsonObjectList);
        }

        return  jsonObjectList;
    }

    /**
     * @param jsonObject value of selector from the user as a String
     * @param selector value of selector from the user as a String
     * @param selectorList value of selector from the user as a String
     * @return populated selectorList of JSON Objects by recursively searching the given JSONObject
     */
    private List<JSONObject> find(JSONObject jsonObject, String selector, List<JSONObject> selectorList) {

        if (jsonObject.containsKey("contentView"))
            find((JSONObject) jsonObject.get("contentView"), selector, selectorList);

        if (jsonObject.containsKey("control"))
            find((JSONObject) jsonObject.get("control"), selector, selectorList);

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
            JSONArray classNames = (JSONArray) jsonObject.get("classNames");
            for (Object classname : classNames) {
                if (classname.equals(selector)) {
                    selectorList.add(jsonObject);
                }
            }
        }
        return selectorList;
    }

    /**
     * @param jsonObjectList value of selector from the user as a String
     * pretty prints JSON Objects list
     */
    public void printJsonList(List<JSONObject> jsonObjectList) {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        for(JSONObject jsonObject : jsonObjectList){
            System.out.println(g.toJson(jsonObject));
        }
        System.out.println("JSON Objects count: " + jsonObjectList.size());
        System.out.println("---------------------------------");
    }

    /**
     * @return List of JSON objects which have already been queried, else returns null
     */
    private List<JSONObject> getCacheResults(String selector) {
         if(hashMap.containsKey(selector)){
             return hashMap.get(selector);
         }
         return null;
    }

    /**
     * @param selector value of selector from the user as a String
     * @param jsonObjectList list of json objects from the JSON
     * adds the results to hashmap to build the cache
     */
    private void addCacheResults(String selector, List<JSONObject> jsonObjectList) {
        hashMap.put(selector, jsonObjectList);
    }

}
