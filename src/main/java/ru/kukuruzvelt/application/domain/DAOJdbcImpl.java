package ru.kukuruzvelt.application.domain;

import org.springframework.stereotype.Component;
import ru.kukuruzvelt.application.model.MovieEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DAOJdbcImpl {

    private static String URL;
    private static String login;
    private static String password;
    private PreparedStatement prStatement;
    private DriverManager driverManager;
    private List<MovieEntity> movieEntityList;



    public MovieEntity findByWebMapping(String title){
        try{
            Connection connection = driverManager.getConnection(URL, login, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM TABLE 'movies'");
            this.movieEntityList = new ArrayList<>(36);
            while(resultSet.next()) {
                MovieEntity me = MovieEntity
                        .builder()
                        .Duration((Integer) resultSet.getObject(0))
                        .TitleRussian((String) resultSet.getObject(1))
                        .TitleOriginal((String) resultSet.getObject(2))
                        .VideoFileName((String) resultSet.getObject(3))
                        .PosterFileName((String) resultSet.getObject(4))
                        .Year(resultSet.getInt(5))
                        .Genres((String[]) resultSet.getObject(6))
                        .Countries((String[]) resultSet.getObject(7))
                        .WebMapping((String) resultSet.getObject(8))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
