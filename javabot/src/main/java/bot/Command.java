package bot;


import common.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rmesterov on 10-Aug-17.
 */
public class Command {
    private int id;
    private String name;
    private String description;
    private String channel;

    public Command(){}



    public Command loadByName(String name) {
        Command ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_command where name = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                ret = getDataFromRS(rs);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            ConnectionPool.closeConnection(rs,ps,con);
        }
        return ret;
    }

    private Command getDataFromRS(ResultSet rs) throws SQLException {
        Command ret = new Command();
        ret.setId(rs.getInt("id"));
        ret.setName(rs.getString("name"));
        ret.setDescription(rs.getString("description"));
        ret.setChannel(rs.getString("channel"));
        return ret;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
