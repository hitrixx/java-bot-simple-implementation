package bot;

import common.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rmesterov on 14-Aug-17.
 */
public class ClientAnswer {
    private int id;
    private int bot_user_id;
    private long client_id;
    private String param_name;
    private int question_id;
    private String text;
    private Timestamp created;
    private Timestamp deactivated;

    public ClientAnswer(){}

    public List<ClientAnswer> loadByClientId(long client_id) {
        List<ClientAnswer> ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_user_answer where client_id = ? and deactivated is null");
            ps.setLong(1, client_id);
            rs = ps.executeQuery();
            ret = new ArrayList<>();
            while (rs.next()) {
                ret.add(getDataFromRS(rs));
            }
        } catch (Exception e){
            
            e.printStackTrace();
        } finally {
            ConnectionPool.closeConnection(rs,ps,con);
        }
        return ret;
    }

    public ClientAnswer load(long user_id, String param_name) {
        ClientAnswer ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_user_answer where bot_user_id = ? and param_name=? and deactivated is null");
            ps.setLong(1, user_id);
            ps.setString(2, param_name);
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

    public ClientAnswer load(long user_id, int question_id) {
        ClientAnswer ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_user_answer where bot_user_id = ? and question_id=? and deactivated is null");
            ps.setLong(1, user_id);
            ps.setInt(2, question_id);
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

    public List<ClientAnswer> loadByUserId(long user_id) {
        List<ClientAnswer> ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_user_answer where bot_user_id = ? and deactivated is null");
            ps.setLong(1, user_id);
            rs = ps.executeQuery();
            ret = new ArrayList<>();
            while (rs.next()) {
                ret.add(getDataFromRS(rs));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            ConnectionPool.closeConnection(rs,ps,con);
        }
        return ret;
    }

    public Map<String,Object> getParams (long user_id) {
        Map<String,Object> ret = new HashMap<>();
        loadByUserId(user_id).forEach((a)->{
            ret.put(a.getParam_name(),a.getText());
        });
        return ret;
    }

    private ClientAnswer getDataFromRS(ResultSet rs) throws SQLException {
        ClientAnswer ret = new ClientAnswer();
        ret.setId(rs.getInt("id"));
        ret.setBot_user_id(rs.getInt("bot_user_id"));
        ret.setClient_id(rs.getInt("client_id"));
        ret.setParam_name(rs.getString("param_name"));
        ret.setQuestion_id(rs.getInt("question_id"));
        ret.setText(rs.getString("text"));
        ret.setCreated(rs.getTimestamp("created"));
        ret.setDeactivated(rs.getTimestamp("deactivated"));
        return ret;
    }

    public void save() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectionPool.getConnection();

            if (id !=0 ) {
                deactivate();
            }

            ps = con.prepareStatement("insert into front.creditbot_user_answer " +
                    "(bot_user_id, client_id, param_name, question_id, text)" +
                    "values (?,?,?,?,?) returning id, created");
            ps.setLong(1, bot_user_id);
            ps.setLong(2, client_id);
            ps.setString(3, param_name);
            ps.setInt(4, question_id);
            ps.setString(5, text);

            rs = ps.executeQuery();
            if (rs.next()) {
                created = rs.getTimestamp("created");
                id = rs.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.closeConnection(rs,ps,con);
        }
    }
    public void deactivate() {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConnectionPool.getConnection();
            if (id !=0 ) {
                ps = con.prepareStatement( "update front.creditbot_user_answer SET " +
                        "deactivated = CURRENT_TIMESTAMP"+
                        " where id = ?;");
                ps.setLong(1, id);
                ps.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.closeConnection(null,ps,con);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBot_user_id() {
        return bot_user_id;
    }

    public void setBot_user_id(int bot_user_id) {
        this.bot_user_id = bot_user_id;
    }

    public long getClient_id() {
        return client_id;
    }

    public void setClient_id(long client_id) {
        this.client_id = client_id;
    }

    public String getParam_name() {
        return param_name;
    }

    public void setParam_name(String param_name) {
        this.param_name = param_name;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(Timestamp deactivated) {
        this.deactivated = deactivated;
    }
}
