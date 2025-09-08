package com.solvd.uber.dao;

import com.solvd.uber.models.Group;
import java.util.List;

public interface IGroupDAO<T extends Group> extends IBaseDAO<T> {
    List<T> getByAccountId(long accountId);
    List<T> getBusinessGroupsByAccountId(long accountId);
    List<T> getPersonalGroupsByAccountId(long accountId);
}
