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

public class Main extends ActionBarActivity {

    private EditText searchField;
    private TextView message;
    private Button searchButton;

//    /*
//      In Android, localhost is the address of the device or the emulator.
//      To connect to your machine, you need to use the below IP address
//     */
//    //https://localhost:8443/ZotRides/
//    private final String baseURL = BackendServerConn.getURL();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* display the login page */
        // upon creation, inflate and initialize the layout
        setContentView(R.layout.main);
        searchField = findViewById(R.id.search_field);
        message = findViewById(R.id.message);
        searchButton = findViewById(R.id.search);

        //assign a listener to call a function to handle the user request when clicking a button
        searchButton.setOnClickListener(view -> viewResults());
    }

    public void viewResults() {
        Intent listPage = new Intent(Main.this, ListViewActivity.class);
        // activate the list page.
        System.out.println("passing: " + searchField.getText());
        listPage.putExtra("token", searchField.getText().toString());
        startActivity(listPage);
    }

//    /* send request to server */
//    public void search() {
//
//        message.setText("Trying to perform full-text search");
//        // use the same network queue across our application
//        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
//        // request type is POST
//        final StringRequest searchRequest = new StringRequest(
//                Request.Method.POST,
//                baseURL + "/api/full-text-search",
//                response -> {
//                    // TODO: should parse the json response to redirect to appropriate functions
//                    //  upon different response value.
//                    Log.d("main.success", response);
//                    //TODO : CONNECT TO MOVIE LIST PAGE
//
//                    // initialize the activity(page)/destination
//                    Intent listPage = new Intent(Main.this, ListViewActivity.class);
//                    // activate the list page.
//                    startActivity(listPage);
//                },
//                error -> {
//                    // error
//                    Log.d("main.error", error.toString());
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // POST request form data
//                final Map<String, String> params = new HashMap<>();
//                params.put("token", searchField.getText().toString());
//
//                return params;
//            }
//        };
//
//        // important: queue.add is where the login request is actually sent
//        queue.add(searchRequest);
//
//    }
}