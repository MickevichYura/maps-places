package mickevichyura.github.com.mapsplaces.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mickevichyura.github.com.mapsplaces.PhotoObject;
import mickevichyura.github.com.mapsplaces.R;

public class PhotoCardRecyclerViewAdapter extends RecyclerView.Adapter<PhotoCardViewHolders>
{
    private List<PhotoObject> photoList;
    private Context context;

    public PhotoCardRecyclerViewAdapter(Context context,
                                        List<PhotoObject> photoList)
    {
        this.photoList = photoList;
        this.context = context;
    }

    @Override
    public PhotoCardViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.photo_card_item, null);
        PhotoCardViewHolders rcv = new PhotoCardViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(PhotoCardViewHolders holder, int position)
    {
        holder.mPhotoImageView.setImageBitmap(photoList.get(position).getPhoto());
        holder.mAuthorTextView.setText(photoList.get(position).getAuthor());
    }

    @Override
    public int getItemCount()
    {
        return this.photoList.size();
    }
}
