package prodyogic.findmeparking.objects;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    int id=-1;
    String username;
    String password;

    public User(String username,String pass){
        this.username=username;
        this.password=pass;
    }

    public User(String username, String pass, int id) {
        this.username = username;
        this.password = pass;
        this.id = id;
    }
    public User(JSONObject json){
        decodeJSON(json);
    }
    public User(String jsonString){
        try {
            decodeJSON(new JSONObject(jsonString));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getName(){
        return username;
    }
    public String getPassword(){
       return password;
    }

    public int getId() {
        return id;
    }

    public boolean check(){
        return (username != null && username.length() > 0) && (password != null && password.length() > 0) && (id >= 0);
    }
    public String getJSONString(){
        return encodeJSON().toString();
    }
    private void decodeJSON(JSONObject json){
        try {
            id=json.getInt("id");
            username=json.getString("name");
            password=json.getString("password");
        } catch (JSONException e) {

        }
    }
    private JSONObject encodeJSON(){
        JSONObject res=new JSONObject();
        try {
            if (id >= 1) res.put("id", id);
            res.put("name",username);
            res.put("password",password);
        } catch (JSONException e) {
        }

        return res;
    }
}
