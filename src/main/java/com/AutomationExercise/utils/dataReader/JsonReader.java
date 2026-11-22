package com.AutomationExercise.utils.dataReader;

import com.AutomationExercise.utils.logs.LogsManager;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class  JsonReader {
    //private final String TEST_DATA_PATH = "src/main/resources/test-data/";
    private final String TEST_DATA_PATH = "src/test/resources/test-data/";
    String jsonContent;
    String jsonFileName;
    public JsonReader(String JsonFileName) {
        this.jsonFileName = JsonFileName;
        try{
            JSONObject data = (JSONObject) new JSONParser().
                    parse(new FileReader(TEST_DATA_PATH + JsonFileName +  ".json"));
            jsonContent = data.toJSONString();
        }catch(Exception e){
            LogsManager.error("JsonReader Error", e.getMessage());
        }
    }
    public String getJsonValue(String jsonPath){
        try{
            return JsonPath.read(jsonContent, jsonPath);
        }catch(Exception e){
            LogsManager.error("JsonReader Error", e.getMessage());
                    return "{}";
        }

    }
}
