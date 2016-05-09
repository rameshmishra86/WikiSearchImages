package com.example.ramesh.wikisearch;

/**
 * Created by ramesh on 5/4/2016.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<DataObject> List_wiki;
    private Context context;

    public DataAdapter(Context context,ArrayList<DataObject> List_wiki) {
        this.context = context;
        this.List_wiki = List_wiki;

    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {


        if(!List_wiki.get(i).getImage_url().equalsIgnoreCase(""))
        Picasso.with(context)
                .load(List_wiki.get(i).getImage_url())
                .placeholder(R.drawable.microsoft) // optional
                .error(R.drawable.microsoft)       // optional
                .into(viewHolder.img_wiki);
        else
            viewHolder.img_wiki.setImageResource(R.drawable.microsoft);


        viewHolder.tv_wiki.setText(List_wiki.get(i).getImage_title());
    }

    @Override
    public int getItemCount() {
        return List_wiki.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_wiki;
        TextView tv_wiki;

        public ViewHolder(View view) {
            super(view);

            img_wiki = (ImageView)view.findViewById(R.id.img_wiki);
            tv_wiki = (TextView)view.findViewById(R.id.tv_wiki);
        }
    }
}