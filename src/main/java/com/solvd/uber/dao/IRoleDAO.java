package com.solvd.uber.dao;

import com.solvd.uber.models.Role;
import java.util.List;

public interface IRoleDAO<T extends Role> extends IBaseDAO<T> {
    List<T> getRolesByPersonId(long personId);
}
