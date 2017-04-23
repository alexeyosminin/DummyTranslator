package com.osminin.dummytranslater.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.osminin.dummytranslater.R;
import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.models.TranslationModel;
import com.osminin.dummytranslater.presentation.interfaces.FavoritesPresenter;
import com.osminin.dummytranslater.ui.base.BaseActivity;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static com.osminin.dummytranslater.Config.TRANSLATION_MODEL_KEY;

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
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void addItem(TranslationModel model) {
        Timber.d("addItem: ");
        mAdapter.addItem(model);
    }

    @Override
    public Observable<TranslationModel> itemClickObservable() {
        Timber.d("itemClickObservable: ");
        return mAdapter.mViewClickSubject.cast(TranslationModel.class);
    }

    @Override
    public void finishView(TranslationModel model) {
        Timber.d("finishView: ");
        setResult(RESULT_OK, new Intent().putExtra(TRANSLATION_MODEL_KEY, model));
        supportFinishAfterTransition();
    }

    @Override
    public void clearList() {
        Timber.d("clearList: ");
        mAdapter.clearList();
    }

    class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int CLICK_TIMEOUT = 500;
        private List<TranslationModel> mData = new LinkedList<>();
        PublishSubject<TranslationModel> mViewClickSubject = PublishSubject.create();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_card_layout, parent, false);
            return new FavoritesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((FavoritesViewHolder) holder).mPrimaryText.setText(mData.get(position).getPrimaryText());
            RxView.clicks(holder.itemView)
                    .throttleFirst(CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                    .map(aVoid -> mData.get(position))
                    .subscribe(mViewClickSubject);
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
