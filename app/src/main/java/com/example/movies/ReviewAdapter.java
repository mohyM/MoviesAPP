package com.example.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.model.Review_Data;

import java.util.List;

/**
 * Created by لا اله الا الله on 23/01/2016.
 */
class ReviewAdapter extends ArrayAdapter
{
    List<Review_Data> obj;
    Context context;
    LayoutInflater inflater;
    public ReviewAdapter(Context context,List<Review_Data>objects) {
        super(context,R.layout.review_layout, objects);
        obj=objects;
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView=inflater.inflate(R.layout.review_layout,parent,false);

      //  Toast.makeText(getApplicationCon, "" + position, Toast.LENGTH_SHORT).show();


        TextView auth=(TextView) convertView.findViewById(R.id.author);
          TextView content=(TextView)convertView.findViewById(R.id.review);
          TextView path=(TextView)convertView.findViewById(R.id.link);
        auth.setText("Author:"+obj.get(position).getAuthor());
         content.setText(obj.get(position).getReview());
          path.setText(obj.get(position).getUrl());

        return convertView;

    }
}

