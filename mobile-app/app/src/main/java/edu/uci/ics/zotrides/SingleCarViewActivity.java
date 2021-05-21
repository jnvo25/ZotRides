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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SingleCarViewActivity extends Activity {
    private TextView title;
    private TextView category;
    private TextView rating;
    private String id;

    /*
      In Android, localhost is the address of the device or the emulator.
      To connect to your machine, you need to use the below IP address
     */
    //https://localhost:8443/ZotRides/
    private final String baseURL = BackendServerConn.getURL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlecar);
        title = findViewById(R.id.title);
        category = findViewById(R.id.category);
        rating = findViewById(R.id.rating);

        // access car id passed by Intent
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        System.out.println("receiving: " + id);

        // retrieve results from backend server
        final ArrayList<PickupLocation> locations = new ArrayList<>();
        search(locations);

//        // bind event handlers
//        prevButton.setOnClickListener(view -> prevPage(cars));
//        nextButton.setOnClickListener(view -> nextPage(cars));
    }

    /* send request to server */
    public void search(ArrayList<PickupLocation> locations) {
//        message.setText("Trying to perform single car search");

        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest searchRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/single-car?id=" + id,
                response -> {
                    try {
                        JSONArray results = new JSONArray(response);
                        JSONObject result;
                        String[] addresses; String[] phones; String[] loc_ids;
                        String tempID; String tempAddr; String tempPhone;
                        if (results.length() == 1) {
                            result = results.getJSONObject(0);
                            title.setText(result.getString("car_name"));
                            category.setText(result.getString("car_category"));
                            rating.setText(result.getDouble("car_rating") + "");

                            loc_ids = result.getString("location_ids").split(";");
                            addresses = result.getString("location_address").split(";");
                            phones = result.getString("location_phone").split("<br>");
                            for (int i = 0; i < addresses.length; ++i) {
                                tempID = loc_ids[i];
                                tempAddr = addresses[i];
                                tempPhone = (i < phones.length ? phones[i] : "");
                                locations.add(new PickupLocation(tempID, tempAddr, tempPhone));
                            }
                        } else {
                            Log.d("SingleCarViewActivity.0", "error parsing JSON results");
                            return;
                        }
                    } catch (JSONException e) {
                        Log.d("SingleCarViewActivity.0", "error parsing JSON results");
                    }

                    System.out.println("query: " + locations.toString());
                    Log.d("SingleCarViewActivity.1", response);

                    /* adapter object used to display list */
                    SingleCarListViewAdapter adapter = new SingleCarListViewAdapter(locations, this);

                    ListView listView = findViewById(R.id.pickup_list); // list id corresponds to UI's list id in singlecar.xml
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        // TODO : OPTIONALLY CONNECT TO SINGLE LOCATION PAGE
                        PickupLocation location = locations.get(position);
                        String message = String.format("Clicked on position: %d, title: %s", position, location.getAddress());
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        /* Toast class to display message */
                    });


//                    // initialize the activity(page)/destination
//                    Intent listPage = new Intent(Main.this, ListViewActivity.class);
//                    // activate the list page.
//                    startActivity(listPage);
                },
                error -> {
                    // error
                    Log.d("SingleCarViewActivity.0", error.toString());
                });

        // important: queue.add is where the login request is actually sent
        queue.add(searchRequest);
    }


}