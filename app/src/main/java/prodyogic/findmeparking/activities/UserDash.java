package prodyogic.findmeparking.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import prodyogic.findmeparking.R;
import prodyogic.findmeparking.includes.ApiClient;
import prodyogic.findmeparking.objects.SpaceDash;
import prodyogic.findmeparking.objects.SpaceDashAdapter;
import prodyogic.findmeparking.objects.User;

public class UserDash extends AppCompatActivity {

    User user;
    ListView parkingList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash);
        this.user = MainActivity.user;
        updateparkings();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        this.user = MainActivity.user;
        updateparkings();
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


        switch (view.getId()) {


            case R.id.addparkingButton:
                addparking();
                break;
            case R.id.searchparkingButton:
                Intent intent = new Intent(this, ParkingSearchActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void addparking() {
        Intent intent = new Intent(this, AddParkingSpace.class);
        startActivity(intent);

    }

    private void updateparkings() {
        ProgressBar prog = (ProgressBar) findViewById(R.id.loadingBar);
//        prog.setVisibility(View.VISIBLE);
        TextView warning = (TextView) findViewById(R.id.warningText);
        warning.setText("");


        ApiClient api = new ApiClient() {
            @Override
            protected void finished(int code, String callResult) {
                ProgressBar prog = (ProgressBar) findViewById(R.id.loadingBar);
                prog.setVisibility(View.INVISIBLE);
                TextView warning = (TextView) findViewById(R.id.warningText);
                if (code == 200) {
                    JSONArray arr = null;
                    try {
                        arr = new JSONArray(callResult);
                        if (arr.length() > 0) {
                            ArrayList<SpaceDash> items = SpaceDash.getList(arr);
                            if (!items.isEmpty()) setList(items);
                            else {
                                warning.setText("You have no parkings");
                            }
                        }
                    } catch (JSONException e) {
                        warning.setText("Fatal Error");
                    }
                } else if (code == 204) {
                    warning.setText("You have no parkings");
                } else {
                    warning.setText("Connection Problems");
                }

            }
        };
//        api.requestGET("http://findmyparking-demiurgosoft.rhcloud.com/ratings/user/" + user.getId());
    }

    private void setList(ArrayList<SpaceDash> items) {
        parkingList = (ListView) findViewById(R.id.parkingList);


        SpaceDashAdapter adapter = new SpaceDashAdapter(this, items);

        // Assign adapter to ListView
        parkingList.setAdapter(adapter);
        parkingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                SpaceDash itemValue = (SpaceDash) parkingList.getItemAtPosition(position);
                int parkingId = itemValue.getId();

                showPage(parkingId);

            }

        });
    }

    private void showPage(int parkingId) {
        if (parkingId > 0) {
            Intent intent = new Intent(this, ParkingActivity.class);

            intent.putExtra("parking_id", parkingId);
            startActivity(intent);
        }
    }
}

