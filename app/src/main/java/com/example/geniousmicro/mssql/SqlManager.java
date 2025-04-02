package com.example.geniousmicro.mssql;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlManager {

    public Connection getSQLConnection(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String driver="net.sourceforge.jtds.jdbc.Driver";
        try

        {
            Class.forName(driver).newInstance();
            String connString="jdbc:jtds:sqlserver://" + "52.66.61.62:1433" + ";" + "databaseName=" +
                    "GTECH_3103GTMCDM" + ";user=" + "DEV_AVJT62" + ";password=" + "vT2bWb%v!vg5UnbHynEcE" + ";";
            conn= DriverManager.getConnection(connString);
        }
        catch(Exception ex){
            conn = null;

        }
        return conn;


    }


}
