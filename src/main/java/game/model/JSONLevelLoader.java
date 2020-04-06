package game.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JSONLevelLoader {
    /**
     * JSON object used to extract all the necessary information
     */
    private JSONObject obj;

    private ArrayList<String> jsonLevels = new ArrayList<>();

    /**
     * Takes the specified json path string and extracts all of the relevant information
     * @param jsonPath Path to the json file
     */
    public JSONLevelLoader(String jsonPath) {
        try {
            FileReader reader = new FileReader("src/main/resources/" + jsonPath);
            JSONParser parser = new JSONParser();
            this.obj = (JSONObject) parser.parse(reader);
            this.extract();
            reader.close();
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private void extract(){
        JSONArray array = (JSONArray) this.obj.get("levels");
        for (Object o : array) {
            jsonLevels.add((String) o);
        }
    }

    public ArrayList<String> getJsonLevels() {
        return jsonLevels;
    }
}
