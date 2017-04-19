package com.osminin.dummytranslater.ui.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.osminin.dummytranslater.R;
import com.osminin.dummytranslater.models.TranslationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by osminin on 3/20/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_INPUT = 111;
    public static final int TYPE_TRANSLATION = 222;
    public static final int TYPE_RECENT = 333;
    private static final int CLICK_TIMEOUT = 500;
    private List<TranslationModel> mRecents = new ArrayList<>();
    private List<View> mInput = new ArrayList<>();
    private List<View> mTranslation = new ArrayList<>();

    private PublishSubject<TranslationModel> mViewClickSubject = PublishSubject.create();

    public Observable<TranslationModel> getViewClickedObservable() {
        return mViewClickSubject;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        Timber.v("onCreateViewHolder: ");
        RecyclerView.ViewHolder res;
        View view;
        if (type == TYPE_RECENT) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recent_card_layout, viewGroup, false);
            res = new RecentViewHolder(view);
            //else we have a header/footer
        } else {
            //create a new framelayout, or inflate from a resource
            view = new FrameLayout(viewGroup.getContext());
            //make sure it fills the space
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            res = new HeaderFooterViewHolder(view);
        }
        return res;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
        Timber.v("onBindViewHolder: %d", position);
        //check what type of view our position is
        if (position < mInput.size()) {
            View v = mInput.get(position);
            //add our view to a header view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        } else if (position >= mInput.size() && position < mTranslation.size() + mInput.size()) {
            View v = mTranslation.get(position - mInput.size());
            //add oru view to a footer view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        } else {
            //it's one of our mRecents, display as required
            prepareGeneric((RecentViewHolder) vh, position - mInput.size() - mTranslation.size());
            ((RecentViewHolder) vh).mPrimaryText
                    .setText(mRecents.get(position - mInput.size() - mTranslation.size()).getPrimaryText());
            ((RecentViewHolder) vh).favoriteIcon.setVisibility(
                    mRecents.get(position - mInput.size() - mTranslation.size()).isFavorite()
                            ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        //make sure the adapter knows to look for all our mRecents, mInput, and mTranslation
        return mInput.size() + mRecents.size() + mTranslation.size();
    }

    private void prepareHeaderFooter(HeaderFooterViewHolder vh, View view) {
        Timber.v("prepareHeaderFooter: ");
        //empty out our FrameLayout and replace with our header/footer
        vh.base.removeAllViews();
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        vh.base.addView(view);
    }

    private void prepareGeneric(RecentViewHolder vh, int position) {
        Timber.v("prepareGeneric: %d", position);
        RxView.clicks(vh.itemView)
                .throttleFirst(CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .map(aVoid -> mRecents.get(position))
                .subscribe(mViewClickSubject);
    }

    @Override
    public int getItemViewType(int position) {
        Timber.v("getItemViewType: %d", position);
        //check what type our position is, based on the assumption that the order is mInput > mRecents > mTranslation
        if (position < mInput.size()) {
            return TYPE_INPUT;
        } else if (position >= mInput.size() + mTranslation.size()) {
            return TYPE_RECENT;
        }
        return TYPE_TRANSLATION;
    }

    //add a header to the adapter
    public void addInputCard(View header) {
        Timber.d("addInputCard: ");
        if (!mInput.contains(header)) {
            mInput.add(header);
            //animate
            notifyItemInserted(mInput.size() - 1);
        }
    }

    //remove a inputCard from the adapter
    public void removeInputCard(View inputCard) {
        Timber.d("removeInputCard: ");
        if (mInput.contains(inputCard)) {
            //animate
            notifyItemRemoved(mInput.indexOf(inputCard));
            mInput.remove(inputCard);
            if (inputCard.getParent() != null) {
                ((ViewGroup) inputCard.getParent()).removeView(inputCard);
            }
        }
    }

    //add a translationCard to the adapter
    public void addTranslationCard(View translationCard) {
        Timber.d("addTranslationCard: ");
        if (!mTranslation.contains(translationCard)) {
            mTranslation.add(translationCard);
            //animate
            notifyItemInserted(mInput.size());
        }
    }

    //remove a translationCard from the adapter
    public void removeTranslationCard(View translationCard) {
        Timber.d("removeTranslationCard: ");
        if (mTranslation.size() > 0) {
            //animate
            notifyItemRemoved(mInput.size());
            mTranslation.remove(translationCard);
            if (translationCard.getParent() != null) {
                ((ViewGroup) translationCard.getParent()).removeView(translationCard);
            }
        }
    }

    public void setRecents(List<TranslationModel> recents) {
        Timber.d("setRecents: ");
        this.mRecents = recents;
        notifyDataSetChanged();
    }

    public void clearRecent() {
        Timber.d("clearRecent: ");
        int count = mRecents.size();
        mRecents.clear();
        notifyItemRangeRemoved(mInput.size() + mTranslation.size() - 1, count);
    }

    public void addRecentItem(TranslationModel item) {
        Timber.d("addRecentItem: %s", item);
        mRecents.add(item);
        notifyItemChanged(mRecents.size() - 1);
    }

    public static class RecentViewHolder extends RecyclerView.ViewHolder {

        TextView mPrimaryText;
        View favoriteIcon;

        public RecentViewHolder(View itemView) {
            super(itemView);
            mPrimaryText = ButterKnife.findById(itemView, R.id.recent_primary_text);
            favoriteIcon = ButterKnife.findById(itemView, R.id.recent_favorite_icon);
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
