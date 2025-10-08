package ru.kukuruzvelt.application.domain;

import ru.kukuruzvelt.application.model.MovieEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MovieEntityDAOTextFile implements MovieEntityDAO {

    public static String sourceBase = "B:\\src\\database_substitute.txt";

    @Override
    public MovieEntity findByWebMapping(String title) {
        return null;
    }

    @Override
    public List<MovieEntity> recieveFilteredNotPaginatedList(Map<String, String> paramsMap) {
        try {
            List<MovieEntity> resultList = new ArrayList<>();
            Iterator<String> iterator = Files.readAllLines(Paths.get(sourceBase)).iterator();
            while(iterator.hasNext()) {
                String str = iterator.next();
                if (str.contentEquals("{")){
                    MovieEntity entity =
                            MovieEntity.builder()
                                    .WebMapping(takeInfo(iterator))
                                    .VideoFileName(takeInfo(iterator))
                                    .PosterFileName(takeInfo(iterator))
                                    .Year(Integer.parseInt(takeInfo(iterator)))
                                    .Countries((takeInfo(iterator)).split(", "))
                                    .Genres((takeInfo(iterator)).split(", "))
                                    .Duration(Integer.parseInt(takeInfo(iterator)))
                                    .TitleRussian(takeInfo(iterator))
                                    .TitleOriginal(takeInfo(iterator))
                                    //.Directors((takeInfo(iterator)).split(", "))
                                    .build();
                    if (entity.matchesRequirement(paramsMap)) {
                        resultList.add(entity);
                    }
                }
            }
            return resultList;
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public long getFilteredNotPaginatedListSize(Map<String, String> paramsMap) {
        return recieveFilteredNotPaginatedList(paramsMap).size();
    }

    @Override
    public List<String> findAllUniqueValueFromRequiredColumn(String type) {
        return List.of();
    }

    @Override
    public List recievePaginatedFilteredList(Map<String, String> paramsMap, int entitiesPerPage) {
        return List.of();
    }

    private String takeInfo(Iterator<String> iterator){
        return iterator.next().split(" = ")[1];
    }
}