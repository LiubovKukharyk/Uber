package com.solvd.uber.dao;

import com.solvd.uber.models.Payment;
import java.util.List;

public interface IPaymentDAO<T extends Payment> extends IBaseDAO<T> {
    List<T> getByWalletId(long walletId);
    List<T> getByRideId(long rideId);
}
