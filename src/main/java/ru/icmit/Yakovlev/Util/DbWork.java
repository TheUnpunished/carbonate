package ru.icmit.Yakovlev.Util;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DbWork{

    private static DbWork dbWork;

    private Connection connection;

    public static DbWork getInstance(){

        if (dbWork==null){

            dbWork = new DbWork();

        }

        return dbWork;

    }

    public void setConnection() throws SQLException, ClassNotFoundException {

        if (this.connection == null || this.connection.isClosed()){

            Properties p = getProperties();

            String user = p.getProperty("db.user");

            String password = p.getProperty("db.password");

            String url = p.getProperty("db.url");

            String drivetClassName = p.getProperty("db.driverClassName");

            Class.forName(drivetClassName);

            this.connection = DriverManager.getConnection(url, user, password);

        }

    }

    public Connection getConnection() {

        return this.connection;

    }

    private Properties getProperties(){

        Properties prop = new Properties();

        try {

            prop.load(getClass().getResourceAsStream("/application.properties"));

        } catch (IOException e) {

            e.printStackTrace();

        }

        return prop;

    }

    public void close(){

        try {

            connection.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public Long generateId(String sequenceName){

        Long id = null;

        String sql = "select nextval( ? ) as id";

        try (PreparedStatement st = getConnection().prepareStatement(sql)){

            st.setString(1, sequenceName);

            ResultSet rs = st.executeQuery();

            if (rs.next()){

                id = rs.getLong("id");

            }

            rs.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return id;

    }

}