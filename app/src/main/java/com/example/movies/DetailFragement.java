package com.example.movies;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Review_Data;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by لا اله الا الله on 27/01/2016.
 */
public class DetailFragement extends Fragment implements View.OnClickListener,AsyncResponse {

    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
    ImageView iv;
    RatingBar rat;
    ImageButton btn;
    MovieData md;
    ListView list;
    String title=null;
    String originalt=null;
    String originall=null;
    String story=null;
    float pop;
    String dt=null;
    float rating;
    String path=null;
    int id;
    int state;
    ListView list2;
    List<String>data=new ArrayList<String>();
   View root;
    RecyclerView  recyclerView;
    RecyclerAdapter adapter;
    private ArrayAdapter<String>adapt;
    List<String>li=new ArrayList<String>();
    List<Review_Data>rd=new ArrayList<Review_Data>();
    List<String>movieModelList=new ArrayList<String>();
    String tweet="#first_trailer_path";
    List<String>trailers=new ArrayList<String>();
    String[]videos;
    android.support.v7.widget.ShareActionProvider share;
    public DetailFragement()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_menu, menu);
        MenuItem item=(MenuItem)menu.findItem(R.id.action_share);
         share=(android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root=inflater.inflate(R.layout.fragement_detail, container, false);
        initialize(root);

        md=new MovieData(getActivity().getApplicationContext());
        if(MainActivity.twopane) {
            Bundle x = this.getArguments();
            title = x.getString("title");
            originalt = x.getString("originaltitle");
            originall = x.getString("originallanguage");
            story = x.getString("overview");
            pop = x.getFloat("popularity");
            dt = x.getString("date");
            rating = x.getFloat("rating");
            path = x.getString("path");
            id = x.getInt("movie_id");
            state = x.getInt("state");
        }
        else
        {
            Bundle x=getActivity().getIntent().getExtras();
            title = x.getString("title");
            originalt = x.getString("originaltitle");
            originall = x.getString("originallanguage");
            story = x.getString("overview");
            pop = x.getFloat("popularity");
            dt = x.getString("date");
            rating = x.getFloat("rating");
            path = x.getString("path");
            id = x.getInt("movie_id");
            state = x.getInt("state");

            }
        if(state==1 || state==2) {
            tv1.setText(title);
            tv2.setText(originalt);
            tv3.setText(originall);
            tv4.setText(story);
            tv5.setText("" + pop);
            tv6.setText(dt);
            rat.setRating((float) (rating / 2));
            Picasso.with(getActivity().getApplicationContext()).load(path).fit().into(iv);
            list2 = (ListView) root.findViewById(R.id.list3);

            recyclerView = (RecyclerView) root.findViewById(R.id.list2);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        if(state ==1) {

            getTrailers();
            getReviews();
        }

        if(state==2)
        {
            getvideospath();
             adapter=new RecyclerAdapter(getActivity(),videos);
            recyclerView.setAdapter(adapter);
            Uri lin=Uri.parse("content://"+Contract.Authority+"/"+MovieData.table_name_r+"/"+id);
            Cursor cu=getActivity().getContentResolver().query(lin, null, null, null, null);
            LoadData l=new LoadData(getActivity(),cu,0);
            list2.setAdapter(l);
            list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(li.get(position)));
                    startActivity(i);
                }
            });
        }


        return root;
    }
    public Intent getDefaultShareIntent(String y)
    {

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, y + " " + tweet);



        return intent;
    }

    public void getvideospath()
    {

        Uri lin2=Uri.parse("content://" + Contract.Authority + "/" + MovieData.table_name_t + "/" + id);
        Cursor cu=getActivity().getContentResolver().query(lin2, null, null, null, null);
        for(cu.moveToFirst();! cu.isAfterLast();cu.moveToNext())
        {
            String path=cu.getString(cu.getColumnIndex(MovieData.Trailer_Path));
            trailers.add(path);
        }
        videos=new String[trailers.size()];
        trailers.toArray(videos);
    }

    public void  getTrailers()
    {



       String link="http://api.themoviedb.org/3/movie/"+id+"/videos?api_key="+getActivity().getString(R.string.movie_api);
        CatchData data = new CatchData();
        data.delegate = this;
        data.execute(link);





    }
    public void getReviews()
    {
        CatchReviews review=new CatchReviews();
        review.execute("http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=" + getActivity().getString(R.string.movie_api));

    }

    public void initialize(View root)
    {
        tv1=(TextView)root.findViewById(R.id.btn1);
        tv2=(TextView)root.findViewById(R.id.originaltitle);
        tv3=(TextView)root.findViewById(R.id.original_language);
        tv4=(TextView)root.findViewById(R.id.overview);
        tv5=(TextView)root.findViewById(R.id.popularity);
        tv6=(TextView)root.findViewById(R.id.release_date);
        iv=(ImageView)root.findViewById(R.id.image1);
        rat=(RatingBar)root.findViewById(R.id.rating1);
        btn=(ImageButton)root.findViewById(R.id.btn_favourite);
        btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        int x=0;
        Uri lin=Uri.parse("content://"+Contract.Authority+"/"+MovieData.table_name+"/"+id);
        Cursor cu=getActivity().getContentResolver().query(lin, null, null, null, null);
        for(cu.moveToFirst();!cu.isAfterLast();cu.moveToNext())
        {
            x=Integer.parseInt(cu.getString(cu.getColumnIndex(MovieData.MOVIE_ID)));
        }
        cu.close();

        if(x==0) {
            ContentValues val = new ContentValues();
            ContentValues val2 = new ContentValues();
            ContentValues val3 = new ContentValues();
            val.put(MovieData.MOVIE_ID, id);
            val.put(MovieData.TITLE, title);
            val.put(MovieData.ORG_TITLE, originalt);
            val.put(MovieData.POSTER_PATH, path);
            val.put(MovieData.Language, originall);
            val.put(MovieData.Rating, rating);
            val.put(MovieData.OVERVIEW, story);
            val.put(MovieData.POPULARITY, pop);
            val.put(MovieData.DATE, dt);

            Uri check = getActivity().getContentResolver().insert(Contract.Link, val);

            for (int i = 0; i < movieModelList.size(); i++) {

                val2.put(MovieData.MOVIE_ID_f, id);
                val2.put(MovieData.Trailer_Path, movieModelList.get(i));

                Uri check2 = getActivity().getContentResolver().insert(Contract.Link2, val2);
            }

            for (int i = 0; i < rd.size(); i++) {
                val3.put(MovieData.MOVIE_ID_R, id);
                val3.put(MovieData.Author, rd.get(i).getAuthor());
                val3.put(MovieData.DATA, rd.get(i).getReview());
                val3.put(MovieData.Link, rd.get(i).getUrl());
                Uri check3 = getActivity().getContentResolver().insert(Contract.Link3, val3);
            }

            Toast.makeText(getActivity(),"Added To Favourite", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(), "It's Already In Favourite", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void processFinish(List<String> output) {

        if(output.size()!=0) {
            try {
                share.setShareIntent(getDefaultShareIntent("https://www.youtube.com/watch?v=".concat(output.get(0))));
            }
            catch (Exception e)
            {

            }
        }

    }

    @Override
    public void processFinishTrailers(List<String> output) {


        String[]VideoId=new String[output.size()];
        output.toArray(VideoId);
         adapter=new RecyclerAdapter(getActivity(),VideoId);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public class CatchData extends AsyncTask<String,Void,List<String>>
    {
        public AsyncResponse delegate = null;

        @Override
        protected List<String> doInBackground(String... params) {
            if (params.length==0)
                return null;

            HttpURLConnection conn=null;
            BufferedReader br=null;
            String total;
            try
            {
                URL url=new URL(params[0]);
                conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                InputStream isn=conn.getInputStream();
                if(isn==null)
                {
                    total=null;
                }

                br=new BufferedReader(new InputStreamReader(isn));
                StringBuffer sb=new StringBuffer();
                String line;
                while((line=br.readLine())!=null)
                {
                    sb.append(line);
                }
                if(sb.length()==0)
                {
                    total=null;
                }
                total=sb.toString();
                JSONObject obj=new JSONObject(total);
                JSONArray arr=obj.getJSONArray("results");

                for(int i=0;i<arr.length();i++)
                {
                    JSONObject ob=arr.getJSONObject(i);
                    String  key=ob.getString("key");
                    movieModelList.add(key);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            catch(JSONException e)
            {
                System.out.println("Error");
                e.printStackTrace();
            }
            finally {
                if(conn!=null)
                    conn.disconnect();
                if(br!=null)
                {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return movieModelList;
        }

        @Override
        protected void onPostExecute(List<String>result) {

            super.onPostExecute(result);

            if(result.size()!=0) {
                delegate.processFinish(result);
                delegate.processFinishTrailers(result);
            }




        }
    }

    public class CatchReviews extends AsyncTask<String,Void,List<Review_Data>>
    {

        @Override
        protected List<Review_Data> doInBackground(String... params) {
            if (params.length==0)
                return null;

            HttpURLConnection conn=null;
            BufferedReader br=null;
            String total;
            try
            {
                URL url=new URL(params[0]);
                conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                InputStream isn=conn.getInputStream();
                if(isn==null)
                {
                    total=null;
                }
                br=new BufferedReader(new InputStreamReader(isn));
                StringBuffer sb=new StringBuffer();
                String line;
                while((line=br.readLine())!=null)
                {
                    sb.append(line);
                }
                if(sb.length()==0)
                {
                    total=null;
                }
                total=sb.toString();
                JSONObject obj=new JSONObject(total);
                JSONArray arr=obj.getJSONArray("results");

                for(int i=0;i<arr.length();i++)
                {
                    Review_Data r=new Review_Data();
                    JSONObject ob=arr.getJSONObject(i);
                    String auth=ob.getString("author");
                    String content=ob.getString("content");
                    String path=ob.getString("url");
                    r.setAuthor(auth);
                    r.setReview(content);
                    r.setUrl(path);
                    li.add(path);
                    rd.add(r);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            catch(JSONException e)
            {
                System.out.println("Error");
                e.printStackTrace();
            }
            finally {
                if(conn!=null)
                    conn.disconnect();
                if(br!=null)
                {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return rd;
        }

        @Override
        protected void onPostExecute(List<Review_Data>result) {

            super.onPostExecute(result);
            if(result.size()!=0) {

                ReviewAdapter adapt = new ReviewAdapter(getActivity().getApplicationContext(), result);
                list2.setAdapter(adapt);
                adapt.notifyDataSetChanged();
                list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String url = li.get(position);
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);

                    }
                });
            }
        }
    }
    class LoadData extends CursorAdapter
    {

        public LoadData(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout,parent,false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView txt9=(TextView)view.findViewById(R.id.author);
            TextView txt10=(TextView)view.findViewById(R.id.review);
            TextView txt11=(TextView)view.findViewById(R.id.link);
            String auth=cursor.getString(cursor.getColumnIndex(MovieData.Author));
            String content=cursor.getString(cursor.getColumnIndex(MovieData.DATA));
            String link=cursor.getString(cursor.getColumnIndex(MovieData.Link));
            li.add(link);
            txt9.setText(auth);
            txt10.setText(content);
            txt11.setText(link);


        }
    }



}
