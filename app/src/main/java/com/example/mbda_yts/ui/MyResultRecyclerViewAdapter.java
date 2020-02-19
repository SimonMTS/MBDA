package com.example.mbda_yts.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbda_yts.YTVideo;
import com.example.mbda_yts.ui.ResultFragment.OnListFragmentInteractionListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import com.example.mbda_yts.R;


public class MyResultRecyclerViewAdapter extends RecyclerView.Adapter<MyResultRecyclerViewAdapter.ViewHolder> {

    private final List<YTVideo> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyResultRecyclerViewAdapter(List<YTVideo> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).video_title);

        if ( !mValues.get(position).video_image_url.equals("") ) {
            new DownLoadImageTask( holder.mThumbnail ).execute( mValues.get(position).video_image_url );
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addItems(LinkedHashSet<YTVideo> newVideos) {
//        mValues.addAll(0, newVideos);

        LinkedList<YTVideo> list = new LinkedList<>(newVideos);
        Iterator<YTVideo> itr = list.iterator(); //descending
        while(itr.hasNext()) {
            YTVideo video = itr.next();

            mValues.add(0, video);
            this.notifyItemInserted(0);
        }

//        mValues.add(0, new YTVideo("", "test1-test1-test1-test1", "https://i.ytimg.com/vi/J3pF2jkQ4vc/hqdefault.jpg"));
//        this.notifyItemInserted(0);

//        mValues.add(mValues.size()-1, new YTVideo("", "test2-test2-test2-test2", "https://i.ytimg.com/vi/J3pF2jkQ4vc/hqdefault.jpg"));
//        this.notifyItemInserted(mValues.size()-1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mThumbnail;
        public YTVideo mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.content);
            mThumbnail = view.findViewById(R.id.thumbnail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            Bitmap cropped = Bitmap.createBitmap(result, 0,45,480,270 );

            imageView.setImageBitmap(cropped);
        }
    }
}
