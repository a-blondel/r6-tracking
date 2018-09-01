package com.rainbow6.siege.r6_app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;
import com.rainbow6.siege.r6_app.repository.ConnectionRepository;

public class ConnectionViewModel extends AndroidViewModel {

    private ConnectionRepository mConnectRepository;

    public ConnectionViewModel(Application application) {
        super(application);
        mConnectRepository = new ConnectionRepository(application);
    }

    public void insert(ConnectionEntity connectionEntity) { mConnectRepository.insert(connectionEntity); }

    public ConnectionEntity getConnect(String appId) { return mConnectRepository.getConnection(appId); }

}
