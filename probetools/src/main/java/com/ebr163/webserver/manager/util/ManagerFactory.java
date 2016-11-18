package com.ebr163.webserver.manager.util;

import android.content.Context;

import com.ebr163.webserver.manager.AssetsManager;
import com.ebr163.webserver.manager.BaseManager;
import com.ebr163.webserver.manager.DBManager;
import com.ebr163.webserver.manager.HttpInterceptManager;
import com.ebr163.webserver.manager.IndexManager;
import com.ebr163.webserver.manager.PreferencesManager;
import com.ebr163.webserver.manager.TransitionManager;

/**
 * Created by mac1 on 02.11.16.
 */

public class ManagerFactory {

    public <T> T build(Context context, Class<T> manager) {
        return (T) create(context, manager);
    }

    protected BaseManager create(Context context, Class manager) {
        if (AssetsManager.class.equals(manager)) {
            return new AssetsManager(context.getAssets());
        }
        if (IndexManager.class.equals(manager)) {
            return new IndexManager();
        }
        if (DBManager.class.equals(manager)) {
            return new DBManager(context);
        }
        if (HttpInterceptManager.class.equals(manager)) {
            return new HttpInterceptManager();
        }
        if (PreferencesManager.class.equals(manager)) {
            return new PreferencesManager();
        }
        if (TransitionManager.class.equals(manager)) {
            return new TransitionManager();
        }
        return null;
    }
}
