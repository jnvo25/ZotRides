package edu.uci.ics.zotrides;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends ActionBarActivity {

    private EditText username;
    private EditText password;
    private TextView message;
    private Button loginButton;

    /*
      In Android, localhost is the address of the device or the emulator.
      To connect to your machine, you need to use the below IP address
     */
    //https://localhost:8443/ZotRides/
    private final String baseURL = BackendServerConn.getURL();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* display the login page */
        // upon creation, inflate and initialize the layout
        setContentView(R.layout.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        message = findViewById(R.id.message);
        loginButton = findViewById(R.id.login);

        //assign a listener to call a function to handle the user request when clicking a button
        loginButton.setOnClickListener(view -> login());
    }

    /* send request to server */
    public void login() {

        message.setText("Trying to login");
        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest loginRequest = new StringRequest(
                Request.Method.POST,
                baseURL + "/api/app-login",
                response -> {

                    // verify credentials
                    try {
                        JSONObject result = new JSONObject(response);
                        String status = result.getString("status");
                        String loginMessage = result.getString("message");

                        message.setText(loginMessage);

                        // valid
                        if (status.equals("success")) {
                            Log.d("login.success", response);

                            // initialize the activity(page)/destination
                            Intent listPage = new Intent(Login.this, Main.class);
                            // activate the list page.
                            startActivity(listPage);
                        }

                        // invalid - don't do anything
                    }
                    catch (JSONException e) {
                        Log.d("login.error", "error parsing JSON results");
                        message.setText("JSON parsing error");
                    }
                },
                error -> {
                    // error
                    Log.d("login.error", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // POST request form data
                final Map<String, String> params = new HashMap<>();
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());

                return params;
            }
        };

        // important: queue.add is where the login request is actually sent
        queue.add(loginRequest);

    }
}