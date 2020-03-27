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

import com.example.mbda_yts.MainActivity;
import com.example.mbda_yts.YTVideo;
import com.example.mbda_yts.ui.ResultFragment.OnListFragmentInteractionListener;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import com.example.mbda_yts.R;


public class ResultRecyclerViewAdapter extends RecyclerView.Adapter<ResultRecyclerViewAdapter.ViewHolder> {

    private final List<YTVideo> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ResultRecyclerViewAdapter(List<YTVideo> items, OnListFragmentInteractionListener listener) {
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

        final YTVideo clickedVideo = mValues.get(position);

        if ( !mValues.get(position).video_image_url.equals("") ) {
            new DownLoadImageTask( holder.mThumbnail ).execute( mValues.get(position).video_image_url );
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);

                    ((MainActivity)v.getContext()).onClick(clickedVideo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addItems(LinkedHashSet<YTVideo> newVideos) {

        LinkedList<YTVideo> list = new LinkedList<>(newVideos);
        Iterator<YTVideo> itr = list.iterator(); //descending
        while(itr.hasNext()) {
            YTVideo video = itr.next();

            mValues.add(0, video);
            this.notifyItemInserted(0);
        }

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

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();

                logo = BitmapFactory.decodeStream(is);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            Bitmap cropped = Bitmap.createBitmap(result, 0,45,480,270 );

            imageView.setImageBitmap(cropped);
        }
    }
}
