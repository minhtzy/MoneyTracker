package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.model.Wallet;

import java.util.List;

public interface IWalletsDAO {
    public boolean insertWallet(WalletEntity wallet);
    public boolean updateWallet(WalletEntity wallet) ;
    public boolean deleteWallet(long walletId) ;
    public WalletEntity getWalletById(long id);
    public boolean hasWallet(String userId);
    public List<WalletEntity> getAllWalletByUser(String userId);
}
