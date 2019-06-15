package com.bennelson.familymap.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bennelson.familymap.R;

/**
 * Created by BenNelson on 12/8/16.
 */

public class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private ImageView image;
    private Context context;
    private String id;
    private TextView firstLine;
    private TextView secondLine;

    public SearchHolder (View view)
    {
        super(view);
        //set views to child
        image = (ImageView) view.findViewById(R.id.marker_or_gender);
        firstLine = (TextView) view.findViewById(R.id.firstLine);
        secondLine = (TextView) view.findViewById(R.id.secondLine);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //if is person, go to person class activity

        //if is event go to map
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TextView getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(TextView firstLine) {
        this.firstLine = firstLine;
    }

    public TextView getSecondLine() {
        return secondLine;
    }

    public void setSecondLine(TextView secondLine) {
        this.secondLine = secondLine;
    }
}
