package bot;

import common.Common;
import common.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmesterov on 10-Aug-17.
 */
public class Step {
    private int id;
    private String name;
    private int next_step_id;
    private int question_id;
    private int command_id;
    private boolean is_finish;
    private Question question;

    public boolean isIs_finish() {
        return is_finish;
    }

    public void setIs_finish(boolean is_finish) {
        this.is_finish = is_finish;
    }

    public int getNext_step_id() {
        return next_step_id;
    }

    public void setNext_step_id(int next_step_id) {
        this.next_step_id = next_step_id;
    }
    public Step(){}


    public List<Step> loadByCommandId(long command_id) {
        List<Step> ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_step where command_id = ?");
            ps.setLong(1, command_id);
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
    public Step loadNextStepById(long id) {
        Step ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_step where id = (" +
                    "select next_step_id from front.creditbot_step where id = ?" +
                    ");");
            ps.setLong(1, id);
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
    public Step loadFirstStepByCommandId(long command_id) {
        Step ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_step where command_id = ? order by id asc limit 1;"
                    );
            ps.setLong(1, command_id);
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
    public Step loadById(long id) {
        Step ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_step where id = ?");
            ps.setLong(1, id);
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

    private Step getDataFromRS(ResultSet rs) throws SQLException {
        Step ret = new Step();
        ret.setId(rs.getInt("id"));
        ret.setName(rs.getString("name"));
        ret.setNext_step_id(rs.getInt("next_step_id"));
        ret.setQuestion_id(rs.getInt("question_id"));
        ret.setCommand_id(rs.getInt("command_id"));
        ret.setIs_finish(rs.getBoolean("is_finish"));
        return ret;
    }

    public boolean validate (String text2vatidate){
        return getQuestion().getParam_reqexp()==null || getQuestion().getParam_reqexp().isEmpty() || Common.checkReqExp(text2vatidate, getQuestion().getParam_reqexp());
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
    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
        question=null;
    }

    public int getCommand_id() {
        return command_id;
    }

    public void setCommand_id(int command_id) {
        this.command_id = command_id;
    }

    public Question getQuestion() {
        if (question==null){
            question=new Question().loadById(question_id);
        }
        return question;
    }
}
