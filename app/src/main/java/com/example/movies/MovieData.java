package com.example.movies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by لا اله الا الله on 25/12/2015.
 */
class MovieData extends SQLiteOpenHelper {

   public final static String dbname="movie.db";
    public final static int version=2;
    ///////////table of movie data /////////////////////////
    public final static String table_name="moviedata";
    public final static String TITLE="title";
    public final static String ORG_TITLE="org_title";
    public final static String MOVIE_ID="movie_id";
    public final static String POSTER_PATH="poster_path";
    public final static String Language="language";
    public final static String Rating="rating";
    public final static String OVERVIEW="overview";
    public final static String POPULARITY="popularity";
    public final static String DATE="release_date";
    //////////////table of youtube trailers ////////////////////////////
    public final static String table_name_t="trailersdata";
    public final static String ID="_id";
    public final static String Trailer_Path="path";
    public final static String MOVIE_ID_f="movie_id";
    ///////////table of reviews ///////////////////////////////////////
    public final static String table_name_r="reviewsdata";
    public final static String ID_r="_id";
    public final static String Author="author";
    public final static String DATA="data";
    public final static String Link="link";
    public final static String MOVIE_ID_R="movie_id";
    SQLiteDatabase sd;

    public MovieData(Context context) {
        super(context, dbname, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE "+ table_name +"("+
                MOVIE_ID+" INTEGER PRIMARY KEY,"+
                TITLE+" TEXT NOT NULL,"+
                ORG_TITLE+" TEXT NOT NULL,"+
                POSTER_PATH+" TEXT NOT NULL,"+
                Language+" TEXT NOT NULL,"+
                Rating+" REAL NOT NULL,"+
                OVERVIEW+" TEXT NOT NULL,"+
                POPULARITY+" REAL NOT NULL,"+
                DATE+" TEXT NOT NULL"+");";
        String query2="CREATE TABLE "+table_name_t+"("+
                ID+" INTEGER PRIMARY KEY  AUTOINCREMENT,"+
                Trailer_Path+" TEXT NOT NULL UNIQUE,"+
                MOVIE_ID_f+" INTEGER NOT NULL,"+
                "FOREIGN KEY ("+MOVIE_ID_f+") REFERENCES "+table_name+" ("+MOVIE_ID+"));";
        String query3="CREATE TABLE "+table_name_r+"("+
                ID_r+" INTEGER PRIMARY KEY  AUTOINCREMENT,"+
                Author+" TEXT NOT NULL,"+
                DATA+" TEXT NOT NULL,"+
                Link+" TEXT NOT NULL UNIQUE,"+
                MOVIE_ID_R+" INTEGER NOT NULL,"+
                "FOREIGN KEY ("+MOVIE_ID_R+") REFERENCES "+table_name+" ("+MOVIE_ID+"));";
       db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query3);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

          db.execSQL("DROP TABLE IF EXISTS "+table_name );
          db.execSQL("DROP TABLE IF EXISTS "+table_name_t );
        db.execSQL("DROP TABLE IF EXISTS "+table_name_r);
             onCreate(db);
    }

    public void openDB()
    {
        sd=getWritableDatabase();
    }
    public void closeDB()
    {
        if(sd!=null&&sd.isOpen())
        {
            sd.close();
        }
    }
}
