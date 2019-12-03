package com.minhtzy.moneytracker.wallet;

import com.minhtzy.moneytracker.entity.WalletEntity;

public interface OnWalletItemInteractionListener {
    void onWalletItemClicked(WalletEntity entity);
}
