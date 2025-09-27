package ru.kukuruzvelt.application.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kukuruzvelt.application.Application;
import ru.kukuruzvelt.application.domain.DAO;
import ru.kukuruzvelt.application.model.MovieEntity;

import java.util.List;


@Controller
public class CatalogController {

    private static int entitiesPerPage = 12;
    private static int entitiesPerRow = 3;


    @GetMapping("/catalogthymeleaf")
    public String getCatalog(Model model,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestParam(name = "sortby", required = false, defaultValue = "ru") String sortingType,
                             @RequestParam(name = "year", required = false, defaultValue = "-1") String yearRequired,
                             @RequestParam(name = "genre", required = false, defaultValue = "all") String genreRequired,
                             @RequestParam(name = "country", required = false, defaultValue = "all") String countryRequired,
                             @RequestParam(name = "search", required = false, defaultValue = "null") String searchRequest) {
       System.out.println(
                        "Доступ к каталогу от: " + request.getRemoteAddr() +
                        "; Тип сортировки: " + sortingType +
                        "; Запрошенный год: " + yearRequired +
                        "; Запрошенный жанр:" + genreRequired +
                        "; Запрошенная страна: " + countryRequired +
                        "; Поиск по тексту: " + searchRequest);
        DAO dao = DAO.getInstance(Application.sourceBase).
                prepareData().
                filterByYear(Integer.parseInt(yearRequired)).
                filterByGenre(genreRequired).
                filterByCountry(countryRequired).
                filterBySearchRequest(searchRequest).
                sortBy(sortingType);
        model.addAttribute("listOfGenres", dao.getAllGenres());
        model.addAttribute("listOfYears", dao.getAllYears());
        model.addAttribute("listOfCountries", dao.getAllCountries());
        model.addAttribute("listOfMovies", dao.getListOfEntities());
        System.out.println(response.getContentType());
        return "catalogthymeleaf";
    }

    @GetMapping("/catalog")
    public String searchForMovie() {

        return "catalog";
    }

    @GetMapping("/raw/catalog")
    public ResponseEntity<List<MovieEntity>> rawCatalogRequestHandler
            (HttpServletRequest request,
             HttpServletResponse response,
             @RequestParam(name = "year", required = false, defaultValue = "null") String yearRequired,
             @RequestParam(name = "genre", required = false, defaultValue = "null") String genreRequired,
             @RequestParam(name = "country", required = false, defaultValue = "null") String countryRequired,
             @RequestParam(name = "search", required = false, defaultValue = "null") String searchRequest,
             @RequestParam(name = "sortby", required = false, defaultValue = "null") String sortingType,
             @RequestParam(name = "page", required = false, defaultValue = "null") String page){
        System.out.println(
                "=============================================\n" +
                        "Доступ к REST каталогу от: " + request.getRemoteAddr() +
                        "; Запрошенный год: " + yearRequired +
                        "; Запрошенный жанр:" + genreRequired +
                        "; Запрошенная страна: " + countryRequired +
                        "; Поиск по тексту: " + searchRequest+
                        "; Тип сортировки: " + sortingType);
        List<MovieEntity> list = DAO.getInstance(Application.sourceBase)
                .prepareData()
                .filterByYear(yearRequired)
                .filterByGenre(genreRequired)
                .filterByCountry(countryRequired)
                .filterBySearchRequest(searchRequest)
                .sortBy(sortingType)
                .getListOfEntities();
        if (!page.contentEquals("null")) {
            try {
                System.out.println("Number Format Exception Catched");
                int pagge = Integer.parseInt(page);
                list = subListed(list, pagge);

            } catch (NumberFormatException exception) {
                return new ResponseEntity<>(list, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/raw/{name}")
    public ResponseEntity<List> returnValues(
            @PathVariable String name){
        List list = DAO.getInstance(Application.sourceBase).prepareData().getAllAvailableParameters(name);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    private List subListed(List rawList, int page){
        int lastIndexOfRawList = rawList.size() - 1;
        int beginIndex = (page - 1) * entitiesPerPage;
        if (beginIndex > lastIndexOfRawList) return rawList;
        int endIndex = page*entitiesPerPage - 1;
        if (endIndex > lastIndexOfRawList) endIndex = lastIndexOfRawList;
        return rawList.subList(beginIndex, endIndex+1);
    }


}
