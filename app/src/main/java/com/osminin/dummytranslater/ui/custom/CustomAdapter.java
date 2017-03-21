package com.osminin.dummytranslater.ui.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.osminin.dummytranslater.R;
import com.osminin.dummytranslater.models.RecentModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by osminin on 3/20/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CLICK_TIMEOUT = 500;
    public static final int TYPE_HEADER = 111;
    public static final int TYPE_FOOTER = 222;
    public static final int TYPE_ITEM = 333;
    //our items
    List<RecentModel> items = new ArrayList<>();
    //headers
    List<View> headers = new ArrayList<>();
    //footers
    List<View> footers = new ArrayList<>();

    private PublishSubject<RecentModel> mViewClickSubject = PublishSubject.create();

    public Observable<RecentModel> getViewClickedObservable() {
        return mViewClickSubject;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        //if our position is one of our items (this comes from getItemViewType(int position) below)
        RecyclerView.ViewHolder res;
        View view;
        if (type == TYPE_ITEM) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.favorites_item_layout, viewGroup, false);
            res = new GenericViewHolder(view);
            //else we have a header/footer
        } else {
            //create a new framelayout, or inflate from a resource
            view = new FrameLayout(viewGroup.getContext());
            //make sure it fills the space
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            res = new HeaderFooterViewHolder(view);
        }
        return res;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
        //check what type of view our position is
        if (position < headers.size()) {
            View v = headers.get(position);
            //add our view to a header view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        } else if (position >= headers.size() + items.size()) {
            View v = footers.get(position - items.size() - headers.size());
            //add oru view to a footer view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        } else {
            //it's one of our items, display as required
            prepareGeneric((GenericViewHolder) vh, position - headers.size());
        }
    }

    @Override
    public int getItemCount() {
        //make sure the adapter knows to look for all our items, headers, and footers
        return headers.size() + items.size() + footers.size();
    }

    private void prepareHeaderFooter(HeaderFooterViewHolder vh, View view) {
        //empty out our FrameLayout and replace with our header/footer
        vh.base.removeAllViews();
        vh.base.addView(view);
    }

    private void prepareGeneric(GenericViewHolder vh, int position) {
        RecentModel recentItem = items.get(position);
        RxView.clicks(vh.itemView)
                .throttleFirst(CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .map(aVoid -> recentItem)
                .subscribe(mViewClickSubject);
    }

    @Override
    public int getItemViewType(int position) {
        //check what type our position is, based on the assumption that the order is headers > items > footers
        if (position < headers.size()) {
            return TYPE_HEADER;
        } else if (position >= headers.size() + items.size()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    //add a header to the adapter
    public void addHeader(View header) {
        if (!headers.contains(header)) {
            headers.add(header);
            //animate
            notifyItemInserted(headers.size() - 1);
        }
    }

    //remove a header from the adapter
    public void removeHeader(View header) {
        if (headers.contains(header)) {
            //animate
            notifyItemRemoved(headers.indexOf(header));
            headers.remove(header);
            if (header.getParent() != null) {
                ((ViewGroup) header.getParent()).removeView(header);
            }
        }
    }

    //add a footer to the adapter
    public void addFooter(View footer) {
        if (!footers.contains(footer)) {
            footers.add(footer);
            //animate
            notifyItemInserted(headers.size() + items.size() + footers.size() - 1);
        }
    }

    //remove a footer from the adapter
    public void removeFooter(View footer) {
        if (footers.contains(footer)) {
            //animate
            notifyItemRemoved(headers.size() + items.size() + footers.indexOf(footer));
            footers.remove(footer);
            if (footer.getParent() != null) {
                ((ViewGroup) footer.getParent()).removeView(footer);
            }
        }
    }

    public static class GenericViewHolder extends RecyclerView.ViewHolder {

        public GenericViewHolder(View itemView) {
            super(itemView);
        }
    }

    //our header/footer RecyclerView.ViewHolder is just a FrameLayout
    public static class HeaderFooterViewHolder extends RecyclerView.ViewHolder {
        FrameLayout base;

        public HeaderFooterViewHolder(View itemView) {
            super(itemView);
            this.base = (FrameLayout) itemView;
        }
    }
}
