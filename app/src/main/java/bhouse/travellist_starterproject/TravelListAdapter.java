package bhouse.travellist_starterproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by bearg on 5/28/2016.
 */
public class TravelListAdapter extends RecyclerView.Adapter<TravelListAdapter.ViewHolder> {

    Context mContext;

    public TravelListAdapter(Context context) {
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout placeHolder;
        private LinearLayout placeNameHolder;
        private TextView placeName;
        private ImageView placeImage;

        private ViewHolder(View itemView) {
            super(itemView);

            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            placeImage = (ImageView) itemView.findViewById(R.id.placeImage);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_places, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Place place = PlaceData.placeList().get(position);
        holder.placeName.setText(place.name);

        Picasso.with(mContext).load(place.getImageResourceId(mContext))
                .into(holder.placeImage);
    }

    @Override
    public int getItemCount() {
        return PlaceData.placeList().size();
    }
}
