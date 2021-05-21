package edu.uci.ics.zotrides;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class ListViewActivity extends Activity {
    private Button prevButton;
    private Button nextButton;
    private TextView paginationMessage;
    private String token;

    /*
      In Android, localhost is the address of the device or the emulator.
      To connect to your machine, you need to use the below IP address
     */
    //https://localhost:8443/ZotRides/
    private final String host = "10.0.2.2"; // IP address for localhost
    private final String port = "8443";
    private final String domain = "ZotRides";
    private final String baseURL = "https://" + host + ":" + port + "/" + domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        prevButton = findViewById(R.id.prev);
        nextButton = findViewById(R.id.next);
        paginationMessage = findViewById(R.id.pagination_message);

        // access token passed by Intent
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        System.out.println("receiving: " + token);

        // retrieve results from backend server
        final ArrayList<Car> cars = new ArrayList<>();
        search(cars);

        // bind event handlers
        prevButton.setOnClickListener(view -> prevPage(cars));
        nextButton.setOnClickListener(view -> nextPage(cars));
    }

    /* send request to server */
    public void search(ArrayList<Car> cars) {
//        message.setText("Trying to perform full-text search");

        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest searchRequest = new StringRequest(
                Request.Method.POST,
                baseURL + "/api/full-text-search",
                response -> {
                    try {
                        JSONObject result = new JSONObject(response);
                        JSONArray results = result.getJSONArray("results");
                        JSONObject temp;
                        String id; String title; String cat; String[] temp_pickups; String[] pickups = new String[3]; double rating;
                        for (int i = 0; i < results.length() && i < 20; ++i) {
                            temp = results.getJSONObject(i);
                            id = temp.getString("car_id");
                            title = temp.getString("car_name");
                            cat = temp.getString("car_category");
                            rating = temp.getDouble("car_rating");

                            temp_pickups = temp.getString("location_address").split(";");
                            for (int j = 0; j < temp_pickups.length; ++j) {
                                pickups[j] = temp_pickups[j];
                            }
                            for (int j = temp_pickups.length; j < 3; ++j) {
                                pickups[j] = "";
                            }

                            cars.add(new Car(id, title, cat, pickups[0], pickups[1], pickups[2], rating));
                        }

                        paginationMessage.setText(result.getString("message"));
                    } catch (JSONException e) {
                        Log.d("ListViewActivity.nay", "error parsing JSON results");
                    }

                    System.out.println("query: " + cars.toString());
                    Log.d("ListViewActivity.yay", response);

                    /* adapter object used to display list */
                    CarListViewAdapter adapter = new CarListViewAdapter(cars, this);

                    ListView listView = findViewById(R.id.list); // list id corresponds to UI's list id in listview.xml
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        // TODO : CONNECT TO SINGLE CAR PAGE
                        Car car = cars.get(position);
                        String message = String.format("Clicked on position: %d, title: %s", position, car.getTitle());
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        /* Toast class to display message */

                        Intent singleCarPage = new Intent(ListViewActivity.this, SingleCarViewActivity.class);
                        // activate the list page.
                        System.out.println("passing: " + car.getID());
                        singleCarPage.putExtra("id", car.getID().toString());
                        startActivity(singleCarPage);
                    });
                },
                error -> {
                    // error
                    Log.d("ListViewActivity.naynay", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // POST request form data
                final Map<String, String> params = new HashMap<>();
//                params.put("token", searchField.getText().toString());
                params.put("token", token);

                return params;
            }
        };

        // important: queue.add is where the login request is actually sent
        queue.add(searchRequest);

        // -------------------------------
    }

    /* send request to server */
    public void nextPage(ArrayList<Car> cars) {
//        message.setText("Trying to perform pagination");
        // reset cars
        cars.clear();

        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest searchRequest = new StringRequest(
            Request.Method.POST,
            baseURL + "/api/paginate",
            response -> {
                try {
                    JSONObject result = new JSONObject(response);
                    JSONArray results = result.getJSONArray("results");
                    JSONObject temp;
                    String id; String title; String cat; String[] temp_pickups; String[] pickups = new String[3]; double rating;
                    for (int i = 0; i < results.length() && i < 20; ++i) {
                        temp = results.getJSONObject(i);
                        id = temp.getString("car_id");
                        title = temp.getString("car_name");
                        cat = temp.getString("car_category");
                        rating = temp.getDouble("car_rating");

                        temp_pickups = temp.getString("location_address").split(";");
                        for (int j = 0; j < temp_pickups.length; ++j) {
                            pickups[j] = temp_pickups[j];
                        }
                        for (int j = temp_pickups.length; j < 3; ++j) {
                            pickups[j] = "";
                        }

                        cars.add(new Car(id, title, cat, pickups[0], pickups[1], pickups[2], rating));
                    }

                    paginationMessage.setText(result.getString("message"));
                } catch (JSONException e) {
                    Log.d("ListViewActivity.nay", "error parsing JSON results");
                }

                System.out.println("query: " + cars.toString());
                Log.d("ListViewActivity.yay", response);

                /* adapter object used to display list */
                CarListViewAdapter adapter = new CarListViewAdapter(cars, this);

                ListView listView = findViewById(R.id.list); // list id corresponds to UI's list id in listview.xml
                listView.setAdapter(adapter);
            },
            error -> {
                // error
                Log.d("ListViewActivity.naynay", error.toString());
            }) {
            @Override
            protected Map<String, String> getParams() {
                // POST request form data
                final Map<String, String> params = new HashMap<>();
                params.put("pageStatus", "1");

                return params;
            }
        };

        // important: queue.add is where the login request is actually sent
        queue.add(searchRequest);

        // -------------------------------
    }

    /* send request to server */
    public void prevPage(ArrayList<Car> cars) {
//        message.setText("Trying to perform pagination");

        cars.clear();

        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest searchRequest = new StringRequest(
                Request.Method.POST,
                baseURL + "/api/paginate",
                response -> {
                    try {
                        JSONObject result = new JSONObject(response);
                        JSONArray results = result.getJSONArray("results");
                        JSONObject temp;
                        String id; String title; String cat; String[] temp_pickups; String[] pickups = new String[3]; double rating;
                        for (int i = 0; i < results.length() && i < 20; ++i) {
                            temp = results.getJSONObject(i);
                            id = temp.getString("car_id");
                            title = temp.getString("car_name");
                            cat = temp.getString("car_category");
                            rating = temp.getDouble("car_rating");

                            temp_pickups = temp.getString("location_address").split(";");
                            for (int j = 0; j < temp_pickups.length; ++j) {
                                pickups[j] = temp_pickups[j];
                            }
                            for (int j = temp_pickups.length; j < 3; ++j) {
                                pickups[j] = "";
                            }

                            cars.add(new Car(id, title, cat, pickups[0], pickups[1], pickups[2], rating));
                        }

                        paginationMessage.setText(result.getString("message"));
                    } catch (JSONException e) {
                        Log.d("ListViewActivity.nay", "error parsing JSON results");
                    }

                    System.out.println("query: " + cars.toString());
                    Log.d("ListViewActivity.yay", response);

                    /* adapter object used to display list */
                    CarListViewAdapter adapter = new CarListViewAdapter(cars, this);

                    ListView listView = findViewById(R.id.list); // list id corresponds to UI's list id in listview.xml
                    listView.setAdapter(adapter);
                },
                error -> {
                    // error
                    Log.d("ListViewActivity.naynay", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // POST request form data
                final Map<String, String> params = new HashMap<>();
                params.put("pageStatus", "-1");

                return params;
            }
        };

        // important: queue.add is where the login request is actually sent
        queue.add(searchRequest);

        // -------------------------------
    }
}