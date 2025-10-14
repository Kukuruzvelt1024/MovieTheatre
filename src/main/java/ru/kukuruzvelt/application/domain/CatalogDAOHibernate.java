package ru.kukuruzvelt.application.domain;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kukuruzvelt.application.model.MovieEntity;

import java.util.List;
import java.util.Map;


public class CatalogDAOHibernate implements CatalogDAO {
    @Autowired
    Configuration configuration;

    public MovieEntity findByWebMapping(String title){
        //Configuration configuration = new Configuration();
        //configuration.configure("B:\\IdeaProjects\\application\\src\\main\\resources\\application.properties");
        configuration.addAnnotatedClass(MovieEntity.class);
        try(SessionFactory sessionFactory = configuration.buildSessionFactory()){
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            MovieEntity result = session.createQuery("FROM MovieEntity where webmapping = " + title, MovieEntity.class).uniqueResult();
            System.out.println("HIBERNATE = " + result);
            return result;

        }


    }

    @Override
    public MovieEntity findRandomMovie() {
        return null;
    }

    @Override
    public List<MovieEntity> findAllByRequiredParameters(Map<String, String> paramsMap) {
        return List.of();
    }

    @Override
    public List<MovieEntity> findAllByRequiredParametersPaginated(Map<String, String> paramsMap, int entitiesPerPage) {
        return List.of();
    }

    @Override
    public long getFilteredNotPaginatedListSize(Map<String, String> paramsMap) {
        return 0;
    }

    @Override
    public List<String> findAllUniqueValueFromRequiredColumn(String type) {
        return List.of();
    }

    @Override
    public List<String> findAllUniqueValueFromRequiredColumnAndFilter(String type, Map<String, String> paramsMap) {
        return List.of();
    }
}
