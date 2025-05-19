package Models.Households;

import org.json.simple.JSONObject;

public class CreateHouseholdBody {

    public static String getCreateHouseholdBody(String name) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", name);
        return requestBody.toJSONString();
    }
}
