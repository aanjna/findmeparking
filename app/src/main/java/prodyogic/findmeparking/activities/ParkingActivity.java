package prodyogic.findmeparking.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import prodyogic.findmeparking.R;
import prodyogic.findmeparking.includes.ApiClient;
import prodyogic.findmeparking.objects.ParkingSpace;

public class ParkingActivity extends AppCompatActivity {
    int userId;
    int parkingId;
    ProgressDialog prog;

    private static String getJSONRating(int userId, int parkingId, int rating) {
        return "{\"user_id\":" + userId + ",\"parking_id\":" + parkingId + ",\"rating\":" + rating + "}";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
        Intent intent = getIntent();
        this.parkingId = intent.getIntExtra("parking_id", -1);
        this.userId = MainActivity.user.getId();
        if (parkingId > 0) {
            getparking();
            getparkingRating();
            if (userId > 0) getUserRating();
        }
        addListenerOnRatingBar();
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

    private void getparkingRating() {
        ApiClient api = new ApiClient() {
            @Override
            protected void finished(int code, String callResult) {

                if (code == 200) {
                    JSONArray arr = null;
                    try {
                        arr = new JSONArray(callResult);
                        if (arr.length() > 0) {
                            JSONObject res = arr.getJSONObject(0);

                            Double rating = res.getDouble("avg_rating");
                            String ratingstr = String.format("%.2f", rating);
                            TextView ratingtext = (TextView) findViewById(R.id.parking_rating);
                            ratingtext.setText("Rating:" + ratingstr);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == 204) {
                    TextView ratingtext = (TextView) findViewById(R.id.parking_rating);
                    ratingtext.setText("No ratings");
                } else {

                }

            }
        };
        api.requestGET("http://findmyparking-demiurgosoft.rhcloud.com/parking/rating/" + parkingId);

    }

    private void getUserRating() {

        ApiClient api = new ApiClient() {
            @Override
            protected void finished(int code, String callResult) {

                if (code == 200) {
                    JSONArray arr;
                    try {
                        arr = new JSONArray(callResult);
                        if (arr.length() > 0) {
                            JSONObject res = arr.getJSONObject(0);
                            int rating = res.getInt("rating");
                            RatingBar ratingBar = (RatingBar) findViewById(R.id.user_rating);
                            float rat = (float) ((float) rating / 2.0);
                            ratingBar.setRating(rat);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == 204) {

                } else {

                }

            }
        };
        api.requestGET("http://findmyparking-demiurgosoft.rhcloud.com/rating/" + userId + "/" + parkingId);

    }

    private void getparking() {
        prog = ProgressDialog.show(this, "Loading", "Loading parkings", false, false); //creates the progress dialog

        ApiClient api = new ApiClient() {
            @Override
            protected void finished(int code, String callResult) {
                prog.dismiss();
                prog = null;
                TextView warningtext = (TextView) findViewById(R.id.parking_name_text);
                if (code == 200) {
                    JSONArray arr;
                    try {
                        arr = new JSONArray(callResult);
                        if (arr.length() <= 0) warningtext.setText("problem with retrieved data");
                        else {
                            setparkingInfo(new ParkingSpace(arr.getString(0)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == 204) {
                    warningtext.setText("No parking found");
                } else {
                    warningtext.setText("Server problem");
                }

            }
        };
      //  api.requestGET("http://findmyparking-demiurgosoft.rhcloud.com/parking/" + parkingId);
    }

    private void postRating(int rating) {
        ApiClient api = new ApiClient() {
            @Override
            protected void finished(int code, String callResult) {
                String warningText = null;
                if (code == 201) warningText = "Rating update";
                else if (code == 409) warningText = "Problem updating rating";
                else warningText = "Server Error";
                if (warningText != null)
                    Toast.makeText(getApplicationContext(), warningText, Toast.LENGTH_LONG).show();
            }
        };
        Log.d("rating", ParkingActivity.getJSONRating(userId, parkingId, rating));
        api.requestPOST("http://findmyparking-demiurgosoft.rhcloud.com/rating", ParkingActivity.getJSONRating(userId, parkingId, rating));
    }

    private void setparkingInfo(ParkingSpace parkingSpace) {
        TextView nametext = (TextView) findViewById(R.id.parking_name_text);
        TextView descriptiontext = (TextView) findViewById(R.id.description_text);
        nametext.setText(parkingSpace.getName());
        descriptiontext.setText("Country: " + parkingSpace.getCountry() + "\nDESCRIPTION\n" + parkingSpace.getDescription());
    }

    private void addListenerOnRatingBar() {

        RatingBar ratingBar = (RatingBar) findViewById(R.id.user_rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if (fromUser) {
                    int rat = (int) (rating * 2);
                    postRating(rat);
                    getparkingRating();
                    getUserRating();
                }
            }
        });
    }
}

