package prodyogic.findmeparking.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SpaceDash {
    private JSONObject obj;

    public SpaceDash(JSONObject obj) {
        this.obj = obj;
    }

    public static ArrayList<SpaceDash> getList(JSONArray arr) {
        ArrayList<SpaceDash> items = new ArrayList();
        for (int i = 0; i < arr.length(); i++) {
            try {
                SpaceDash item = new SpaceDash(arr.getJSONObject(i));
                if (item.check()) {
                    items.add(item);
                }
            } catch (JSONException e) {

            }
        }

        return items;
    }

    public String getName() {
        try {
            return obj.getString("name");
        } catch (JSONException e) {
            return null;
        }
    }

    public String getRating() {
        try {
            return obj.getString("rating");
        } catch (JSONException e) {
            return null;
        }
    }

    public int getId() {
        try {
            return obj.getInt("id");
        } catch (JSONException e) {
            return -1;
        }
    }

    public boolean check() {
        return obj.has("name") && obj.has("id") && obj.has("rating");
    }
}
