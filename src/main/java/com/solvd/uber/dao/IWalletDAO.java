package com.solvd.uber.dao;

import com.solvd.uber.models.Wallet;

public interface IWalletDAO<T extends Wallet> extends IBaseDAO<T> {
    T getByAccountId(long accountId);
}
