package prodyogic.findmeparking.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParkSpaceItem {
    private JSONObject obj;

    public ParkSpaceItem(JSONObject obj) {
        this.obj = obj;
    }

    public static ArrayList<ParkSpaceItem> getList(JSONArray arr) {
        ArrayList<ParkSpaceItem> items = new ArrayList();
        for (int i = 0; i < arr.length(); i++) {
            try {
                ParkSpaceItem item = new ParkSpaceItem(arr.getJSONObject(i));
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

    public String getCountry() {
        try {
            return obj.getString("country");
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
        return obj.has("name") && obj.has("id") && obj.has("country");
    }
}
