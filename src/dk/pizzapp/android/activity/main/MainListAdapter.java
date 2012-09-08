package dk.pizzapp.android.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.androidquery.AQuery;
import dk.pizzapp.android.App;
import dk.pizzapp.android.R;
import dk.pizzapp.android.activity.info.InfoActivity;
import dk.pizzapp.android.data.Response;
import dk.pizzapp.android.data.Restaurant;

public class MainListAdapter extends ArrayAdapter<Response> {
    private Activity activity;

    public MainListAdapter(Activity activity) {
        super(activity, R.layout.list_item_main);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return App.visibleRestaurants.size();
    }

    static class ViewHolder {
        int id;
        ImageView icon;
        TextView name;
        TextView address;
        TextView distance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.list_item_main, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.list_item_icon);
            holder.name = (TextView) convertView.findViewById(R.id.list_item_name);
            holder.address = (TextView) convertView.findViewById(R.id.list_item_address);
            holder.distance = (TextView) convertView.findViewById(R.id.list_item_distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Restaurant restaurant = App.visibleRestaurants.get(position);
        AQuery aq = new AQuery(convertView);
        holder.id = position;
        aq.id(holder.name).text(restaurant.getName());
        aq.id(holder.address).text(restaurant.getAddress());
        aq.id(holder.icon).image("http://pizzapi.dk/display/" + restaurant.getId(), true, true, 0, R.drawable.icon, aq.getCachedImage(R.drawable.icon), AQuery.FADE_IN_NETWORK);

        if ((int) restaurant.getDistance() < 1000)
            aq.id(holder.distance).text((int) restaurant.getDistance() + " m");
        else {
            aq.id(holder.distance).text((int) restaurant.getDistance() / 1000 + " km");
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                App.restaurant = App.visibleRestaurants.get(viewHolder.id);
                activity.startActivity(new Intent(activity, InfoActivity.class));
            }
        });

        return convertView;
    }
}
