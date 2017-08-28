package bot;

import common.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmesterov on 10-Aug-17.
 */
public class User {
    private int id;
    private long chat_id;
    private String first_name;
    private String last_name;
    private String language_code;
    private Timestamp created;
    private Timestamp updated;
    private long client_id;
    private String channel;

    public User(){};



    public User loadByChatId(long chat_id) {
        User user = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.bot_user where chat_id = ? and channel!='mdfin_errors'");
            ps.setLong(1, chat_id);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = getDataFromRS(rs);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            ConnectionPool.closeConnection(rs,ps,con);
        }
        return user;
    }

    public List<User> loadByChannel(String channel) {
        List<User> ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.bot_user where channel = ?");
            ps.setString(1, channel);
            rs = ps.executeQuery();
            ret = new ArrayList<>();
            while (rs.next()){
                ret.add(getDataFromRS(rs));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            ConnectionPool.closeConnection(rs,ps,con);
        }
        return ret;
    }
    private User getDataFromRS(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setChat_id(rs.getInt("chat_id"));
        user.setFirst_name(rs.getString("first_name"));
        user.setLast_name(rs.getString("last_name"));
        user.setLanguage_code(rs.getString("language_code"));
        user.setCreated(rs.getTimestamp("created"));
        user.setUpdated(rs.getTimestamp("updated"));
        user.setClient_id(rs.getInt("client_id"));
        user.setChannel(rs.getString("channel"));
        return user;
    }


    public void save() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectionPool.getConnection();

            if (id ==0) {
                ps = con.prepareStatement("insert into front.bot_user " +
                        "(chat_id, first_name, last_name, language_code, client_id, channel)" +
                        "values (?,?,?,?,?,?) returning id, created");
                ps.setLong(1, chat_id);
                ps.setString(2, first_name);
                ps.setString(3, last_name);
                ps.setString(4, language_code);

                ps.setLong(5, client_id);
                ps.setString(6, channel);
                rs = ps.executeQuery();
                if (rs.next()) {
                    created = rs.getTimestamp("created");
                    id = rs.getInt("id");
                }
            } else {
                String str = "update front.bot_user SET " +
                        "client_id = ?,"+
                        "language_code = ?,"+
                        "channel = ?"+
                        " where id = ?";
                ps = con.prepareStatement(str);
                ps.setLong(1,   client_id);
                ps.setString(2,   language_code);
                ps.setString(3,   channel);

                ps.setLong(4,      id);
                ps.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.closeConnection(rs,ps,con);
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getChat_id() {
        return chat_id;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public long getClient_id() {
        return client_id;
    }

    public void setClient_id(long client_id) {
        this.client_id = client_id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
