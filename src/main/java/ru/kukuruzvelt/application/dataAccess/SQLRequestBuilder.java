package ru.kukuruzvelt.application.dataAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLRequestBuilder {
    private final StringBuilder queryBuilder = new StringBuilder();

    public SQLRequestBuilder setBase(String str){
        this.queryBuilder.append(str);
        return this;
    }

    public SQLRequestBuilder setSelectDistinct(String requiredColumn){
        if (requiredColumn.contentEquals("countries"))
            this.queryBuilder.append("SELECT DISTINCT unnest (countries) AS N FROM public.movies ");
        if (requiredColumn.contentEquals("genres"))
            this.queryBuilder.append("SELECT DISTINCT unnest (genres) AS N FROM public.movies ");
        if (requiredColumn.contentEquals("decades"))
            this.queryBuilder.append("SELECT DISTINCT yearproduction - yearproduction%(10) AS N FROM public.movies ");
        if (requiredColumn.contentEquals("directors"))
            this.queryBuilder.append("SELECT DISTINCT unnest (directors) AS N  FROM public.movies ");
        return this;
    }

    public SQLRequestBuilder setConditions(Map<String, String> paramsMap){
        List<String> querySubstrings = new ArrayList<>();
        String requiredRegex = "[а-яА-Я]+";
        String digitalRegex = "^\\d{3,5}$";
        if (paramsMap == null) paramsMap = new HashMap<>();
        if (isAvailableForQuering(paramsMap.get("genre"), requiredRegex)) querySubstrings.add("'" + paramsMap.get("genre") + "' = ANY(movies.genres)");
        if (isAvailableForQuering(paramsMap.get("country"), requiredRegex)) querySubstrings.add("'" + paramsMap.get("country") + "' = ANY(movies.countries)");
        if (isAvailableForQuering(paramsMap.get("search"), "[а-яА-Я\s]+")) querySubstrings.add("(russiantitle ILIKE '%" + paramsMap.get("search") + "%' OR '"+paramsMap.get("search")+"' = ANY(movies.directors))");
        if (isAvailableForQuering(paramsMap.get("director"), "[а-яА-Я\s]+")) querySubstrings.add("'" + paramsMap.get("director") + "' = ANY(movies.directors)");
        if (isAvailableForQuering(paramsMap.get("decade"), digitalRegex)) {
            int decade = Integer.parseInt(paramsMap.get("decade"));
            querySubstrings.add("yearproduction <= " + (decade + 9) + " AND yearproduction >= " + decade);
        }
        for (int i = 0; i < querySubstrings.size(); i++) {
            if (i == 0) queryBuilder.append(" WHERE ");
            if (i > 0) queryBuilder.append(" AND ");
            queryBuilder.append(querySubstrings.get(i));
        }
        return this;
    }

    public SQLRequestBuilder setOrderBy(Map<String, String> paramsMap){
        String str = paramsMap.getOrDefault("sort", "russian");
        if (str.contentEquals("null")) this.queryBuilder.append( " ORDER BY russiantitle ASC ");
        if (str.contentEquals("duration")) this.queryBuilder.append( " ORDER BY duration DESC " );
        if (str.contentEquals("year")) this.queryBuilder.append( " ORDER BY yearproduction DESC ");
        if (str.contentEquals("russian")) this.queryBuilder.append( " ORDER BY russiantitle ASC ");
        if (str.contentEquals("original")) this.queryBuilder.append( " ORDER BY originaltitle ASC ");
        if (str.contentEquals("directors")) this.queryBuilder.append( " ORDER BY directors ASC ");
        return this;

    }

    public SQLRequestBuilder setOrderBy(String orderBy){
        this.queryBuilder.append(orderBy);
        return this;
    }

    public SQLRequestBuilder setOffsetAndLimit(Map<String, String> paramsMap){
        int pageNumber = getPageNumber(paramsMap);
        int elementsPerPage = getShowValue(paramsMap);
        if (pageNumber == -1) return this;
        queryBuilder
                .append(" OFFSET ")
                .append((pageNumber-1)*elementsPerPage)
                .append(" LIMIT ")
                .append(elementsPerPage);
        return this;
    }

    private int getShowValue(Map<String, String> paramsMap){
        try {
            return Integer.parseInt(paramsMap.getOrDefault("show", "9"));
        }
        catch (NumberFormatException ex) {
            return 9;
        }
    }

    private int getPageNumber(Map<String, String> paramsMap){
        try {
            int i = Integer.parseInt(paramsMap.getOrDefault("page", "1"));
            if (i < 0) i = 1;
            return i;
        }
        catch (NumberFormatException e){
            return 1;
        }
    }
    private boolean isAvailableForQuering(String test, String regex){
        if (test == null) return false;
        return !test.contentEquals("null") && !test.contentEquals("all")
                && test.matches(regex);
    }

    public String toString(){
        return this.queryBuilder.toString();
    }

}
