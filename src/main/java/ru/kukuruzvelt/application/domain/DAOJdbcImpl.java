package ru.kukuruzvelt.application.domain;

import ru.kukuruzvelt.application.model.MovieEntity;

import java.sql.*;
import java.util.*;


public class DAOJdbcImpl implements DAO {

    private static String URL = "jdbc:postgresql://localhost:5432/";
    private static String login = "postgres";
    private static String password = "";
    private DriverManager driverManager ;

    public MovieEntity findByWebMapping(String webmapping) {
        String query="SELECT * FROM public.movies WHERE webmapping = '" + webmapping +"'";
        System.out.println(query);
        try (Connection connection = driverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)){
            resultSet.next();
            MovieEntity me = MovieEntity
                    .builder()
                    .Duration(resultSet.getInt("duration"))
                    .TitleRussian(resultSet.getString("russiantitle"))
                    .TitleOriginal(resultSet.getString("originaltitle"))
                    .VideoFileName(resultSet.getString("videofilename"))
                    .PosterFileName(resultSet.getString("posterfilename"))
                    .Year(resultSet.getInt("yearproduction"))
                    .Genres(resultSet.getString("genres").substring(1, resultSet.getString("genres").length()-1).split(", "))
                    .Countries(resultSet.getString("countries").substring(1, resultSet.getString("countries").length()-1).split(", "))
                    .WebMapping(resultSet.getString("webmapping"))
                    .Directors(resultSet.getString("directors").substring(2, resultSet.getString("directors").length()-2).split(", "))
                    .build();
            return me;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MovieEntity> findAndFilterEntitiesNotPaginated(Map<String, String> paramsMap) {
        try (Connection connection = driverManager.getConnection(URL, login, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    new SQLRequestBuilder()
                            .buildQueryForMovieTableUsingRequestParams(paramsMap))){
            List resultList = new ArrayList<>(100);
            while (resultSet.next()) {
                MovieEntity me = MovieEntity
                        .builder()
                        .Duration(resultSet.getInt("duration"))
                        .TitleRussian(resultSet.getString("russiantitle"))
                        .TitleOriginal(resultSet.getString("originaltitle"))
                        .VideoFileName(resultSet.getString("videofilename"))
                        .PosterFileName(resultSet.getString("posterfilename"))
                        .Year(resultSet.getInt("yearproduction"))
                        .Genres(resultSet.getString("genres").substring(1, resultSet.getString("genres").length()-1).split(", "))
                        .Countries(resultSet.getString("countries").substring(1, resultSet.getString("countries").length()-1).split(", "))
                        .WebMapping(resultSet.getString("webmapping"))
                        .Directors(resultSet.getString("directors").substring(2, resultSet.getString("directors").length()-2).split(", "))
                        .build();
                resultList.add(me);
            }
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
       }
    }

    public List<MovieEntity> findAndFilterEntitiesPaginated(Map<String, String> paramsMap, int entitiesPerPage) {
        try (Connection connection = driverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     new SQLRequestBuilder()
                             .buildQueryForCatalogPage(paramsMap,
                                     entitiesPerPage))){
            System.out.println("Trimmed request = " + new SQLRequestBuilder()
                    .buildQueryForCatalogPage(paramsMap,
                            entitiesPerPage));
            List resultList = new ArrayList<>(100);
            while (resultSet.next()) {
                MovieEntity me = MovieEntity
                        .builder()
                        .Duration(resultSet.getInt("duration"))
                        .TitleRussian(resultSet.getString("russiantitle"))
                        .TitleOriginal(resultSet.getString("originaltitle"))
                        .VideoFileName(resultSet.getString("videofilename"))
                        .PosterFileName(resultSet.getString("posterfilename"))
                        .Year(resultSet.getInt("yearproduction"))
                        .Genres(resultSet.getString("genres").substring(1, resultSet.getString("genres").length()-1).split(", "))
                        .Countries(resultSet.getString("countries").substring(1, resultSet.getString("countries").length()-1).split(", "))
                        .WebMapping(resultSet.getString("webmapping"))
                        .Directors(resultSet.getString("directors").substring(2, resultSet.getString("directors").length()-2).split(", "))
                        .build();
                resultList.add(me);
            }
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public long receiveNotPaginatedListQuantity(Map<String, String> paramsMap){
        try (Connection connection = driverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     new SQLRequestBuilder()
                             .CountQueryBuilder(paramsMap))){
            resultSet.next();
            long result = (long)resultSet.getObject(1);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<String> findAllUniqueValueFromRequiredColumn(String type) {
        try (Connection connection = driverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(new SQLRequestBuilder().buildQueryForDistinctParametrs(type))){
            Class.forName("org.postgresql.Driver");
            ArrayList resultList = new ArrayList();
            while (resultSet.next()) {
                resultList.add(resultSet.getString(1));
            }
            return resultList;
        } catch (SQLException e) {
            return Collections.emptyList();
        } catch (ClassNotFoundException e) {
            return Collections.emptyList();
        }
    }

    


}
