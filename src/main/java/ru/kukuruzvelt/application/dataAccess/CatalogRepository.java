package ru.kukuruzvelt.application.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kukuruzvelt.application.model.MovieEntity;

import java.util.Optional;

@Repository
public interface CatalogRepository extends CrudRepository<MovieEntity, String> {

    @Override
    Optional<MovieEntity> findById(String s);
}
