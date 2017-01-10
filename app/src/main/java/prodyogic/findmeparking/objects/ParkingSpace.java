package prodyogic.findmeparking.objects;

import org.json.JSONException;
import org.json.JSONObject;

public class ParkingSpace {
    JSONObject obj;

    public ParkingSpace(String name, String description, String country){
        obj=new JSONObject();
        try {
            obj.put("name",name);
            obj.put("description",description);
            obj.put("country",country);
        } catch (JSONException e) {

        }
    }
    public ParkingSpace(JSONObject json){
        this.obj=json;
    }
    public ParkingSpace(String jsonString){
        try {
            this.obj=new JSONObject(jsonString);

        } catch (JSONException e) {

        }
    }
    public String getName(){
        try {
            return obj.getString("name");
        } catch (JSONException e) {
          return null;
        }
    }
    public String getDescription(){
        try {
            return obj.getString("description");
        } catch (JSONException e) {
            return null;
        }
    }
    public String getCountry(){
        try {
            return obj.getString("country");
        } catch (JSONException e) {
            return null;
        }
    }
    public int getInt(){
        try {
            return obj.getInt("id");
        } catch (JSONException e) {
           return -1;
        }
    }
    public boolean check(){
        return obj.has("name") && obj.has("description") && obj.has("country");
    }
    public String getJSONString(){
        return obj.toString();
    }
}
