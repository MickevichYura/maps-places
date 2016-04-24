package mickevichyura.github.com.mapsplaces.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mickevichyura.github.com.mapsplaces.PlaceObject;
import mickevichyura.github.com.mapsplaces.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PlaceViewHolder> {

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView placeName;
        TextView placeAddress;
        ImageView placePhoto;

        PlaceViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setLongClickable(true);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            placeName = (TextView) itemView.findViewById(R.id.place_name);
            placeAddress = (TextView) itemView.findViewById(R.id.place_address);
            placePhoto = (ImageView) itemView.findViewById(R.id.place_photo);
        }

        public void bind(final PlaceObject place, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(place);
                }
            });
        }

    }

    public List<PlaceObject> places;

    private OnItemClickListener listener;

    public RecyclerViewAdapter(List<PlaceObject> places, OnItemClickListener listener) {
        this.places = places;
        this.listener = listener;
    }

    public RecyclerViewAdapter(List<PlaceObject> places) {
        this.places = places;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_card_item, parent, false);
        PlaceViewHolder pvh = new PlaceViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        holder.placeName.setText(places.get(position).getName());
        holder.placeAddress.setText(places.get(position).getAddress());
        holder.placePhoto.setImageResource((R.drawable.common_google_signin_btn_icon_light));
        holder.bind(places.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
