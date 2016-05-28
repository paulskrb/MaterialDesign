package bhouse.travellist_starterproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
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

    private Context mContext;

    // ViewHolder class has an OnClick method that will call
    // mItemClickListener.onItemClick(View, int) method defined
    // in onCreate of MainActivity. The MainActivity class sets
    // the value of mItemClickListener to be its listener instance
    // using the setter method we provide.
    private OnItemClickListener mItemClickListener;

    // MainActivity uses this interface and defines an onItemClick method
    // and sets its TravelListAdapter to use this listener
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public TravelListAdapter(Context context) {
        mContext = context;
    }

    // not going to use this ViewHolder outside this class, so we can
    // make it non-static instead of static. also allows us to make the
    // adapter's onItemClickListener non-static
    protected class ViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

        private LinearLayout placeHolder;
        private LinearLayout placeNameHolder;
        private TextView placeName;
        private ImageView placeImage;

        private ViewHolder(View itemView) {
            super(itemView);

            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            placeHolder.setOnClickListener(this);

            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            placeImage = (ImageView) itemView.findViewById(R.id.placeImage);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {

                mItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_places, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Place place = PlaceData.placeList().get(position);
        holder.placeName.setText(place.name);

        Picasso.with(mContext).load(place.getImageResourceId(mContext))
                .into(holder.placeImage);

        Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(),
                place.getImageResourceId(mContext));

        // generates a color for the background of our LinearLayout that
        // holds to text view, or black if the color doesn't exist
        Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int bgColor =
                        palette.getLightVibrantColor(mContext.getResources()
                                .getColor(android.R.color.black));
                holder.placeNameHolder.setBackgroundColor(bgColor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PlaceData.placeList().size();
    }
}
