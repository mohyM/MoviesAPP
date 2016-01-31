package com.example.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.model.MovieModel;
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
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {


    TextView tv;
    ArrayAdapter<String>x;
    GridView l;
    String link=null;
    List<MovieModel>movieModelList2;
    List<MovieModel>favourite;


    List<String>movieModelListfavourite;
    String var="http://image.tmdb.org/t/p/w185/";
    int mposition= GridView.INVALID_POSITION;;
    public MainActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forcastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.action_refresh)
        {
            updateData();
            return true;
        }
        if(id==R.id.action_sort)
        {
            Intent i=new Intent(getActivity(),SettingActivity.class);
            startActivity(i);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String value=pref.getString("sorting", "most_popular");
        if(value.equals("most_popular")||value.equals("highest_rated")) {
            updateData();
        }
        else {
            getMovieData();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mposition!=GridView.INVALID_POSITION)
        {
            outState.putInt("key",mposition);
        }
        super.onSaveInstanceState(outState);


    }

    public void updateData()
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String value=pref.getString("sorting", "most_popular");

            if (value.equals("most_popular")) {
                link = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+getActivity().getString(R.string.movie_api);
            } else if (value.equals("highest_rated")) {
                link = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key="+getActivity().getString(R.string.movie_api);
            }

            CatchData data = new CatchData();
            data.execute(link);
            Log.v("button", "send");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_main, container, false);
         l=(GridView)root.findViewById(R.id.list1);
        l.setOnItemClickListener(this);
        if(savedInstanceState!=null && savedInstanceState.containsKey("key"))
        {
            mposition=savedInstanceState.getInt("key");
        }
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String value=pref.getString("sorting","most_popular");
        if(value.equals("most_popular")||value.equals("highest_rated")) {

            OnVersionNameSelectionChangeListener listener = (OnVersionNameSelectionChangeListener) getActivity();
            listener.OnSelectionChanged(position,1,movieModelList2.get(position).getTitle(),movieModelList2.get(position).getOriginal_title(),
                    movieModelList2.get(position).getOriginal_language(),movieModelList2.get(position).getOverview(),
                    movieModelList2.get(position).getPopularity(),movieModelList2.get(position).getRelease_date(),
                    movieModelList2.get(position).getVote_average(), movieModelList2.get(position).getPoster_path(),movieModelList2.get(position).getId() );
        }
        else
        {

            OnVersionNameSelectionChangeListener listener2 = (OnVersionNameSelectionChangeListener) getActivity();
            listener2.OnSelectionChanged(position,2,favourite.get(position).getTitle(),favourite.get(position).getOriginal_title(),
                    favourite.get(position).getOriginal_language(),favourite.get(position).getOverview(),
                    favourite.get(position).getPopularity(),favourite.get(position).getRelease_date(),
                    favourite.get(position).getVote_average(), favourite.get(position).getPoster_path(),favourite.get(position).getId());

        }
        mposition=position;
    }
    public void getMovieData()
    {
        favourite=new ArrayList<>();
        movieModelListfavourite = new ArrayList<>();
        Cursor cu=getContext().getContentResolver().query(Contract.Link,null,null,null,null);
        for(cu.moveToFirst();!cu.isAfterLast();cu.moveToNext())
        {
            MovieModel mf=new MovieModel();
            mf.setTitle(cu.getString(cu.getColumnIndex(MovieData.TITLE)));
            mf.setOriginal_language(cu.getString(cu.getColumnIndex(MovieData.Language)));
            mf.setOriginal_title(cu.getString(cu.getColumnIndex(MovieData.ORG_TITLE)));
            mf.setPoster_path(cu.getString(cu.getColumnIndex(MovieData.POSTER_PATH)));
            mf.setPopularity(cu.getFloat(cu.getColumnIndex(MovieData.POPULARITY)));
            mf.setRelease_date(cu.getString(cu.getColumnIndex(MovieData.DATE)));
            mf.setId(cu.getInt(cu.getColumnIndex(MovieData.MOVIE_ID)));
            mf.setOverview(cu.getString(cu.getColumnIndex(MovieData.OVERVIEW)));
            mf.setVote_average(cu.getFloat(cu.getColumnIndex(MovieData.Rating)));
            movieModelListfavourite.add(cu.getString(cu.getColumnIndex(MovieData.POSTER_PATH)));
            favourite.add(mf);
        }
        cu.close();
        l.setAdapter(new ImageListAdapter(getActivity(), movieModelListfavourite));




    }


    public class CatchData extends AsyncTask<String,Void,List<String>>
    {

        @Override
        protected List<String> doInBackground(String... params) {
            if (params.length==0)
                return null;
            List<String>movieModelList=new ArrayList<>();
            movieModelList2=new ArrayList<>();
            HttpURLConnection conn=null;
            BufferedReader br=null;
             String total;
            try
            {
            URL url=new URL(params[0]);
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
                conn.connect();
                Log.v("status=","connected");
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
                Log.v("status=","string buffer succefully data");
                total=sb.toString();
                JSONObject obj=new JSONObject(total);
                JSONArray arr=obj.getJSONArray("results");

                for(int i=0;i<arr.length();i++)
                {
                    JSONObject ob=arr.getJSONObject(i);
                    MovieModel m=new MovieModel();
                  String  path=var.concat(ob.getString("poster_path"));
                    m.setPoster_path(path);
                    m.setAdult(ob.getBoolean("adult"));
                    m.setOverview(ob.getString("overview"));
                    m.setRelease_date(ob.getString("release_date"));
                    m.setId(ob.getInt("id"));
                    m.setOriginal_title(ob.getString("original_title"));
                    m.setOriginal_language(ob.getString("original_language"));
                    m.setTitle(ob.getString("title"));
                    m.setBackdrop_path(ob.getString("backdrop_path"));
                    m.setPopularity((float) ob.getDouble("popularity"));
                    m.setViedo(ob.getBoolean("video"));
                    m.setVote_average((float) ob.getDouble("vote_average"));

                    movieModelList.add(path);
                    movieModelList2.add(m);

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

            l.setAdapter(new ImageListAdapter(getActivity(), result));
            new ImageListAdapter(getActivity(), result).notifyDataSetChanged();



        }
    }
    public class ImageListAdapter extends ArrayAdapter
    {
        private Context context;
        private List<String>obj;
        private LayoutInflater inflater;
        public ImageListAdapter(Context context, List<String>objects) {
            super(context,R.layout.listlayout,objects);
            obj=objects;
            this.context=context;
            inflater=LayoutInflater.from(context);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null)
                convertView=inflater.inflate(R.layout.listlayout,parent,false);
            Picasso.with(context).load(obj.get(position)).fit().into((ImageView)convertView);

            return convertView;

        }
    }



}
