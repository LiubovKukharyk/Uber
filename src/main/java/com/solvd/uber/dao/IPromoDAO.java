package com.solvd.uber.dao;

import com.solvd.uber.models.Promo;
import java.util.List;

public interface IPromoDAO<T extends Promo> extends IBaseDAO<T> {
    List<T> getByPersonId(long personId);
}
