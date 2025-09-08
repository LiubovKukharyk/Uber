package com.solvd.uber.dao;

import com.solvd.uber.models.History;
import java.util.List;

public interface IHistoryDAO<T extends History> extends IBaseDAO<T> {


	List<History> getByAccountId(long accountId);
}
