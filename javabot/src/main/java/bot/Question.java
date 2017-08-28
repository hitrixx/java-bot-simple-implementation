package bot;


import common.Common;
import common.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by rmesterov on 10-Aug-17.
 */
public class Question {
    private int id;
    private String name;
    private String question_text_en;
    private String question_text_loc1;
    private String question_text_loc2;
    private String param_name;
    private String param_reqexp;
    private String invalid_answer_text;
    private Map<String,Object> params=null;
    public String getInvalid_answer_text() {
        return invalid_answer_text;
    }

    public void setInvalid_answer_text(String invalid_answer_text) {
        this.invalid_answer_text = invalid_answer_text;
    }

    private int command_id; // id of the path chose by client

    public Question(){}


    public Question loadById(long id) {
        Question ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_question where id = ?");
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


    public Question loadByName(String name) {
        Question ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_question where name = ?");
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

    public Question loadByParamName(String param_name) {
        Question ret = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionPool.getConnection();
            ps = con.prepareStatement("select * from front.creditbot_question where param_name = ?");
            ps.setString(1, param_name);
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
    private Question getDataFromRS(ResultSet rs) throws SQLException {
        Question ret = new Question();
        ret.setId(rs.getInt("id"));
        ret.setName(rs.getString("name"));
        ret.setQuestion_text_en(rs.getString("question_text_en"));
        ret.setQuestion_text_loc1(rs.getString("question_text_loc1"));
        ret.setQuestion_text_loc2(rs.getString("question_text_loc2"));
        ret.setParam_name(rs.getString("param_name"));
        ret.setParam_reqexp(rs.getString("param_reqexp"));
        ret.setCommand_id(rs.getInt("command_id"));
        ret.setInvalid_answer_text(rs.getString("invalid_answer_text"));

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

    public void setParams(Map<String,Object> params) {
        this.params=params;
    }


    public String getQuestion_text_en() {
            return processText(question_text_en);
    }

    public void setQuestion_text_en(String question_text_en) {
        this.question_text_en = question_text_en;
    }

    public String getQuestion_text_loc1() {
        return processText(question_text_loc1);
    }

    public void setQuestion_text_loc1(String question_text_loc1) {
        this.question_text_loc1 = question_text_loc1;
    }

    public String getQuestion_text_loc2() {
        return processText(question_text_loc2);
    }

    public void setQuestion_text_loc2(String question_text_loc2) {
        this.question_text_loc2 = question_text_loc2;
    }

    public String getParam_name() {
        return param_name;
    }

    public void setParam_name(String param_name) {
        this.param_name = param_name;
    }

    public String getParam_reqexp() {
        return param_reqexp;
    }

    public void setParam_reqexp(String param_reqexp) {
        this.param_reqexp = param_reqexp;
    }

    public int getCommand_id() {
        return command_id;
    }

    public void setCommand_id(int command_id) {
        this.command_id = command_id;
    }

    private String processText(String text) {
        String ret = text;
        if (params != null) {
            try {
                ret = Common.generateTemplate(text, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
//        params.clear();
        params=null;
    }
}
