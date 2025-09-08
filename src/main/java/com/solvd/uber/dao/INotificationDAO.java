package com.solvd.uber.dao;

import com.solvd.uber.models.Notification;
import java.util.List;

public interface INotificationDAO<T extends Notification> extends IBaseDAO<T> {
    List<T> getByPersonId(long personId);
}
