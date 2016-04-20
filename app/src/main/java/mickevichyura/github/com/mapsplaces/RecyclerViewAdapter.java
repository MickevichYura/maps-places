package mickevichyura.github.com.mapsplaces;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PlaceViewHolder> {

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;

        PlaceViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            personName = (TextView) itemView.findViewById(R.id.place_name);
            personAge = (TextView) itemView.findViewById(R.id.place_age);
            personPhoto = (ImageView) itemView.findViewById(R.id.place_photo);
        }

    }

    public List<Place> places;

    RecyclerViewAdapter(List<Place> places) {
        this.places = places;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        PlaceViewHolder pvh = new PlaceViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder placeViewHolder, int i) {
        placeViewHolder.personName.setText(places.get(i).name);
        placeViewHolder.personAge.setText(places.get(i).address);
        placeViewHolder.personPhoto.setImageDrawable(places.get(i).photoDrawable);
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
