package ru.kukuruzvelt.application.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kukuruzvelt.application.model.DbMovieEntity;

@Repository
public interface MovieRepository extends JpaRepository<DbMovieEntity, Integer> {
}
