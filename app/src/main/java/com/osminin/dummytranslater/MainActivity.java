package com.osminin.dummytranslater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.osminin.dummytranslater.application.App;
import com.osminin.dummytranslater.network.NetworkComponent;
import com.osminin.dummytranslater.network.TranslatorService;
import com.osminin.dummytranslater.network.modules.NetworkModule;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.osminin.dummytranslater.Config.API_KEY;

public class MainActivity extends AppCompatActivity {

    @Inject
    TranslatorService mTranslatorService;

    private NetworkComponent mNetworkComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetworkComponent = App.getComponent().plusNetworkComponent(new NetworkModule());
        mNetworkComponent.inject(this);

        mTranslatorService.translate("en-ru", API_KEY, "hello world")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> Log.d("response", response.getText().get(0)));
    }
}
