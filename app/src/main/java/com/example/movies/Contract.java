package com.example.movies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by لا اله الا الله on 05/01/2016.
 */
public class Contract extends ContentProvider {
    public static final String Authority="com.example.movies.Contract";
    public static final Uri Link=Uri.parse("content://"+Authority+"/"+MovieData.table_name);
    public static final Uri Link2=Uri.parse("content://"+Authority+"/"+MovieData.table_name_t);
    public static final Uri Link3=Uri.parse("content://"+Authority+"/"+MovieData.table_name_r);
    SQLiteDatabase sd;
    MovieData db;
    public static final int movie=1;
    public static final int movie_id=2;
    public static final int trailer=3;
    public static final int trailer_id=4;
    public static final int review=5;
    public static final int review_id=6;
    public static final UriMatcher suri=new UriMatcher(UriMatcher.NO_MATCH);
    static {
        suri.addURI(Authority,MovieData.table_name,movie);
        suri.addURI(Authority,MovieData.table_name+"/#",movie_id);
        suri.addURI(Authority,MovieData.table_name_t,trailer);
        suri.addURI(Authority,MovieData.table_name_t+"/#",trailer_id);
        suri.addURI(Authority,MovieData.table_name_r,review);
        suri.addURI(Authority,MovieData.table_name_r+"/#",review_id);

    }
    @Override
    public boolean onCreate() {
        db=new MovieData(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sq=new SQLiteQueryBuilder();
     ;

        switch (suri.match(uri))
        {
            case movie:
                sq.setTables(MovieData.table_name);

                break;
            case movie_id:
                sq.setTables(MovieData.table_name);
                sq.appendWhere(MovieData.MOVIE_ID+" = "+uri.getLastPathSegment());
                break;
            case trailer:
                break;
            case trailer_id:
                sq.setTables(MovieData.table_name_t);
                sq.appendWhere(MovieData.MOVIE_ID_f+" = "+uri.getLastPathSegment());
                break;
            case review:
                break;
            case review_id:
                sq.setTables(MovieData.table_name_r);
                sq.appendWhere(MovieData.MOVIE_ID_R+" = "+uri.getLastPathSegment());
                break;
            default:
                new IllegalArgumentException("invalid uri "+uri);
        }
        Cursor cu=sq.query(db.getReadableDatabase(),projection,selection,selectionArgs,null,null,sortOrder);
        cu.setNotificationUri(getContext().getContentResolver(), uri);
        return cu;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        sd=db.getWritableDatabase();
        long id=0;
        long id_t=0;
        long id_r=0;
        Uri _uri=null;
        switch (suri.match(uri))
        {

            case movie:
                id=sd.insert(MovieData.table_name,null,values);
                if (id > 0) {
                    _uri = ContentUris.withAppendedId(Link, id);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case movie_id:

                break;
            case trailer:
                id_t=sd.insert(MovieData.table_name_t,null,values);
                Toast.makeText(getContext(),""+id_t, Toast.LENGTH_LONG).show();
                if (id_t > 0) {
                    _uri = ContentUris.withAppendedId(Link2, id_t);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case trailer_id:
                Log.v("number",""+suri.match(uri));
                Toast.makeText(getContext(),""+suri.match(uri), Toast.LENGTH_LONG).show();
                break;
            case review:
                id_r=sd.insert(MovieData.table_name_r,null,values);
                if (id_r > 0) {
                    _uri = ContentUris.withAppendedId(Link3, id_r);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case review_id:
                break;
            default:
                new IllegalArgumentException("invalid uri"+uri);
        }

        return _uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        sd=db.getWritableDatabase();
        int nrow=0;

        switch (suri.match(uri))
        {
            case movie:
                nrow=sd.delete(MovieData.table_name,selection,selectionArgs);
                break;
            case movie_id:
                if(TextUtils.isEmpty(selection))
                {
                    nrow=sd.delete(MovieData.table_name,MovieData.MOVIE_ID+" = "+uri.getLastPathSegment(),null);
                }
                else
                {
                    nrow=sd.delete(MovieData.table_name,MovieData.MOVIE_ID+" = "+uri.getLastPathSegment() + " AND "+selection,selectionArgs);
                }
                break;
            case trailer:
                nrow=sd.delete(MovieData.table_name_t,selection,selectionArgs);
                break;
            case trailer_id:
                if(TextUtils.isEmpty(selection))
                {
                    nrow=sd.delete(MovieData.table_name_t,MovieData.MOVIE_ID_f+" = "+uri.getLastPathSegment(),null);
                }
                else
                {
                    nrow=sd.delete(MovieData.table_name_t,MovieData.MOVIE_ID_f+" = "+uri.getLastPathSegment() + " AND "+selection,selectionArgs);
                }
                break;
            case review:
                nrow=sd.delete(MovieData.table_name_r,selection,selectionArgs);
                break;
            case review_id:
                if(TextUtils.isEmpty(selection))
                {
                    nrow=sd.delete(MovieData.table_name_r,MovieData.MOVIE_ID_R+" = "+uri.getLastPathSegment(),null);
                }
                else
                {
                    nrow=sd.delete(MovieData.table_name_r,MovieData.MOVIE_ID_R+" = "+uri.getLastPathSegment() + " AND "+selection,selectionArgs);
                }
                break;

            default:
                new IllegalArgumentException("invalid uri"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return nrow;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        return 0;
    }
}
