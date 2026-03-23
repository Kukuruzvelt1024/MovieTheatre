package ru.kukuruzvelt.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kukuruzvelt.application.dataAccess.CatalogDataAccess;
import ru.kukuruzvelt.application.dataAccess.CatalogRepository;
import ru.kukuruzvelt.application.model.MovieEntity;

import java.util.List;
import java.util.Map;
@Service
public class CatalogService {

    @Autowired
    private CatalogDataAccess DataAccessObject;
    @Autowired
    private CatalogRepository repository;


    public MovieEntity findMovieByWebmapping(String webmapping){
        return repository.findById(webmapping).orElse(MovieEntity.nullEntity());
        //return DataAccessObject.findByWebMapping(webmapping);
    }

    public List<MovieEntity> findMoviesByRequiredParameters(Map<String, String> parametersRequired){
        return DataAccessObject.findAllByRequiredParametersPaginated(parametersRequired);
    }

    public MovieEntity findRandomMovie(Map<String, String> requiredParams){
        return DataAccessObject.findRandomMovie();
    }

    public long findTotalRelevantRowsAmount(Map<String, String> requiredParams){
        return DataAccessObject.getFilteredNotPaginatedListSize(requiredParams);
    }

    public List<String> findAllDistinctValues(Map<String, String> parametersRequired, String column){
        return DataAccessObject.findAllUniqueValuesFromColumn(column, parametersRequired);
    }


}
