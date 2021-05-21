package edu.uci.ics.zotrides;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/* helper class to show list of movies */

public class SingleCarListViewAdapter extends ArrayAdapter<PickupLocation> {
    private final ArrayList<PickupLocation> locations;

    public SingleCarListViewAdapter(ArrayList<PickupLocation> locations, Context context) {
        super(context, R.layout.row, locations);
        this.locations = locations;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.singlecarrow, parent, false);

        PickupLocation location = locations.get(position);

        TextView address = view.findViewById(R.id.address);
        TextView phone = view.findViewById(R.id.phone);

        address.setText(location.getAddress());
        phone.setText(location.getPhone() == null ? "" : location.getPhone());
        return view;
    }
}