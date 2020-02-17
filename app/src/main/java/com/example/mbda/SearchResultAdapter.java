package com.example.mbda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    LinkedHashSet<String> SearchResultList;

    public SearchResultAdapter(LinkedHashSet<String> searchResultList, Context context) {
        this.SearchResultList = searchResultList;
    }

    @Override
    public SearchResultAdapter.SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row,parent,false);
        SearchResultViewHolder viewHolder=new SearchResultViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchResultAdapter.SearchResultViewHolder holder, int position) {
        ArrayList<String> asList = new ArrayList<>(SearchResultList);

        holder.text.setText(asList.get(position));
    }

    @Override
    public int getItemCount() {
        return SearchResultList.size();
    }

    public static class SearchResultViewHolder extends RecyclerView.ViewHolder {

        protected TextView text;

        public SearchResultViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_id);
        }
    }
}