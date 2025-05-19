package Models.Users;

import org.json.simple.JSONObject;

public class CreateUserBody {

    public static String getCreateUserBody(String firstName, String lastName, String email) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        requestBody.put("email", email);
        return requestBody.toJSONString();
    }
}
