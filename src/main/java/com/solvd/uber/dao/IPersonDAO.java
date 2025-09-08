package com.solvd.uber.dao;

import com.solvd.uber.models.Person;
import java.util.List;

public interface IPersonDAO<T extends Person> extends IBaseDAO<T> {
    List<T> getByRole(String roleName);
}
