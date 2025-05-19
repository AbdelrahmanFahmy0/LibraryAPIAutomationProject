package Models.Households;

import org.json.simple.JSONObject;

public class UpdateHouseholdBody {

    public static String getUpdateHouseholdBody(String name) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", name);
        return requestBody.toJSONString();
    }
}
