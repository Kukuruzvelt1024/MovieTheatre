package ru.kukuruzvelt.application.dataAccess;

import ru.kukuruzvelt.application.model.MovieEntity;

import java.util.List;
import java.util.Map;

public interface CatalogDataAccess {

    MovieEntity findByWebMapping(String title);

    MovieEntity findRandomMovie();

    List<MovieEntity> findAllByRequiredParametersPaginated(Map<String, String> paramsMap);

    long getFilteredNotPaginatedListSize(Map<String, String> paramsMap);

    List<String> findAllUniqueValuesFromColumn(String type, Map<String, String> paramsMap);


}
