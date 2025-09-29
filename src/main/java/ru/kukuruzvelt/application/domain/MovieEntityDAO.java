package ru.kukuruzvelt.application.domain;

import ru.kukuruzvelt.application.model.MovieEntity;

public interface MovieEntityDAO {

    public MovieEntity findByWebMapping(String title);

}
