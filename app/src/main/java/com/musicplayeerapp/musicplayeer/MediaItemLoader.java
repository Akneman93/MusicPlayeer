package com.musicplayeerapp.musicplayeer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Алексей on 08.01.2017.
 */

public class MediaItemLoader {

    public static final String SONG_DESC = "SONG_";
    public static final String ALBUMS_REQEST = "ALBUMS_REQUEST";




    Context mContext;
    ContentResolver mContentResolver;

    public MediaItemLoader(Context context)
    {
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    MediaDescriptionCompat.Builder  builder = new  MediaDescriptionCompat.Builder();


    // TODO: 09.01.2017  deal with it later;
    private List<MediaBrowserCompat.MediaItem> getAlbums()
    {
        String[] projection = new String[] { MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = MediaStore.Audio.Media.ALBUM + " ASC";
        Cursor cursor = mContentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder);

        List<MediaBrowserCompat.MediaItem> mItems = new ArrayList<>();



        if (cursor != null && cursor.getCount() > 0) {
            Log.e("Loader", "cursor count albums: " + new Integer(cursor.getCount()).toString());
            while (cursor.moveToNext()) {
                //String AlbumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                //Log.e("Loader",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                //String AlbumsID = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));



               // builder.setMediaId(AlbumName);
                //builder.setTitle(AlbumName);
                //builder.setDescription(ALBUM_DESC);
               // mItems.add(new MediaBrowserCompat.MediaItem(builder.build(), MediaBrowserCompat.MediaItem.FLAG_BROWSABLE));
            }
        }
        cursor.close();

        return mItems;
    }

    // TODO: 09.01.2017  deal with it later2;
    private List<MediaBrowserCompat.MediaItem> getSongs(String AlbumID)
    {

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0" + " AND "
                + MediaStore.Audio.Media.ALBUM_ID + " == " +AlbumID ;
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selection, null, sortOrder);


        List<MediaBrowserCompat.MediaItem> mItems = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String Artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                Log.e("Loader",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                String Title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                Log.e("Loader",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                String Data =  cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                Log.e("Loader",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));

                builder.setTitle(Artist);
                builder.setSubtitle(Title);
                builder.setMediaUri(Uri.parse(Data));
                builder.setDescription(SONG_DESC);
                mItems.add(new MediaBrowserCompat.MediaItem(builder.build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
            }
        }
        cursor.close();

        return mItems;
    }


    /** Get all songs sorted by title */

    public List<MediaBrowserCompat.MediaItem> getSongs()
    {

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selection, null, sortOrder);


        List<MediaBrowserCompat.MediaItem> mItems = new ArrayList<>();

        int i = 0;


        Log.e("Loader",Integer.toString(cursor.getCount()));

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String Artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
              //  Log.e("Loader",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                String Title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
              //  Log.e("Loader",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                String Data =  cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
               // Log.e("Loader",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));



                builder.setTitle(Artist.equals("<unknown>") ? "" : Artist);
                builder.setSubtitle(Title.equals("<unknown>") ? "" : Title);
                builder.setMediaUri(Uri.parse(Data));
                builder.setDescription(SONG_DESC);
                builder.setMediaId(new Integer(i).toString());
                mItems.add(new MediaBrowserCompat.MediaItem(builder.build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
                i++;
            }
        }
        cursor.close();

        return mItems;
    }



    /** main function to get media items based on onLoadChildren's parentID */
    public List<MediaBrowserCompat.MediaItem> getItems(final String parentMediaID)
    {
        /*if (parentMediaID.equals(ALBUMS_REQEST))
            return getAlbums();
        else
            return  getSongs(parentMediaID);*/
        return getSongs();

    }



}
