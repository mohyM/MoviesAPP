package com.example.movies;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements OnVersionNameSelectionChangeListener  {

    static  Boolean twopane=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(findViewById(R.id.fragment_container)!=null)
        {
            twopane=true;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }


   @Override
    public void OnSelectionChanged(int versionNameIndex, int state, String t, String ot, String ol, String overview, float pop, String data, float average, String path, int id) {

        if (twopane==true) {
            // If description is available, we are in two pane layout
            // so we call the method in DescriptionFragment to update its content
                 //     d.setDescription(versionNameIndex);
            DetailFragement d=new DetailFragement();

            Bundle args = new Bundle();
            args.putInt("state",state );
            args.putString("title",t);
            args.putString("originaltitle",ot );
            args.putString("originallanguage",ol);
            args.putString("overview", overview );
            args.putFloat("popularity",pop );
            args.putString("date", data );
            args.putFloat("rating", average );
            args.putString("path", path );
            args.putInt("movie_id",id);
            d.setArguments(args);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the backStack so the User can navigate back
            fragmentTransaction.replace(R.id.fragment_container, d);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        } else {

          Intent i = new Intent(this, DetailedActivity.class);
            i.putExtra("state", state);
            i.putExtra("title", t);
            i.putExtra("originaltitle", ot);
            i.putExtra("originallanguage", ol);
            i.putExtra("overview", overview);
            i.putExtra("popularity", pop);
            i.putExtra("date", data);
            i.putExtra("rating", average);
            i.putExtra("path",path);
            i.putExtra("movie_id", id);
            i.putExtra("state",state);
            startActivity(i);
        }
    }
}
