package ru.kukuruzvelt.application.domain;

import org.springframework.stereotype.Component;
import ru.kukuruzvelt.application.model.MovieEntity;
import java.sql.*;
import java.util.*;

@Component
public class CatalogDAOJDBC implements CatalogDAO {

    private static String URL = "jdbc:postgresql://localhost:5432/";
    private static String login = "postgres";
    private static String password = System.getenv("db_password");
    private DriverManager driverManager ;

    @Override public MovieEntity findByWebMapping(String webmapping) {
        String query="SELECT * FROM public.movies WHERE webmapping = '" + webmapping +"'";
        try (Connection connection = driverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)){
            resultSet.next();
            return createEntityFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return MovieEntity.nullEntity();
        }
    }

    @Override public MovieEntity findRandomMovie(){
        String query="SELECT * FROM public.movies ORDER BY random() LIMIT 1";
        try (Connection connection = driverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)){
            resultSet.next();
            return createEntityFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override public List<MovieEntity> findAllByRequiredParametersPaginated(Map<String, String> paramsMap, int entitiesPerPage) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM public.movies");
        queryBuilder.append(translateToConditionalQuerySubstring(paramsMap));
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
        try (Connection connection = driverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(queryBuilder.toString())){
            List resultList = new ArrayList<>(100);
            while (resultSet.next()) {
                resultList.add(createEntityFromResultSet(resultSet));
            }
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override public long getFilteredNotPaginatedListSize(Map<String, String> paramsMap){
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT (*) FROM public.movies");
        queryBuilder.append(translateToConditionalQuerySubstring(paramsMap));
        try (Connection connection = driverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(queryBuilder.toString())){
            resultSet.next();
            long result = (long)resultSet.getObject(1);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override public List<String> findAllUniqueValueFromRequiredColumnAndFilter(String type, Map<String, String> paramsMap) {
        String query;
        if (type.contentEquals("countries")) query = "SELECT DISTINCT unnest (countries) FROM public.movies ";
        else if (type.contentEquals("years")) query = "SELECT DISTINCT yearproduction FROM public.movies ";
        else if (type.contentEquals("genres")) query = "SELECT DISTINCT unnest (genres) FROM public.movies ";
        else if (type.contentEquals("decades")) query = "SELECT DISTINCT yearproduction - yearproduction%(10) AS N FROM public.movies ";
        else if (type.contentEquals("directors")) query = "SELECT DISTINCT unnest (directors) FROM public.movies ";
        else query = " ";
        StringBuilder builder = new StringBuilder();
        builder.append(query);
        builder.append(translateToConditionalQuerySubstring(paramsMap));
        if(type.contentEquals("decades")){
            builder.append(" ORDER BY N ");
        }
        else{
            builder.append(" ORDER BY unnest");
        }
        System.out.println(builder.toString());
        try (Connection connection = driverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             //ResultSet resultSet = statement.executeQuery(new SQLRequestBuilder().buildQueryForDistinctParametrs(type))
             ResultSet resultSet = statement.executeQuery(builder.toString())){
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

    private MovieEntity createEntityFromResultSet(ResultSet resultSet){
        try{
            MovieEntity me = MovieEntity
                .builder()
                .Duration(resultSet.getInt("duration"))
                .TitleRussian(resultSet.getString("russiantitle"))
                .TitleOriginal(resultSet.getString("originaltitle"))
                .VideoFileName(resultSet.getString("videofilename"))
                .PosterFileName(resultSet.getString("posterfilename"))
                .Year(resultSet.getInt("yearproduction"))
                .Genres(resultSet.getString("genres").substring(1, resultSet.getString("genres").length()-1).split(","))
                .Countries(resultSet.getString("countries").substring(1, resultSet.getString("countries").length()-1).split(","))
                .WebMapping(resultSet.getString("webmapping"))
                .Directors(resultSet.getString("directors").substring(2, resultSet.getString("directors").length()-2).split("\",\""))
                .build();
            return me;
        }
        catch (SQLException e) {
            return MovieEntity.nullEntity();
        }
    }

    private String translateToConditionalQuerySubstring(Map<String, String> paramsMap){
        List<String> querySubstrings = new ArrayList<>();
        String requiredRegex = "[а-яА-Я]+";
        String digitalRegex = "^\\d{3,5}$";
        if (paramsMap == null) paramsMap = new HashMap<>();
        if (isTextQueriable(paramsMap.get("genre"), requiredRegex)) querySubstrings.add("'" + paramsMap.get("genre") + "' = ANY(movies.genres)");
        if (isTextQueriable(paramsMap.get("country"), requiredRegex)) querySubstrings.add("'" + paramsMap.get("country") + "' = ANY(movies.countries)");
        if (isTextQueriable(paramsMap.get("search"), requiredRegex)) querySubstrings.add("russiantitle ILIKE '%" + paramsMap.get("search") + "%'");
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
    private boolean isTextQueriable(String test, String regex){
        if (test == null) return false;
        if (!test.contentEquals("null") && !test.contentEquals("all")
                && test.matches(regex)) return true;
        return false;
    }

}

