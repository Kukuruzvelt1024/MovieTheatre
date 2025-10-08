package ru.kukuruzvelt.application.domain;

import ru.kukuruzvelt.application.model.MovieEntity;

import java.util.List;
import java.util.Map;

public interface MovieEntityDAO {

    public MovieEntity findByWebMapping(String title);

    public List<MovieEntity> recieveFilteredNotPaginatedList(Map<String, String> paramsMap);

    public List<MovieEntity> recievePaginatedFilteredList(Map<String, String> paramsMap, int entitiesPerPage);

    public long getFilteredNotPaginatedListSize(Map<String, String> paramsMap);

    public List<String> findAllUniqueValueFromRequiredColumn(String type);


}
