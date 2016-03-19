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

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.cricketmsf.Event;
import org.cricketmsf.Kernel;
import org.cricketmsf.out.OutboundAdapter;
import org.h2.jdbcx.JdbcConnectionPool;

/**
 *
 * @author greg
 */
public class H2Embeded extends OutboundAdapter implements H2EmbededIface {

    JdbcConnectionPool cp;
    String location;
    String path;
    String fileName;
    String testQuery;

    public void loadProperties(HashMap<String, String> properties) {
        path = properties.get("path");
        System.out.println("path=" + path);
        fileName = properties.get("file");
        System.out.println("file=" + fileName);
        location = path + File.separator + fileName;
        testQuery = properties.get("test-query");
        System.out.println("test-query=" + testQuery);
        start();
    }

    @Override
    public void start() {
        cp = JdbcConnectionPool.create("jdbc:h2:" + location, "sa", "sa");
        Connection conn = null;
        try {
            conn = cp.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(testQuery);
            while (rs.next()) {
                System.out.println("service database version: " + rs.getString("VERSION"));
            }
            conn.close();
        } catch (SQLException e) {
            createDatabase(conn);
        }
    }

    public void createDatabase(Connection conn) {
        if (conn == null || testQuery == null || testQuery.isEmpty()) {
            Kernel.getInstance().handleEvent(
                    new Event(
                            this.getClass().getSimpleName(),
                            Event.CATEGORY_LOG,
                            Event.LOG_SEVERE,
                            "",
                            "problem connecting to the database")
            );
            return;
        }
        String createQuery
                = "CREATE TABLE SERVICEVERSION(VERSION VARCHAR);"
                + "INSERT INTO SERVICEVERSION VALUES('1.0')";
        try {
            conn.createStatement().execute(createQuery);
            conn.close();
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
    public void destroy() {
        cp.dispose();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return cp.getConnection();
    }

    @Override
    public String getVersion() {
        String version = null;
        try {
            Connection conn = getConnection();
            ResultSet rs = conn.createStatement().executeQuery("select * from serviceversion");
            while (rs.next()) {
                version = rs.getString("version");
            }
            conn.close();
        } catch (SQLException ex) {
            Kernel.getInstance().handleEvent(
                    new Event(
                            getClass().getSimpleName(),
                            Event.CATEGORY_LOG,
                            Event.LOG_SEVERE,
                            "",
                            ex.getMessage()
                    )
            );
        }
        return version;
    }

}
