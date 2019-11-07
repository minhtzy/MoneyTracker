package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.model.Wallet;

import java.util.List;

public interface IWalletsDAO {

    public boolean insertWallet(Wallet wallet);
    public boolean updateWallet(Wallet wallet) ;
    public boolean deleteWallet(long walletId) ;
    public Wallet getWalletById(long id);
    public boolean hasWallet(String userId);
    public List<Wallet> getAllWalletByUser(String userId);

    public void updateTimeStamp(long walletId, long timestamp);

}
