package com.example.body;

import com.example.body.Config.DBHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;


@SpringBootApplication
public class Bot2Application {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		SpringApplication.run(Bot2Application.class, args);
		DBHandler dbHandler = new DBHandler();
		dbHandler.getDbConnection();







	}

}
