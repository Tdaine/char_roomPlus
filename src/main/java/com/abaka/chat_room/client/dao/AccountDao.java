package com.abaka.chat_room.client.dao;

import com.abaka.chat_room.client.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author abaka
 * @date 2019/8/12 9:58
 */
public class AccountDao extends BasedDao{

    public boolean register(User user){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            String sql = "insert into user(username, password, brief) values (?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1,user.getUsername());
            statement.setString(2,DigestUtils.md5Hex(user.getPassword()));
            statement.setString(3,user.getBrief());
            int rows = statement.executeUpdate();
            if (rows == 1)
                return true;
        } catch (SQLException e) {
            System.out.println("用户注册失败");
            e.printStackTrace();
        }finally {
            close(connection,statement);
        }
        return false;
    }

    public User login(String userName,String password){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            String sql = "select * from user where username = ? and password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1,userName);
            statement.setString(2,DigestUtils.md5Hex(password));
            resultSet = statement.executeQuery();
            if (resultSet.next()){
                User user =  getUser(resultSet);
                return user;
            }
        } catch (SQLException e) {
            System.out.println("用户登陆失败");
            e.printStackTrace();
        }finally {
            close(connection,statement,resultSet);
        }
        return null;
    }

    public User getUser(ResultSet resultSet){
        User user = new User();
        try {
            user.setId(resultSet.getInt("id"));
            user.setUsername(resultSet.getNString("username"));
            user.setPassword(resultSet.getNString("password"));
            user.setBrief(resultSet.getNString("brief"));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Boolean isExist(String userName){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            String sql = "select * from user where username = ? ";
            statement = connection.prepareStatement(sql);
            statement.setString(1,userName);
            resultSet = statement.executeQuery();
            if (resultSet.next()){
               return true;
            }
        } catch (SQLException e) {
            System.out.println("用户查找失败");
            e.printStackTrace();
        }finally {
            close(connection,statement,resultSet);
        }
        return false;
    }
}
