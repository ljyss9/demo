package com.ljy.DAO;

import com.ljy.Model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created by ljy on 2017/2/14.
 */
@Mapper
public interface LoginTicketDAO {

    String TABLE_NAME = "loginticket";
    String  INSERT_FIELDS = " userId, expired, status, ticket ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values (#{userId},#{expired},#{status},#{ticket})"})
    int  addTicket(LoginTicket tickes);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);


    @Update({"update ",TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
