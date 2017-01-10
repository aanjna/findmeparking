package prodyogic.findmeparking.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import prodyogic.findmeparking.R;
import prodyogic.findmeparking.includes.ApiClient;
import prodyogic.findmeparking.objects.ParkingSpace;


public class AddParkingSpace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addparkingspace);
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
        setButtonActive(false);
        EditText place = (EditText) findViewById(R.id.nameInput);
        EditText area = (EditText) findViewById(R.id.countryInput);
        EditText description = (EditText) findViewById(R.id.descriptionInput);
        ParkingSpace parkspace = new ParkingSpace(place.getText().toString(), description.getText().toString(), area.getText().toString());
        if (parkspace.check()) {
            ApiClient api = new ApiClient() {
                @Override
                protected void finished(int code, String callResult) {
                    String result = null;
                    if (code == 201) result = "ParkingSpace added";
                    else if (code == 409) result = "ParkingSpace already exists";
                    else result = "Error Creating parkspace";

                    if (result != null)
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    setButtonActive(true);
                }
            };
//            api.requestPOST("http://findmybeer-demiurgosoft.rhcloud.com/beer", beer.getJSONString());

        }
    }

    private void setButtonActive(boolean active) {
        Button button = (Button) findViewById(R.id.addButton);
        button.setEnabled(active);

    }
}
