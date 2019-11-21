package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.entity.WalletEntity;

import java.util.List;

public interface IWalletsDAO {
    public boolean insertWallet(WalletEntity wallet);
    public boolean updateWallet(WalletEntity wallet) ;
    public boolean deleteWallet(String walletId) ;
    public WalletEntity getWalletById(String id);
    public boolean hasWallet(String userId);
    public List<WalletEntity> getAllWalletByUser(String userId);
}
