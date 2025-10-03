package ru.kukuruzvelt.application.domain;

import ru.kukuruzvelt.application.model.MovieEntity;

import java.util.List;
import java.util.Map;

public interface DAO {

    public MovieEntity findByWebMapping(String title);

    public List<MovieEntity> findAllEntitiesFilteredByHTTPRequestParams(Map<String, String> paramsMap);

    public List<String> findAllDistinctParameterOfEntitiesByType(String type);

}
