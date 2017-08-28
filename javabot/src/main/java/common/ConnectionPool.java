package common;

import org.postgresql.ds.PGPoolingDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rmesterov on 28-Aug-17.
 */
public class ConnectionPool {
    public static PGPoolingDataSource source = null;
    private static PGPoolingDataSource sourceDWH = null;

    public static Connection getConnection() throws SQLException {
        Connection con = null;
        try {
            if (source == null) {
                source = PGPoolingDataSource.getDataSource("FRONT");
                if (source != null) {
                    source.close();
                    source = null;
                }
                source = new PGPoolingDataSource();
                source.setDataSourceName("FRONT");
                //source.setServerName("134.168.55.219:5432");
                //if (Global.applicationName.equals("BPM")) source.setServerName(System.getenv("DB_FRONT"));
                //		else
                source.setServerName(("DB.ServerName"));//TODO
                String user=null;
                String password=null;

                source.setDatabaseName("dbName");
                source.setUser(user);//TODO
                source.setPassword(password);//TODO
                source.setMaxConnections(25);
                source.setSocketTimeout(30);
            }
            con = source.getConnection();
            return con;
        } catch (SQLException e) {
            source = null;
            System.err.println(e);
            throw e;
        }
    }

    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
            }
        }
    }

    public static void closeConnection(ResultSet rs, PreparedStatement ps, Connection con ){
        try {
            if (rs !=null) rs.close();
            if (ps !=null) ps.close();
            if (con !=null) ConnectionPool.closeConnection(con);
        } catch (SQLException e) {
            System.err.println(e);
        }
        rs = null;
        ps = null;
    }
}
