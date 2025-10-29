package ru.kukuruzvelt.application.dataAccess;

import org.springframework.stereotype.Component;
import ru.kukuruzvelt.application.model.MovieEntity;
import java.sql.*;
import java.util.*;

@Component
public class CatalogDataAccessJDBC implements CatalogDataAccess {

    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private static final String login = "postgres";
    private static final String password = System.getenv("db_password");
    private static final String findStatement =
            """
            SELECT * FROM public.movies WHERE webmapping = ?
            """;
    private static final String findRandom =
                    """
                    SELECT * FROM public.movies ORDER BY random() LIMIT 1
                    """;

    @Override public MovieEntity findByWebMapping(String webmapping) {
        try (Connection connection = DriverManager.getConnection(URL, login, password)){
            PreparedStatement prepStatement = connection.prepareStatement(findStatement);
            prepStatement.setString(1, webmapping);
            ResultSet resultSet = prepStatement.executeQuery();
            resultSet.next();
            return createEntityFromResultSet(resultSet);
        } catch (SQLException e) {
            return MovieEntity.nullEntity();
        }
    }

    @Override public MovieEntity findRandomMovie(){
        try (Connection connection = DriverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(findRandom)){
            resultSet.next();
            return createEntityFromResultSet(resultSet);
        } catch (SQLException e) {
            return MovieEntity.nullEntity();
        }
    }

    @Override public List<MovieEntity> findAllByRequiredParametersPaginated(Map<String, String> paramsMap) {
        SQLRequestBuilder request =
                new SQLRequestBuilder()
                        .setBase("SELECT * FROM public.movies ")
                        .setConditions(paramsMap)
                        .setOrderBy(paramsMap)
                        .setOffsetAndLimit(paramsMap);
        try (Connection connection = DriverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(request.toString())){
            List<MovieEntity> resultList = new ArrayList<>(100);
            while (resultSet.next()) {
                resultList.add(createEntityFromResultSet(resultSet));
            }
            return resultList;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    @Override public long getFilteredNotPaginatedListSize(Map<String, String> paramsMap){
        SQLRequestBuilder request = new SQLRequestBuilder()
                .setBase("SELECT COUNT (*) FROM public.movies")
                .setConditions(paramsMap);
        try (Connection connection = DriverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(request.toString())){
            resultSet.next();
            return (long)resultSet.getObject(1);
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override public List<String> findAllUniqueValuesFromColumn(String column, Map<String, String> requestParams) {
        SQLRequestBuilder request = new SQLRequestBuilder();
        request.setSelectDistinct(column)
                .setConditions(requestParams)
                .setOrderBy(" ORDER BY N ");
        try (Connection connection = DriverManager.getConnection(URL, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(request.toString())){
            ArrayList<String> resultList = new ArrayList<>();
            while (resultSet.next()) {
                resultList.add(resultSet.getString(1));
            }
            return resultList;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    private MovieEntity createEntityFromResultSet(ResultSet resultSet){
        try{
            return MovieEntity
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
        }
        catch (SQLException e) {
            return MovieEntity.nullEntity();
        }
    }

}

