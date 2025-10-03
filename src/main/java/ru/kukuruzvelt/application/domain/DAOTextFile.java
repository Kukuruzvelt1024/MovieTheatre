package ru.kukuruzvelt.application.domain;

import ru.kukuruzvelt.application.model.MovieEntity;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DAOTextFile implements DAO {

    public static String sourceBase = "B:\\src\\database_substitute.txt";
    private List<MovieEntity> localDatabaseCopy = new ArrayList<>();
    private static List<MovieEntity> localDatabaseCopyCache = new ArrayList<>(1024);
    private static Set<String> setOfUniqueGenres = new HashSet();
    private static Set<Integer> setOfUniqueYears = new HashSet();
    private static Set<String> setOfUniqueCountries = new HashSet();
    private static Set<Integer> setOfUniqueDecades = new HashSet();

    public List<MovieEntity> findAllEntitiesFilteredByHTTPRequestParams(Map<String, String> paramsMap){
        try {
            localDatabaseCopyCache.clear();
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
                                    .build();
                    setOfUniqueGenres.addAll(Arrays.asList(entity.getGenres()));
                    setOfUniqueCountries.addAll(Arrays.asList(entity.getCountries()));
                    setOfUniqueYears.add(entity.getYear());
                    setOfUniqueDecades.add(castYearToDecade(entity.getYear()));
                    if (entity.matchesRequirement(paramsMap)) {
                        this.localDatabaseCopy.add(entity);
                        localDatabaseCopyCache.add(entity);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /*System.out.println("Полная загрузка данных из внешнего источника");
        System.out.println("Размер кэша: " + localDatabaseCopyCache.size());
        System.out.println("Размер ДАО-объекта: " + this.localDatabaseCopy.size());
        System.out.println("Количество жанров: " + this.setOfUniqueGenres.size());
        System.out.println("Количество годов: " + this.setOfUniqueYears.size());*/
        return this.localDatabaseCopy;
    }

    public List findAllDistinctParameterOfEntitiesByType(String type){
        switch (type) {
            case ("countries") : return this.getAllCountries();
            case ("years") : return this.getAllYears();
            case ("genres") : return this.getAllGenres();
            case ("decades") : return this.getAllDecades();
        }
        return null;
    }

    public MovieEntity findByWebMapping(String webMapping){
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

    public DAOTextFile(){
        this.prepareData();
    }

    public DAOTextFile prepareData(){
        this.loadDataFromExternalSource();
        return this;
    }

    private void loadDataFromExternalSource(){
        try {
            localDatabaseCopyCache.clear();
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
                            .build();
                    setOfUniqueGenres.addAll(Arrays.asList(entity.getGenres()));
                    setOfUniqueCountries.addAll(Arrays.asList(entity.getCountries()));
                    setOfUniqueYears.add(entity.getYear());
                    setOfUniqueDecades.add(castYearToDecade(entity.getYear()));
                    localDatabaseCopy.add(entity);

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
        return this.localDatabaseCopy;
    }

    public List<String> getAllGenres(){
        if (setOfUniqueGenres.size() == 0){
            this.prepareData();
        }
        ArrayList<String> listOfUniqueGenres = new ArrayList<>(setOfUniqueGenres);
        Collections.sort(listOfUniqueGenres);
        return listOfUniqueGenres;
    }

    public List<Integer> getAllYears(){
        if(setOfUniqueYears.size() == 0){
            this.prepareData();
        }
        ArrayList<Integer> listOfYears = new ArrayList<>(setOfUniqueYears);
        Collections.sort(listOfYears);
        return listOfYears;
    }

    public List<String> getAllCountries(){
        if(setOfUniqueCountries.size() == 0){
            this.prepareData();
        }
        ArrayList<String> listOfCountries = new ArrayList<>(setOfUniqueCountries);
        Collections.sort(listOfCountries);
        return listOfCountries;
    }

    public List<Integer> getAllDecades(){
        if(setOfUniqueDecades.size() == 0){
            this.prepareData();
        }
        ArrayList<Integer> listOfDecades= new ArrayList<>(setOfUniqueDecades);
        Collections.sort(listOfDecades);
        return listOfDecades;
    }



    public DAOTextFile filterByYearGenreCountrySearchRequest(String yearRequired,
                                                             String genreRequired,
                                                             String countryRequired,
                                                             String searchRequest){
        this.localDatabaseCopy = this.localDatabaseCopy.stream()
                .filter((yearRequired.contentEquals("null") ||
                        yearRequired.contentEquals("all")) ?
                        me->true : me -> me.getYear().equals(Integer.parseInt(yearRequired)))
                .filter(genreRequired.contentEquals("null") ||
                        genreRequired.contentEquals("all") ?
                        me -> true:
                        me -> me.containsGenre(genreRequired))
                .filter(countryRequired.contentEquals("null") ||
                        countryRequired.contentEquals("all") ?
                        me -> true:
                        me -> me.containsCountry(countryRequired))
                .filter(searchRequest.contentEquals("null")?
                        me -> true:
                        me -> me.getTitleRussian().contains(searchRequest))
                .collect(Collectors.toList());
                return this;
    }



    public DAOTextFile reduceToLimitOfEntitiesPerPage(String page, int entitiesPerPage) {

        if ((page == null) || (page.contentEquals("null"))) {
            int pageNum = 1;
            this.localDatabaseCopy = subListed(this.localDatabaseCopy, pageNum, entitiesPerPage);
            return this;
        }
        if(page.contentEquals("all")){
            return this;
        }
        try {
            int pageNum = Integer.parseInt(page);
                    this.localDatabaseCopy = subListed(this.localDatabaseCopy, pageNum, entitiesPerPage);
            } catch (NumberFormatException exception) {
                    System.out.println("NumFormatException catched");
                    return this;
        }
        return this;
    }

    private List subListed(List rawList, int page, int entitiesPerPage){
        int lastIndexOfRawList = rawList.size() - 1;
        int beginIndex = (page - 1) * entitiesPerPage;
        if (beginIndex > lastIndexOfRawList) return rawList;
        int endIndex = page*entitiesPerPage - 1;
        if (endIndex > lastIndexOfRawList) endIndex = lastIndexOfRawList;
        return rawList.subList(beginIndex, endIndex+1);
    }


    public int sizeOfResultSet(){
        return this.localDatabaseCopy.size();
    }


    private int castYearToDecade(int year){
        return year - year % 10;
    }

    private String takeInfo(Iterator<String> iterator){
        return iterator.next().split(" = ")[1];
    }

    public void serializeToCsv(){
        try(FileWriter writer = new FileWriter("A:\\append.txt", false))
        {

            Iterator<MovieEntity> iterator=this.localDatabaseCopy.iterator();
            while(iterator.hasNext()) {
                MovieEntity me = iterator.next();
                StringBuilder strbuilder = new StringBuilder();
                StringBuilder genresBuilder = new StringBuilder();
                String genres[] = me.getGenres();
                genresBuilder.append("{");
                for(int i = 0; i< genres.length;i++){
                    genresBuilder.append(genres[i]);
                    if(i < genres.length-1) genresBuilder.append(", ");
                }
                genresBuilder.append("}");
                StringBuilder countriesBuilder = new StringBuilder();
                String countries[] = me.getCountries();
                countriesBuilder.append("{");
                for(int i = 0; i < countries.length;i++){

                    countriesBuilder.append(countries[i]);
                    if(i < countries.length-1) countriesBuilder.append(", ");
                }
                countriesBuilder.append("}");


                strbuilder.append(me.getDuration()).append(";")
                        .append(me.getTitleRussian()).append(";")
                        .append(me.getTitleOriginal()).append(";")
                        .append(me.getVideoFileName()).append(";")
                        .append(me.getPosterFileName()).append(";")
                        .append(me.getYear()).append(";")
                        .append(genresBuilder.toString()).append(";")
                        .append(countriesBuilder.toString()).append(";")
                        .append(me.getWebMapping());

                writer.write(strbuilder.toString());
                writer.append("\n");
            }

            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

}
