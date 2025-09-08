package com.solvd.uber.dao;

import com.solvd.uber.models.Account;

public interface IAccountDAO<T extends Account> extends IBaseDAO<T> {
    T getByPersonId(long personId);
}
