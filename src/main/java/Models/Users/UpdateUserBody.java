package Models.Users;

import org.json.simple.JSONObject;

public class UpdateUserBody {

    public static String getUpdateUserBody(String firstName, String lastName, String email) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        requestBody.put("email", email);
        return requestBody.toJSONString();
    }

    public static String getPartialUpdateUserBody(String email) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        return requestBody.toJSONString();
    }
}
