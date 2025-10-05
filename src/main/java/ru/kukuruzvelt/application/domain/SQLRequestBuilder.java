package ru.kukuruzvelt.application.domain;

import java.util.*;

public class SQLRequestBuilder {

    public String buildQueryForMovieTableUsingRequestParams(Map<String, String> paramsMap){
        List<String> querySubstrings = new ArrayList<>();
        if (paramsMap == null) paramsMap = new HashMap<>();
        if(isTextQueriable(paramsMap.get("genre"), "[а-яА-Яa-zA-Z]+")) {
            querySubstrings.add("'" + paramsMap.get("genre") + "' = ANY(movies.genres)");
        }
        if(isTextQueriable(paramsMap.get("country"), "[а-яА-Яa-zA-Z]+")) {
            querySubstrings.add("'" + paramsMap.get("country") + "' = ANY(movies.countries)");
        }
        if (isTextQueriable(paramsMap.get("search"), "[а-яА-Яa-zA-Z]+"))  {
            querySubstrings.add("russiantitle LIKE '%" + paramsMap.get("search") + "%'");
        }
        if(isQueriable(paramsMap.get("decade"))) {
            int decade = Integer.parseInt(paramsMap.get("decade"));
            querySubstrings.add("yearproduction <= " + String.valueOf(decade + 9) + " AND yearproduction >= " + decade);
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM public.movies");
        for (int i = 0; i < querySubstrings.size(); i++){
            if (i == 0){
                queryBuilder.append(" WHERE ");
            }
            if(i > 0 && i < querySubstrings.size()){
                queryBuilder.append(" AND ");
            }
            queryBuilder.append(querySubstrings.get(i));
        }
        queryBuilder.append(" ORDER BY russiantitle ASC;");
        System.out.println("\nQUERY BUILDER RESULT:" + queryBuilder.toString());
        return queryBuilder.toString();
    }

    public String buildQueryForDistinctParametrs(String type){
        switch (type){
            case ("countries") : return "SELECT DISTINCT unnest (countries) FROM public.movies ";
            case ("years") : return "SELECT DISTINCT yearproduction FROM public.movies";
            case ("genres") :return "SELECT DISTINCT unnest (genres) FROM public.movies";
            case ("decades") : return "SELECT DISTINCT yearproduction - yearproduction%(10) FROM public.movies";
        }
        return null;
    }

    public String CountQueryBuilder(Map<String, String> paramsMap){
        List<String> querySubstrings = new ArrayList<>();
        if (paramsMap == null) paramsMap = new HashMap<>();
        if(isQueriable(paramsMap.get("genre")) && (paramsMap.get("genre").matches("[а-яА-Яa-zA-Z]+"))) {
            querySubstrings.add("'" + paramsMap.get("genre") + "' = ANY(movies.genres)");
        }
        if(isQueriable(paramsMap.get("country")) && (paramsMap.get("country").matches("[а-яА-Яa-zA-Z]+"))) {
            querySubstrings.add("'" + paramsMap.get("country") + "' = ANY(movies.countries)");
        }
        if (isQueriable(paramsMap.get("search")) && (paramsMap.get("search").matches("[а-яА-Яa-zA-Z]+"))) {
            querySubstrings.add("russiantitle LIKE '%" + paramsMap.get("search") + "%'");
        }
        if(isQueriable(paramsMap.get("decade"))) {
            int decade = Integer.parseInt(paramsMap.get("decade"));
            querySubstrings.add("yearproduction <= " + String.valueOf(decade + 9) + " AND yearproduction >= " + decade);
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT (*) FROM public.movies");
        for (int i = 0; i < querySubstrings.size(); i++){
            if (i == 0){
                queryBuilder.append(" WHERE ");
            }
            if(i > 0 && i < querySubstrings.size()){
                queryBuilder.append(" AND ");
            }
            queryBuilder.append(querySubstrings.get(i));
        }
        System.out.println("\nQUERY BUILDER RESULT:" + queryBuilder.toString());
        return queryBuilder.toString();

    }

    public String buildQueryForCatalogPage(Map<String, String> paramsMap, int entitiesPerPage){
        List<String> querySubstrings = new ArrayList<>();
        if (paramsMap == null) paramsMap = new HashMap<>();
        if(isTextQueriable(paramsMap.get("genre"), "[а-яА-Яa-zA-Z]+")) {
            querySubstrings.add("'" + paramsMap.get("genre") + "' = ANY(movies.genres)");
        }
        if(isTextQueriable(paramsMap.get("country"), "[а-яА-Яa-zA-Z]+")) {
            querySubstrings.add("'" + paramsMap.get("country") + "' = ANY(movies.countries)");
        }
        if (isTextQueriable(paramsMap.get("search"), "[а-яА-Яa-zA-Z]+"))  {
            querySubstrings.add("russiantitle LIKE '%" + paramsMap.get("search") + "%'");
        }
        if(isQueriable(paramsMap.get("decade"))) {
            int decade = Integer.parseInt(paramsMap.get("decade"));
            querySubstrings.add("yearproduction <= " + String.valueOf(decade + 9) + " AND yearproduction >= " + decade);
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM public.movies");
        for (int i = 0; i < querySubstrings.size(); i++){
            if (i == 0){
                queryBuilder.append(" WHERE ");
            }
            if(i > 0 && i < querySubstrings.size()){
                queryBuilder.append(" AND ");
            }
            queryBuilder.append(querySubstrings.get(i));
        }
        int page;
        try {
            page = Integer.parseInt(paramsMap.getOrDefault("page", "1"));
        }
        catch (NumberFormatException e){
            page = 1;
        }
        queryBuilder.append(" ORDER BY russiantitle ASC");
        queryBuilder.append(" OFFSET " + (page-1)*entitiesPerPage + " LIMIT " + entitiesPerPage);
        System.out.println("\nQUERY BUILDER RESULT:" + queryBuilder.toString());
        return queryBuilder.toString();

    }
    private boolean isQueriable(String test){
        if (test != null &&
                !test.contentEquals("null") &&
                !test.contentEquals("all")) return true;
        return false;
    }

    private boolean isTextQueriable(String test, String regex){
        if (test == null) return false;
        if (!test.contentEquals("null") && !test.contentEquals("all")
            && test.matches(regex)) return true;
        return false;
    }
}
