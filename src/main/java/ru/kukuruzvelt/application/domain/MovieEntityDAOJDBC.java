package ru.kukuruzvelt.application.domain;

import org.springframework.stereotype.Component;
import ru.kukuruzvelt.application.model.MovieEntity;

import java.sql.*;
import java.util.*;
@Component
public class MovieEntityDAOJDBC implements MovieEntityDAO {

    private static String URL = "jdbc:postgresql://localhost:5432/";
    private static String login = "postgres";
    private static String password = null;
    private DriverManager driverManager ;

    public MovieEntity findByWebMapping(String webmapping) {
        String query="SELECT * FROM public.movies WHERE webmapping = '" + webmapping +"'";
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

    public List<MovieEntity> recieveFilteredNotPaginatedList(Map<String, String> paramsMap) {
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

    public List<MovieEntity> recievePaginatedFilteredList(Map<String, String> paramsMap, int entitiesPerPage) {
        try (Connection connection = driverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     new SQLRequestBuilder()
                             .buildQueryForCatalogPage(paramsMap,
                                     entitiesPerPage))){
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

    public long getFilteredNotPaginatedListSize(Map<String, String> paramsMap){
        try (Connection connection = driverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     new SQLRequestBuilder()
                             .countQuery(paramsMap))){
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

class SQLRequestBuilder {

    public String buildQueryForMovieTableUsingRequestParams(Map<String, String> paramsMap){
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM 'public.moves'");
        queryBuilder.append(sqlConditionalQueryComposer(paramsMap));
        queryBuilder.append(" ORDER BY russiantitle ASC;");
        return queryBuilder.toString();
    }

    public String buildQueryForDistinctParametrs(String type){
        switch (type){
            case ("countries") : return "SELECT DISTINCT unnest (countries) FROM public.movies ORDER BY unnest";
            case ("years") : return "SELECT DISTINCT yearproduction FROM public.movies ORDER BY unnest";
            case ("genres") :return "SELECT DISTINCT unnest (genres) FROM public.movies ORDER BY unnest";
            case ("decades") : return "SELECT DISTINCT yearproduction - yearproduction%(10) FROM public.movies";
            case ("directors") : return "SELECT DISTINCT unnest (directors) FROM public.movies ORDER BY unnest";
        }
        return null;
    }

    public String countQuery(Map<String, String> paramsMap) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT (*) FROM public.movies");
        queryBuilder.append(sqlConditionalQueryComposer(paramsMap));
        return queryBuilder.toString();
    }

    public String buildQueryForCatalogPage(Map<String, String> paramsMap, int entitiesPerPage){
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM public.movies");
        queryBuilder.append(sqlConditionalQueryComposer(paramsMap));
        int page;
        try {
            page = Integer.parseInt(paramsMap.getOrDefault("page", "1"));
        }
        catch (NumberFormatException e){
            page = 1;
        }
        if (page <= 0) page = 1;
        queryBuilder.append(" ORDER BY russiantitle ASC");
        queryBuilder.append(" OFFSET " + (page-1)*entitiesPerPage + " LIMIT " + entitiesPerPage);
        return queryBuilder.toString();

    }
    private boolean isTextQueriable(String test, String regex){
        if (test == null) return false;
        if (!test.contentEquals("null") && !test.contentEquals("all")
                && test.matches(regex)) return true;
        return false;
    }

    private String sqlConditionalQueryComposer(Map<String, String> paramsMap){
        List<String> querySubstrings = new ArrayList<>();
        String requiredRegex = "[а-яА-Я]+";
        String digitalRegex = "^\\d{1,14}$";
        if (paramsMap == null) paramsMap = new HashMap<>();
        if (isTextQueriable(paramsMap.get("genre"), requiredRegex)) querySubstrings.add("'" + paramsMap.get("genre") + "' = ANY(movies.genres)");
        if (isTextQueriable(paramsMap.get("country"), requiredRegex)) querySubstrings.add("'" + paramsMap.get("country") + "' = ANY(movies.countries)");
        if (isTextQueriable(paramsMap.get("search"), requiredRegex)) querySubstrings.add("russiantitle LIKE '%" + paramsMap.get("search") + "%'");
        if (isTextQueriable(paramsMap.get("decade"), digitalRegex)) {
            int decade = Integer.parseInt(paramsMap.get("decade"));
            querySubstrings.add("yearproduction <= " + String.valueOf(decade + 9) + " AND yearproduction >= " + decade);
        }
        StringBuilder queryBuilder = new StringBuilder();
        for (int i = 0; i < querySubstrings.size(); i++) {
            if (i == 0) {
                queryBuilder.append(" WHERE ");
            }
            if (i > 0 && i < querySubstrings.size()) {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append(querySubstrings.get(i));
        }
        return queryBuilder.toString();
    }
}
