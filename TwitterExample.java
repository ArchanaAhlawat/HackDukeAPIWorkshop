import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.*;
import com.google.gson.*;

public class TwitterExample {
    private static final String USER_AGENT = "HackDukeDemo";
    private static final String oath_consumer_key = "bl6cRsiHKkMlrHQHf8Xu6ugfm";
    private static final String oath_consumer_private_key = "Ycq24liikxVVUAoOgihZxLVaLUWEZD9FkU5X2l95XFvBee2Q0V";
    private static String oath_combo;
    private static String oath_encoded;

    private static String bearer = "AAAAAAAAAAAAAAAAAAAAAPZh8gAAAAAAdkGdnBz%2FNFSN8CLm3no0iP22QiA%3D85Z15T1JmF6otZEnVzkKUHIiShbj0NFsUGnVWqcXv3ONnGU4n6";

    private static void setUpAuth() {
        oath_combo = oath_consumer_key + ":" + oath_consumer_private_key;
        oath_encoded = Base64.getEncoder().encodeToString(oath_combo.getBytes());
    }

    private static void sendPost() throws IOException {
        String POST_string = "Basic " + oath_encoded;
        URL obj = new URL("https://api.twitter.com/oauth2/token");
        HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Authorization", POST_string);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        BufferedWriter httpRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        httpRequestBodyWriter.write("grant_type=client_credentials");
        httpRequestBodyWriter.close();

        int responseCode = conn.getResponseCode();
        System.out.println("response code: " + responseCode);
        Scanner httpResponseScanner = new Scanner(conn.getInputStream());
        while(httpResponseScanner.hasNextLine()) {
            System.out.println(httpResponseScanner.nextLine());
        }
        httpResponseScanner.close();
    }

    private static void sendGet() throws IOException {
        String GET_URL = "https://api.twitter.com/1.1/trends/place.json";
        GET_URL = GET_URL + "?id=1";
        URL obj = new URL(GET_URL);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + bearer);
        int responseCode = conn.getResponseCode();
        System.out.println("GET response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            JsonParser parser = new JsonParser();
            JsonElement parsedObject = parser.parse(response.toString().substring(1, response.toString().length() - 1));
            JsonObject jsonObj = parsedObject.getAsJsonObject();
            JsonArray trendsArray = jsonObj.get("trends").getAsJsonArray();
            for (int i = 0; i < trendsArray.size(); i++) {
                JsonElement tempObj = trendsArray.get(i);
                String trend = tempObj.getAsJsonObject().get("name").toString();
                System.out.println(trend);

            }
            String test = jsonObj.get("trends").getAsJsonArray().get(0).toString();

            //System.out.println(test);
        }
    }


    public static void main(String[] args) throws Exception{
        //sendPost();
        setUpAuth();
        sendGet();
    }
}
