import org.json.simple.JSONObject;
import service.JsonService;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        JsonService jsonService = new JsonService();
        List<JSONObject> list;
        JSONObject json = null;
        String url = "https://raw.githubusercontent.com/jdolan/quetoo/master/src/cgame/default/ui/settings/SystemViewController.json";
        Scanner userInput = new Scanner(System.in);
        String selector;
        boolean runFlag = true;

        json = jsonService.getJsonFromUrl(url);

        while (runFlag) {
            System.out.print("Enter the selector or 'q' to quit: ");
            System.out.println();
            selector = userInput.nextLine();
            if (selector.equalsIgnoreCase("q")) {
                runFlag = false;
                System.out.print("Quitting!");
            } else {
                try {
                    list = jsonService.find(json, selector);
                    jsonService.printJsonList(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
