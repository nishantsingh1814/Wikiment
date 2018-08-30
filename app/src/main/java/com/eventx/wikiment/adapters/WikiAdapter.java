package com.eventx.wikiment.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eventx.wikiment.R;
import com.eventx.wikiment.interfaces.OnItemClick;
import com.eventx.wikiment.networkModels.SearchResult;

import java.util.ArrayList;

public class WikiAdapter extends RecyclerView.Adapter<WikiAdapter.WikiViewHolder> {

    private OnItemClick  onItemClick;

    public WikiAdapter(OnItemClick onItemClick, Context mContext, ArrayList<SearchResult.Query.Pages> mResultList) {
        this.onItemClick = onItemClick;
        this.mContext = mContext;
        this.mResultList = mResultList;
    }

    private Context mContext;
    private ArrayList<SearchResult.Query.Pages> mResultList;


    @NonNull
    @Override
    public WikiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WikiViewHolder(LayoutInflater.from(mContext).inflate(R.layout.result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WikiViewHolder holder, final int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.mTitleTv.setText(Html.fromHtml(mResultList.get(position).getTitle(), Html.FROM_HTML_MODE_COMPACT));
            if (mResultList.get(position).getTerms() != null) {
                holder.mSnippetTv.setText(Html.fromHtml(mResultList.get(position).getTerms().getDescription().get(0), Html.FROM_HTML_MODE_COMPACT));
            }
        } else {
            holder.mTitleTv.setText(Html.fromHtml(mResultList.get(position).getTitle()));
            if (mResultList.get(position).getTerms() != null) {
                holder.mSnippetTv.setText(Html.fromHtml(mResultList.get(position).getTerms().getDescription().get(0)));
            }
        }
        if (mResultList.get(position).getThumbnail() != null) {
            Glide.with(mContext).load(mResultList.get(position).getThumbnail().getSource()).into(holder.mResultIv);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemCLick(view,mResultList.get(position).getPageid());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }

    class WikiViewHolder extends RecyclerView.ViewHolder {

        private ImageView mResultIv;
        private TextView mSnippetTv;
        private TextView mTitleTv;

        WikiViewHolder(View itemView) {
            super(itemView);
            mResultIv = itemView.findViewById(R.id.result_iv);
            mSnippetTv = itemView.findViewById(R.id.snippet_tv);
            mTitleTv = itemView.findViewById(R.id.title_tv);
        }
    }
}
