package com.example.body.Config;



import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class DBHandler extends DataBaseConfig {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void addToDB(String name, Long userId, Integer messageId, Long chatId, String username) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO " + Const.TABLE + "(" + Const.NAME + "," + Const.USER_ID + "," + Const.ARTIFACTS + ","  + Const.USERNAME + ","
                +Const.CHAT_ID+ ") " + "VALUES(?,?,?,?,?)";

        PreparedStatement prSt = getDbConnection().prepareStatement(insert);

        prSt.setString(1, name);
        prSt.setLong(2, userId);
        prSt.setInt(3, messageId);
        prSt.setString(4,username);
        prSt.setLong(5,chatId);

        prSt.executeUpdate();
    }

    public void deleteArtifactFromDB(String name, Long userId, Integer messageId) throws SQLException, ClassNotFoundException {
        String insert = "DELETE FROM " + Const.TABLE + " WHERE " + Const.NAME + " = '" + name + "' AND " + Const.USER_ID
                + " = " + userId + " AND " + Const.ARTIFACTS + " = " + messageId;
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.executeUpdate();

    }

    public void deleteFolderFromDB(String name, Long userId) throws SQLException, ClassNotFoundException {
        String insert = "DELETE FROM " + Const.TABLE + " WHERE " + Const.NAME + " = '" + name + "' AND " + Const.USER_ID + " = " + userId;
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.executeUpdate();

    }

    public List<String> selectFoldersNames(Long userId) throws SQLException, ClassNotFoundException {
        String insert = "SELECT DISTINCT " + Const.NAME + " FROM " + Const.TABLE + " WHERE " + Const.USER_ID + '=' + userId;
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        ResultSet resultSet = prSt.executeQuery();


        List<String> list = new ArrayList<>();
        while (resultSet.next()) {
            String k = resultSet.getString("name");
            list.add(k);
        }

        return list;
    }


    public List<Integer> selectArtifact(String name, Long userId) throws SQLException, ClassNotFoundException {
        String insert = "SELECT " + Const.ARTIFACTS + " FROM " + Const.TABLE + " WHERE " + Const.USER_ID + '=' + userId + " AND "
                + Const.NAME + "= '" + name + "'";

        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        ResultSet resultSet = prSt.executeQuery();

        List<Integer> list = new ArrayList<>();
        while (resultSet.next()) {
            Integer k = resultSet.getInt("artifacts");
            list.add(k);
        }
        return list;
    }

    public List<Long> selectUserId() throws SQLException, ClassNotFoundException {
        String insert = "SELECT DISTINCT " + Const.USER_ID + " FROM " + Const.TABLE;

        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        ResultSet resultSet = prSt.executeQuery();

        List<Long> list = new ArrayList<>();
        while (resultSet.next()) {
            Long k = resultSet.getLong("userid");
            list.add(k);
        }
        return list;
    }



    public ResultSet selectTable() throws SQLException, ClassNotFoundException {
        String insert = "SELECT * FROM " + Const.TABLE;

        PreparedStatement prSt = getDbConnection().prepareStatement(insert);

        return prSt.executeQuery();
    }





}
