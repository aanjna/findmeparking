package prodyogic.findmeparking.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import prodyogic.findmeparking.R;
import prodyogic.findmeparking.includes.ApiClient;
import prodyogic.findmeparking.objects.ParkingAdapter;
import prodyogic.findmeparking.objects.ParkSpaceItem;


public class ParkingSearchActivity extends AppCompatActivity {
    ListView parkingList;
    int userId;
    ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_search);
//        this.userId = MainActivity.user.getId();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonClicked(View view) {
        EditText searchField = (EditText) findViewById(R.id.searchText);
        String text = searchField.getText().toString();
        if (text != null && text.length() > 2) {
            search(text);

        }
    }

    private void setList(ArrayList<ParkSpaceItem> items) {
        parkingList = (ListView) findViewById(R.id.parkingResults);

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);*/
   /* ArrayAdapter<ParkSpaceItem> adapter=new ArrayAdapter<ParkSpaceItem>(this,
            android.R.layout.simple_list_item_2, android.R.id.text1,items);*/
        ParkingAdapter adapter = new ParkingAdapter(this, items);

        // Assign adapter to ListView
        parkingList.setAdapter(adapter);
        parkingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                ParkSpaceItem itemValue = (ParkSpaceItem) parkingList.getItemAtPosition(position);
                int parkingId = itemValue.getId();

                showPage(parkingId);

            }

        });
    }

    private void search(String text) {
        prog = ProgressDialog.show(this, "Loading", "Loading parkings", false, false); //creates the progress dialog

        ApiClient api = new ApiClient() {
            @Override
            protected void finished(int code, String callResult) {
                prog.dismiss();

                prog = null;

                if (code == 200) {
                    JSONArray arr = null;
                    try {
                        arr = new JSONArray(callResult);
                        if (arr.length() > 0) {
                            ArrayList<ParkSpaceItem> items = ParkSpaceItem.getList(arr);
                            if (!items.isEmpty()) setList(items);
                            else {
                                Toast.makeText(getApplicationContext(), "No parkings found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Fatal Error", Toast.LENGTH_SHORT).show();
                    }
                } else if (code == 204) {
                    Toast.makeText(getApplicationContext(), "No parkings found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Connection problem", Toast.LENGTH_SHORT).show();
                }

            }
        };
       // api.requestGET("http://findmybeer-demiurgosoft.rhcloud.com/beers/" + text);
    }

    private void showPage(int parkingId) {
        if (parkingId > 0) {
            Intent intent = new Intent(this, ParkingActivity.class);

            intent.putExtra("parking_id", parkingId);
            startActivity(intent);
        }
    }

}
