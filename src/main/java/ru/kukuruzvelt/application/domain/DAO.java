package ru.kukuruzvelt.application.domain;

import ru.kukuruzvelt.application.model.MovieEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DAO {

    private String source;
    private List<MovieEntity> localDatabaseCopy = new ArrayList<>();
    private static List<MovieEntity> localDatabaseCopyCache = new ArrayList<>(1024);
    private Set<String> setOfUniqueGenres = new HashSet();
    private Set<Integer> setOfUniqueYears = new HashSet();
    private Set<String> setOfUniqueCountries = new HashSet();

    public static DAO getInstance(String source){
        return new DAO(source);
    }

    public DAO prepareData(){
        this.loadDataFromExternalSource();
        return this;
    }

    private void loadDataFromExternalSource(){
        try {
            localDatabaseCopyCache.clear();
            Iterator<String> iterator = Files.readAllLines(Paths.get(source)).iterator();
            while(iterator.hasNext()) {
                String str = iterator.next();
                if (str.contentEquals("{")){
                    MovieEntity entity =
                    MovieEntity.builder()
                            .WebMapping(takeInfo(iterator))
                            .FilePath(takeInfo(iterator))
                            .PosterFileName(takeInfo(iterator))
                            .Year(Integer.parseInt(takeInfo(iterator)))
                            .Countries((takeInfo(iterator)).split(", "))
                            .Genres((takeInfo(iterator)).split(", "))
                            .Duration(Integer.parseInt(takeInfo(iterator)))
                            .TitleRussian(takeInfo(iterator))
                            .TitleOriginal(takeInfo(iterator))
                            .build();
                    this.setOfUniqueGenres.addAll(Arrays.asList(entity.getGenres()));
                    this.setOfUniqueCountries.addAll(Arrays.asList(entity.getCountries()));
                    this.setOfUniqueYears.add(entity.getYear());
                    this.localDatabaseCopy.add(entity);
                    localDatabaseCopyCache.add(entity);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Полная загрузка данных из внешнего источника");
        System.out.println("Размер кэша: " + localDatabaseCopyCache.size());
        System.out.println("Размер ДАО-объекта: " + this.localDatabaseCopy.size());
        System.out.println("Количество жанров: " + this.setOfUniqueGenres.size());
        System.out.println("Количество годов: " + this.setOfUniqueYears.size());
    }

    public List<MovieEntity> getListOfEntities(){
       this.debugGenres();
       this.debugCountries();
        return this.localDatabaseCopy;
    }

    public List<String> getAllGenres(){
        ArrayList<String> listOfUniqueGenres = new ArrayList<>(this.setOfUniqueGenres);
        Collections.sort(listOfUniqueGenres);
        return listOfUniqueGenres;
    }

    public List<Integer> getAllYears(){
        ArrayList<Integer> listOfYears = new ArrayList<>(this.setOfUniqueYears);
        Collections.sort(listOfYears);
        return listOfYears;
    }

    public List<String> getAllCountries(){
        ArrayList<String> listOfCountries = new ArrayList<>(this.setOfUniqueCountries);
        Collections.sort(listOfCountries);
        return listOfCountries;
    }

    public List getAllAvailableParameters(String type){
        switch (type) {
            case ("countries") : return this.getAllCountries();
            case ("years") : return this.getAllYears();
            case ("genres") : return this.getAllGenres();
        }
        return null;
    }

    public DAO filterByYear(int yearRequired){
        if(yearRequired >= 1890)
        this.localDatabaseCopy = this.localDatabaseCopy.
                stream().
                filter(e -> e.getYear() == yearRequired).
                collect(Collectors.toList());
        return this;
    }

    public DAO filterByYear(String yearRequired){
        if(!yearRequired.contentEquals("null") && (!yearRequired.contentEquals("all"))) {

            Integer yearRequiredInteger = Integer.parseInt(yearRequired);
            System.out.println(yearRequiredInteger);
            this.localDatabaseCopy = this.localDatabaseCopy.
                    stream().
                    filter(e -> e.getYear().equals(yearRequiredInteger)).
                    collect(Collectors.toList());
        }
        return this;
    }

    public DAO filterByGenre(String genreRequired){
        if ((!genreRequired.contentEquals("all")) && (!genreRequired.contentEquals("null"))) {
            this.localDatabaseCopy = this.localDatabaseCopy.
                    stream().
                    filter(e -> e.containsGenre(genreRequired)).collect(Collectors.toList());
        }
        return this;
    }

    public DAO filterByCountry(String countryRequired){
        if ((!countryRequired.contentEquals("all")) && (!countryRequired.contentEquals("null"))) {
            this.localDatabaseCopy = this.localDatabaseCopy.
                    stream().
                    filter(e -> e.containsCountry(countryRequired)).collect(Collectors.toList());
        }
        return this;
    }

    public DAO filterBySearchRequest(String searchRequest){
        if  (!searchRequest.contentEquals( "null")){
            this.localDatabaseCopy = this.localDatabaseCopy.
                    stream().
                    filter(e -> e.getTitleRussian().contains(searchRequest)).collect(Collectors.toList());
        }
        return this;
    }

    public MovieEntity findByOriginalTitle(String title){
        Iterator<MovieEntity> cachedListIterator = localDatabaseCopyCache.iterator();
        while (cachedListIterator.hasNext()){
            MovieEntity me = cachedListIterator.next();
            if (me.getWebMapping().equalsIgnoreCase(title)){
                return me;
            }
        }
        this.loadDataFromExternalSource();
        Iterator<MovieEntity> it = this.localDatabaseCopy.iterator();
        while (it.hasNext()){
            MovieEntity me = it.next();
            if (me.getTitleOriginal().equalsIgnoreCase(title)){
                return me;
            }
        }
        return null;
    }

    public MovieEntity findByRussianTitle(String title){
        Iterator<MovieEntity> cachedListIterator = localDatabaseCopyCache.iterator();
        while (cachedListIterator.hasNext()){
            MovieEntity me = cachedListIterator.next();
            if (me.getWebMapping().equalsIgnoreCase(title)){
                return me;
            }
        }
        this.loadDataFromExternalSource();
        Iterator<MovieEntity> it = this.localDatabaseCopy.iterator();
        while (it.hasNext()){
            MovieEntity me = it.next();
            if (me.getTitleRussian().equalsIgnoreCase(title)){
                return me;
            }
        }
        return null;
    }

    public MovieEntity findByWebMapping(String webMapping) throws NullPointerException{
        Optional<MovieEntity> optionalStaticSearchResult =
                localDatabaseCopyCache.
                        stream().
                        filter(me-> me.getWebMapping().equalsIgnoreCase(webMapping)).
                        findFirst();
        if(optionalStaticSearchResult.isPresent()) return optionalStaticSearchResult.get();
        this.loadDataFromExternalSource();
        Optional<MovieEntity> optionalSearchResult =
                this.localDatabaseCopy.
                        stream().
                        filter(me-> me.getWebMapping().equalsIgnoreCase(webMapping)).
                        findFirst();
        if(optionalStaticSearchResult.isPresent()) return optionalStaticSearchResult.get();
        return null;
    }

    public MovieEntity findByFilePath(String filePath){
        Iterator<MovieEntity> cachedListIterator = localDatabaseCopyCache.iterator();
        while (cachedListIterator.hasNext()){
            MovieEntity me = cachedListIterator.next();
            if (me.getWebMapping().equalsIgnoreCase(filePath)){
                return me;
            }
        }
        this.loadDataFromExternalSource();
        Iterator<MovieEntity> it = this.localDatabaseCopy.iterator();
        while (it.hasNext()){
            MovieEntity me = it.next();
            if (me.getFilePath().equalsIgnoreCase(filePath)){
                return me;
            }
        }
        return null;
    }

    public DAO sortBy(String sortType) {

        if (sortType.contentEquals("eng"))
            this.localDatabaseCopy.sort(Comparator.comparing(MovieEntity::getTitleOriginal));
        if ((sortType.contentEquals("ru")) && (sortType.contentEquals("null")));
            this.localDatabaseCopy.sort(Comparator.comparing(MovieEntity::getTitleRussian));
        if (sortType.contentEquals("year"))
            this.localDatabaseCopy.sort(Comparator.comparing(MovieEntity::getYear));
        if (sortType.contentEquals("duration"))
            this.localDatabaseCopy.sort(Comparator.comparing(MovieEntity::getDuration));
        return this;
    }

    private void debugGenres(){
        Iterator<String> iterator= setOfUniqueGenres.iterator();
        while (iterator.hasNext()){
            System.out.print(iterator.next()+ "; ");
        }
        System.out.println();
    }

    private void debugCountries(){
        System.out.printf("Доступные страны: ");
        Iterator<String> iterator= setOfUniqueCountries.iterator();
        while (iterator.hasNext()){
            System.out.print(iterator.next()+ "; ");
        }
        System.out.println();
    }

    private DAO(String source){
        this.source = source;
    }

    private String takeInfo(Iterator<String> iterator){
        return iterator.next().split(" = ")[1];
    }



    class UpdateScheduler extends TimerTask{


        @Override
        public void run() {

        }
    }

}
