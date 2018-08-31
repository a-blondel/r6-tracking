package com.rainbow6.siege.r6_app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.rainbow6.siege.r6_app.db.entity.ConnectEntity;
import com.rainbow6.siege.r6_app.repository.ConnectRepository;

public class ConnectViewModel extends AndroidViewModel {

    private ConnectRepository mConnectRepository;

    public ConnectViewModel(Application application) {
        super(application);
        mConnectRepository = new ConnectRepository(application);
    }

    public void insert(ConnectEntity connectEntity) { mConnectRepository.insert(connectEntity); }

    public void update(ConnectEntity connectEntity) { mConnectRepository.update(connectEntity); }

//    getConnect (appId)
//    getEncodedKey (lors du refresh du token)
//    getTicketWithExpirationDate (lors d'un appel à un service avec date valide)
//    setTicketAndExpirationDate (lors du resfresh)
//    setServicesDelay ?

}
