package com.rainbow6.siege.r6_app.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.rainbow6.siege.r6_app.Entities.Connect;
import com.rainbow6.siege.r6_app.Entities.Player;
import com.rainbow6.siege.r6_app.Repository.ConnectRepository;

import java.util.List;

public class ConnectViewModel extends AndroidViewModel {

    private ConnectRepository mConnectRepository;

    public ConnectViewModel(Application application) {
        super(application);
        mConnectRepository = new ConnectRepository(application);
    }

    public void insert(Connect connect) { mConnectRepository.insert(connect); }

    public void update(Connect connect) { mConnectRepository.update(connect); }

//    getConnect (appId)
//    getEncodedKey (lors du refresh du token)
//    getTicketWithExpirationDate (lors d'un appel à un service avec date valide)
//    setTicketAndExpirationDate (lors du resfresh)
//    setServicesDelay ?

}
