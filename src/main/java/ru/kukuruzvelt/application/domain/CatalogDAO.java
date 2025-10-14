package ru.kukuruzvelt.application.domain;

import ru.kukuruzvelt.application.model.MovieEntity;

import java.util.List;
import java.util.Map;

public interface CatalogDAO {

    public MovieEntity findByWebMapping(String title);

    public MovieEntity findRandomMovie();

    public List<MovieEntity> findAllByRequiredParameters(Map<String, String> paramsMap);

    public List<MovieEntity> findAllByRequiredParametersPaginated(Map<String, String> paramsMap, int entitiesPerPage);

    public long getFilteredNotPaginatedListSize(Map<String, String> paramsMap);

    public List<String> findAllUniqueValueFromRequiredColumn(String type);

    public List<String> findAllUniqueValueFromRequiredColumnAndFilter(String type, Map<String, String> paramsMap);


}
