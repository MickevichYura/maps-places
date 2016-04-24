package mickevichyura.github.com.mapsplaces.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mickevichyura.github.com.mapsplaces.R;

public class PhotoCardViewHolders extends RecyclerView.ViewHolder implements
        View.OnClickListener
{
    public ImageView mPhotoImageView;
    public TextView mAuthorTextView;

    public PhotoCardViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        mPhotoImageView = (ImageView) itemView.findViewById(R.id.photo_image_view);
        mAuthorTextView = (TextView) itemView.findViewById(R.id.author_tv);
    }

    @Override
    public void onClick(View view)
    {
        Toast.makeText(view.getContext(),
                "Clicked Position = " + view.getId(), Toast.LENGTH_SHORT)
                .show();
    }
}
