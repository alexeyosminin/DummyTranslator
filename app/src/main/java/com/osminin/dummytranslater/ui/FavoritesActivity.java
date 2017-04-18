package com.osminin.dummytranslater.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.osminin.dummytranslater.R;
import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.presentation.interfaces.FavoritesPresenter;
import com.osminin.dummytranslater.ui.base.BaseActivity;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by osminin on 4/17/2017.
 */

public final class FavoritesActivity extends BaseActivity implements FavoritesView {

    @Inject
    FavoritesPresenter mPresenter;

    @BindView(R.id.favorites_recycler)
    RecyclerView mRecyclerView;

    private FavoritesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);
        mPresenter.bind(this);
        mAdapter = new FavoritesAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        Timber.d("onStart: ");
        super.onStart();
        mPresenter.startObserveUiEvents();
    }

    @Override
    protected void onStop() {
        Timber.d("onStop: ");
        super.onStop();
        mPresenter.stopObserveUiEvents();
    }

    @Override
    public void addItem(TranslationModel model) {
        Timber.d("addItem: ");
        mAdapter.addItem(model);
    }

    @Override
    public void clearList() {
        Timber.d("clearList: ");
        mAdapter.clearList();
    }

    class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<TranslationModel> mData = new LinkedList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_card_layout, parent, false);
            return new FavoritesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((FavoritesViewHolder) holder).mPrimaryText.setText(mData.get(position).getPrimaryText());
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        void addItem(TranslationModel item) {
            mData.add(item);
            notifyItemChanged(mData.size() - 1);
        }

        void clearList() {
            mData.clear();
            notifyDataSetChanged();
        }

        class FavoritesViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.recent_primary_text)
            TextView mPrimaryText;
            @BindView(R.id.recent_favorite_icon)
            View mFavoriteIcon;

            public FavoritesViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
