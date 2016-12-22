package com.ebr163.probetools;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import com.ebr163.probetools.http.IProbeHttpInterceptor;
import com.ebr163.probetools.manager.AssetsManager;
import com.ebr163.probetools.manager.BaseManager;
import com.ebr163.probetools.manager.DBManager;
import com.ebr163.probetools.manager.HttpInterceptManager;
import com.ebr163.probetools.manager.IndexManager;
import com.ebr163.probetools.manager.ManagerFactory;
import com.ebr163.probetools.manager.PreferencesManager;
import com.ebr163.probetools.manager.TransitionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public final class Router {

    private Map<Class<? extends BaseManager>, BaseManager> managers = new HashMap<>();
    private ManagerFactory managerFactory;
    private Context context;
    private SharedPreferences preferences;
    private Map<String, SQLiteOpenHelper> databases;
    private IProbeHttpInterceptor httpInterceptor;

    Router(Context context) {
        this.context = context;
        managers = new HashMap<>();
        managerFactory = new ManagerFactory();
        databases = new LinkedHashMap<>();
    }

    private ManagerFactory getManagerFactory() {
        return managerFactory;
    }

    private <T extends BaseManager> T create(Class<T> controller) {
        return getManagerFactory().build(context, controller);
    }

    public <T extends BaseManager> T registerController(T manager) {
        managers.put(manager.getClass(), manager);
        return (T) manager.setRouter(this);
    }

    public <T extends BaseManager> T getManager(Class<T> manager) {
        if (!managers.containsKey(manager)) {
            return registerController(create(manager));
        }

        return (T) managers.get(manager);
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return preferences;
    }

    public SQLiteOpenHelper getSqLiteOpenHelper(String name) {
        return databases.get(name);
    }

    void putDatabase(String name, SQLiteOpenHelper sqLiteOpenHelper) {
        databases.put(name, sqLiteOpenHelper);
    }


    void setHttpInterceptor(IProbeHttpInterceptor httpInterceptor) {
        this.httpInterceptor = httpInterceptor;
    }

    public List<String> getDatabaseNames() {
        return new ArrayList<>(databases.keySet());
    }

    public IProbeHttpInterceptor getProbeHttpInterceptor() {
        return httpInterceptor;
    }

    NanoHTTPD.Response route(NanoHTTPD.IHTTPSession session) throws Exception {
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
        } else if (session.getUri().matches("/addPreference") && "POST".equals(session.getMethod().name())) {
            return getManager(PreferencesManager.class).addPreference(session);
        } else if (session.getUri().matches("/loadTableNames") && "GET".equals(session.getMethod().name())) {
            return getManager(DBManager.class).loadAllTableNames(session);
        } else if (session.getUri().matches("/loadTable") && "GET".equals(session.getMethod().name())) {
            return getManager(DBManager.class).loadTable(session);
        } else if (session.getUri().matches("/runSQL") && "POST".equals(session.getMethod().name())) {
            return getManager(DBManager.class).runSQL(session);
        } else if (session.getUri().matches("/loadDatabases") && "GET".equals(session.getMethod().name())) {
            return getManager(DBManager.class).loadDatabases(session);
        } else if (session.getUri().contains("database") && "GET".equals(session.getMethod().name())) {
            return getManager(DBManager.class).transition(session);
        } else if (session.getUri().contains("/http/request") && "GET".equals(session.getMethod().name())) {
            return getManager(HttpInterceptManager.class).getRequestData(session);
        } else if (session.getUri().contains("/http/response") && "GET".equals(session.getMethod().name())) {
            return getManager(HttpInterceptManager.class).getResponseData(session);
        }
        return null;
    }
}
