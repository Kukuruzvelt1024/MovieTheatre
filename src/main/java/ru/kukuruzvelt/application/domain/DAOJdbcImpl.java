package ru.kukuruzvelt.application.domain;

import ru.kukuruzvelt.application.model.MovieEntity;

import java.sql.*;
import java.util.*;


public class DAOJdbcImpl implements DAO {

    private static String URL = "jdbc:postgresql://localhost:5432/";
    private static String login = "postgres";
    private static String password = "postgres";
    private PreparedStatement prStatement;
    private DriverManager driverManager ;
    private List<MovieEntity> movieEntityList;

    public List<MovieEntity> findAllEntitiesFilteredByHTTPRequestParams(Map<String, String> paramsMap) {
        try (Connection connection = driverManager.getConnection(URL, login, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    new SQLRequestBuilder()
                            .buildQueryForMovieTableUsingRequestParams(paramsMap))){
            Class.forName("org.postgresql.Driver");
            this.movieEntityList = new ArrayList<>(100);
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
                        .build();
                this.movieEntityList.add(me);
            }
            return this.movieEntityList;
        } catch (SQLException e) {
            return Collections.emptyList();
        } catch (ClassNotFoundException e) {
            return Collections.emptyList();
        }
    }

    public List<String> findAllDistinctParameterOfEntitiesByType(String type) {
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

    public MovieEntity findByWebMapping(String webmapping) {
        String query="SELECT * FROM public.movies WHERE webmapping = '" + webmapping +"'";
        System.out.println(query);
        try (Connection connection = driverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)){
            Class.forName("org.postgresql.Driver");
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
                    .build();
            return me;
        } catch (SQLException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public DAOJdbcImpl reduceToLimitOfEntitiesPerPage(String page, int entitiesPerPage) {

        if ((page == null) || (page.contentEquals("null"))) {
            int pageNum = 1;
            this.movieEntityList = subListed(this.movieEntityList , pageNum, entitiesPerPage);
            return this;
        }
        if(page.contentEquals("all")){
            return this;
        }
        try {
            int pageNum = Integer.parseInt(page);
            this.movieEntityList = subListed(this.movieEntityList, pageNum, entitiesPerPage);
        } catch (NumberFormatException exception) {
            System.out.println("NumFormatException catched");
            return this;
        }
        return this;
    }

    private List subListed(List rawList, int page, int entitiesPerPage){
        int lastIndexOfRawList = rawList.size() - 1;
        int beginIndex = (page - 1) * entitiesPerPage;
        if (beginIndex > lastIndexOfRawList) return rawList;
        int endIndex = page*entitiesPerPage - 1;
        if (endIndex > lastIndexOfRawList) endIndex = lastIndexOfRawList;
        return rawList.subList(beginIndex, endIndex+1);
    }

    public List<MovieEntity> getListOfEntities(){
        return this.movieEntityList;
    }


}
