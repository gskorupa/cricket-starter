/*
 * Copyright 2016 Grzegorz Skorupa <g.skorupa at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cricketmsf.out.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.cricketmsf.Adapter;
import org.cricketmsf.Event;
import org.cricketmsf.Kernel;
import org.cricketmsf.out.OutboundAdapter;
import org.h2.jdbcx.JdbcConnectionPool;

/**
 *
 * @author greg
 */
public class H2Remote extends OutboundAdapter implements H2DatabaseIface, Adapter {

    JdbcConnectionPool cp;
    private String uri;
    private String user;
    private String password;
    private int dbVersion = -1;
    private String versionName = null;
    private boolean started = false;
    final private String VERSION_QUERY = "SELECT VERSION, NAME, DESCRIPTION FROM DBVERSION";
    
    private int actualVersion = 0;

    public int getActualVersion(){
        return actualVersion;
    }
    
    // jdbc:h2:tcp//docker37080-env-6265168.unicloud.pl/opt-h2data/test:CRICKET
    // cricket
    // alamakota
    @Override
    public void loadProperties(HashMap<String, String> properties, String adapterName) {
        uri = properties.get("uri");
        System.out.println("\turi=" + uri);
        user = properties.get("user");
        System.out.println("\tuser=" + user);
        password = properties.get("password");
        System.out.println("\tpassword=" + password);
        actualVersion = 1;
        start();
    }

    @Override
    public int readVersion() {
        Connection conn = getConnection();
        if(conn == null){
            return -1;
        }
        try {
            ResultSet rs = conn.createStatement().executeQuery(VERSION_QUERY);
            while (rs.next()) {
                dbVersion = rs.getInt("VERSION");
                versionName = rs.getString("NAME");
                break;
            }
            conn.close();
        } catch (SQLException e) {
            Kernel.getInstance().handleEvent(
                    new Event(
                            getClass().getSimpleName(),
                            Event.CATEGORY_LOG,
                            Event.LOG_SEVERE,
                            "",
                            e.getMessage()
                    )
            );
        }
        return dbVersion;
    }
    
    @Override
    public int getVersion(){
        return dbVersion;
    }
    
    @Override
    public String getVersionName(){
        return versionName;
    }

    @Override
    public void start() {
        cp = JdbcConnectionPool.create("jdbc:h2:" + uri, user, password);
        Connection conn;
        try {
            conn = cp.getConnection();
            started = false;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        try {
            int version = getVersion();
            if (version < 0) {
                createDatabase(conn);
            }
            upgrade(conn, version);
            conn.close();
        } catch (SQLException e) {
            createDatabase(conn);
        } finally {
            if(conn!=null){
                try{
                    conn.close();
                }catch(SQLException e){}
            }
        }
    }

    @Override
    public void createDatabase(Connection conn) {
        if (conn == null) {
            Kernel.getInstance().handleEvent(
                    new Event(
                            this.getClass().getSimpleName(),
                            Event.CATEGORY_LOG,
                            Event.LOG_SEVERE,
                            "",
                            "unable to create database")
            );
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE DBVERSION(VERSION INT, NAME VARCHAR, DESCRIPTION VARCHAR);");
        sb.append("INSERT INTO DBVERSION VALUES(1, '1.0', 'INITIAL')");

        String createQuery = sb.toString();

        try {
            conn.createStatement().execute(createQuery);
        } catch (SQLException e) {
            Kernel.getInstance().handleEvent(
                    new Event(
                            this.getClass().getSimpleName(),
                            Event.CATEGORY_LOG,
                            Event.LOG_SEVERE,
                            "",
                            e.getMessage())
            );
        }
    }
    
    @Override
    public void upgrade(Connection conn, int fromVersion){
        if(fromVersion == getActualVersion()){
            return;
        }
        int toVersion = getActualVersion();
        
        switch (fromVersion) {
            case 0:
                break;
        }
        
    }

    @Override
    public void destroy() {
        cp.dispose();
    }

    @Override
    public Connection getConnection() {
        Connection conn = null;
        try{
            conn = cp.getConnection();
        }catch(SQLException e){
            Kernel.getInstance().handleEvent(
                    new Event(
                            this.getClass().getSimpleName(),
                            Event.CATEGORY_LOG,
                            Event.LOG_SEVERE,
                            "",
                            e.getMessage())
            );
        }
        return conn;
    }

}
