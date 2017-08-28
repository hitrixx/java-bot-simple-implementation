package bot;


import common.ConnectionPool;

import java.sql.*;

/**
 * Created by rmesterov on 10-Aug-17.
 */
public class Chat {
    private int id;
    private long chat_id;
    private long message_id;
    private int from_user_id;
    private int to_user_id;
    private Timestamp created;
    private Timestamp updated;
    private Timestamp message_date;
    private String message_text;
    private String req;
    private String resp;
    private int step_id;
    private int command_id;
    private Step step;
    public Chat(){}



    public Chat loadByChatId(long chat_id) {
        Chat ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_chat where chat_id = ?");
            ps.setLong(1, chat_id);
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

    public Chat loadLastByFromUserId(long user_id) {
        Chat ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_chat where from_user_id = ? order by id desc limit 1");
            ps.setLong(1, user_id);
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

    public Chat loadLastByToUserId(long user_id) {
        Chat ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_chat where to_user_id = ? order by id desc limit 1");
            ps.setLong(1, user_id);
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
    private Chat getDataFromRS(ResultSet rs) throws SQLException {
        Chat ret = new Chat();
        ret.setId(rs.getInt("id"));
        ret.setChat_id(rs.getInt("chat_id"));
        ret.setCreated(rs.getTimestamp("created"));
        ret.setUpdated(rs.getTimestamp("updated"));
        ret.setMessage_id(rs.getLong("message_id"));
        ret.setFrom_user_id(rs.getInt("from_user_id"));
        ret.setTo_user_id(rs.getInt("to_user_id"));
        ret.setMessage_date(rs.getTimestamp("message_date"));
        ret.setMessage_text(rs.getString("message_text"));
        ret.setReq(rs.getString("req"));
        ret.setResp(rs.getString("resp"));
        ret.setStep_id(rs.getInt("step_id"));
        ret.setCommand_id(rs.getInt("command_id"));
        return ret;
    }


    public void save() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectionPool.getConnection();

            if (id ==0) {
                ps = con.prepareStatement("insert into front.creditbot_chat " +
                        "(chat_id, message_id, from_user_id, to_user_id, message_date, message_text, req, resp, step_id, command_id)" +
                        "values (?,?,?,?,?,?,?,?,?,?) returning id, created");
                ps.setLong(1, chat_id);
                ps.setLong(2, message_id);
                ps.setInt(3, from_user_id);
                ps.setInt(4, to_user_id);
                ps.setTimestamp(5, message_date);
                ps.setString(6, message_text);
                ps.setString(7, req);
                ps.setString(8, resp);
                ps.setInt(9, step_id);
                ps.setInt(10, command_id);

                rs = ps.executeQuery();
                if (rs.next()) {
                    created = rs.getTimestamp("created");
                    id = rs.getInt("id");
                }
            } else {
                String str = "update front.creditbot_chat SET " +
                        "resp = ?"+
                        " where id = ?";
                ps = con.prepareStatement(str);
                ps.setString(1, resp);
                ps.setLong(2,      id);
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

    public long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(long message_id) {
        this.message_id = message_id;
    }

    public int getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(int from_user_id) {
        this.from_user_id = from_user_id;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(int to_user_id) {
        this.to_user_id = to_user_id;
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

    public Timestamp getMessage_date() {
        return message_date;
    }

    public void setMessage_date(Timestamp message_date) {
        this.message_date = message_date;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    public int getStep_id() {
        return step_id;
    }

    public void setStep_id(int step_id) {
        this.step_id = step_id;
        step=null;
    }

    public int getCommand_id() {
        return command_id;
    }

    public void setCommand_id(int command_id) {
        this.command_id = command_id;
    }

    public Step getStep() {
        if(step == null){
            step = new Step().loadById(this.step_id);
        }
        return step;
    }
}
