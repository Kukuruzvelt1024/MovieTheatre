package ru.kukuruzvelt.application.domain;

import ru.kukuruzvelt.application.model.MovieEntity;

import java.util.List;
import java.util.Map;

public interface DAO {

    public MovieEntity findByWebMapping(String title);

    public List<MovieEntity> findAndFilterEntitiesNotPaginated(Map<String, String> paramsMap);

    public long receiveNotPaginatedListQuantity(Map<String, String> paramsMap);

    public List<String> findAllUniqueValueFromRequiredColumn(String type);


    List findAndFilterEntitiesPaginated(Map<String, String> paramsMap, int entitiesPerPage);
}
