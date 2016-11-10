package com.ebr163.webserver;

import android.content.Context;
import android.content.SharedPreferences;

import com.ebr163.webserver.manager.AssetsManager;
import com.ebr163.webserver.manager.BaseManager;
import com.ebr163.webserver.manager.DBManager;
import com.ebr163.webserver.manager.HttpInterceptManager;
import com.ebr163.webserver.manager.IndexManager;
import com.ebr163.webserver.manager.PreferencesManager;
import com.ebr163.webserver.manager.TransitionManager;
import com.ebr163.webserver.manager.util.ManagerFactory;

import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class Router {

    private Map<Class<? extends BaseManager>, BaseManager> managers = new HashMap<>();
    private ManagerFactory managerFactory;
    private Context context;
    private String dbName;
    private SharedPreferences preferences;

    public Router(Context context) {
        this.context = context;
        managerFactory = new ManagerFactory();
    }

    public ManagerFactory getManagerFactory() {
        return managerFactory;
    }

    private <T extends BaseManager> T create(Class<T> controller) {
        return (T) getManagerFactory().build(context, controller);
    }

    public BaseManager registerController(BaseManager manager) {
        return manager.setRouter(this);
    }

    public <T extends BaseManager> T getManager(Class<T> manager) {
        if (!managers.containsKey(manager)) {
            return (T) registerController(create(manager));
        }

        return (T) managers.get(manager);
    }

    public String getDbName() {
        return dbName;
    }

    void setDBName(String dbName) {
        this.dbName = dbName;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public NanoHTTPD.Response route(NanoHTTPD.IHTTPSession session) throws Exception {
        if (session.getUri().contains(".html") && "GET".equals(session.getMethod().name())) {
            return getManager(TransitionManager.class).transition(session);
        } else if (session.getUri().matches("/assets/.*")) {
            return getManager(AssetsManager.class).asset(session);
        } else if (session.getUri().matches("/") && "GET".equals(session.getMethod().name())) {
            return getManager(IndexManager.class).transition(session);
        } else if (session.getUri().matches("/db/download/database") && "GET".equals(session.getMethod().name())) {
            return getManager(DBManager.class).download(session);
        } else if (session.getUri().matches("/preferences/loadAll") && "GET".equals(session.getMethod().name())) {
            return getManager(PreferencesManager.class).loadAllPreferences(session);
        }
        return null;
    }
}
