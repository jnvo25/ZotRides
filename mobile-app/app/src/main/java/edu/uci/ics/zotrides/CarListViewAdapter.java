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

public class CarListViewAdapter extends ArrayAdapter<Car> {
    private final ArrayList<Car> cars;

    public CarListViewAdapter(ArrayList<Car> cars, Context context) {
        super(context, R.layout.row, cars);
        this.cars = cars;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row, parent, false);

        Car car = cars.get(position);

        TextView titleView = view.findViewById(R.id.title);
        TextView categoryView = view.findViewById(R.id.category);
        TextView pickup1View = view.findViewById(R.id.pickup1);
        TextView pickup2View = view.findViewById(R.id.pickup2);
        TextView pickup3View = view.findViewById(R.id.pickup3);
        TextView ratingView = view.findViewById(R.id.rating);

        titleView.setText(car.getTitle());
        categoryView.setText(car.getCategory());
        pickup1View.setText(car.getPickup1());
        pickup2View.setText(car.getPickup2());
        pickup3View.setText(car.getPickup3());
        ratingView.setText(car.getRating() + ""); // cast double to String

        return view;
    }
}