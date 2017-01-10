package prodyogic.findmeparking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import prodyogic.findmeparking.R;
import prodyogic.findmeparking.includes.ApiClient;
import prodyogic.findmeparking.objects.User;


public class MainActivity extends AppCompatActivity {
    public static User user;
    private String pass;

    //  example.requestPOST("http://findmybeer-demiurgosoft.rhcloud.com/beer", "{\"name\":\"fromAndroid\",\"description\":\"fine android beer\",\"country\":\"spain2\"}");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        TextView warningtext = (TextView) findViewById(R.id.warningText);
        warningtext.setText("");
        EditText userText = (EditText) findViewById(R.id.username_text);
        String username = userText.getText().toString();
        EditText passText = (EditText) findViewById(R.id.pasword_text);
        String pass = passText.getText().toString();

        switch(view.getId()) {
            case R.id.login_button:
                startDash();
                // logIn(username, pass);
                break;
            case R.id.signin_button:
                signIn(username,pass);
                break;

        }
    }
    private void signIn(String username,String pass){
        setButtons(false);
        this.pass = pass;
        User user=new User(username,pass);
        ApiClient api=new ApiClient(){
            @Override
            protected void finished(int code, String callResult) {
                String warningText = null;
                if (code == 201) warningText = "User created correctly, please log in";
                else if (code == 409) warningText = "User already exists";
                else warningText = "Error creating user";
                setButtons(true);
                if (warningText != null)
                    Toast.makeText(getApplicationContext(), warningText, Toast.LENGTH_LONG).show();
            }
        };
        // api.requestPOST("http://findmybeer-demiurgosoft.rhcloud.com/user", user.getJSONString());

    }

    private void logIn(String username, String pass) {
        setButtons(false);
        this.pass = pass;
        if (this.pass == null) this.pass = "";
        ApiClient api = new ApiClient() {
            @Override
            protected void finished(int code, String callResult) {
                String warningText = null;
                if (code == 200) {
                    try {
                        JSONArray arr = new JSONArray(callResult);

                        if (arr.length() < 0) warningText = "User not valid";
                        else {
                            String userdata = arr.getString(0);
                            if (userdata == null || userdata.isEmpty())
                                warningText = "Problem with received data";
                            else {
                                User user = new User(userdata);
                                if (checkPass(user.getPassword())) {
                                    MainActivity.user = user;
                                    startDash();
                                }
                                else warningText = "Password not valid";
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (code == 204)
                    warningText = "User not valid";
                else
                    warningText = "Problem loggin in";

                setButtons(true);
                if (warningText != null)
                    Toast.makeText(getApplicationContext(), warningText, Toast.LENGTH_LONG).show();

            }
        };
        //api.requestGET("http://findmybeer-demiurgosoft.rhcloud.com/user/name/" + username);
    }

    private void startDash() {
        Intent intent = new Intent(MainActivity.this, UserDash.class);
        startActivity(intent);
    }

    private void setButtons(boolean active) {
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button signinButton = (Button) findViewById(R.id.signin_button);
        loginButton.setEnabled(active);
        signinButton.setEnabled(active);
    }

    private boolean checkPass(String password) {
        return password.equals(this.pass);
    }

}
