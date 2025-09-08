package com.solvd.uber.dao;

public interface ILoginDAO {
    boolean login(String email, String password);
    void logout(long accountId);
    void resetPassword(long accountId, String newPassword);
}
