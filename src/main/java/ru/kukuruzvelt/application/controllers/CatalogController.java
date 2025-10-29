package ru.kukuruzvelt.application.controllers;

import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kukuruzvelt.application.dataAccess.CatalogDataAccessHibernate;
import ru.kukuruzvelt.application.model.*;
import ru.kukuruzvelt.application.service.CatalogService;

import java.util.*;

@Controller
public class CatalogController {

    private static final int entitiesPerPage = 9;
    private static final int entitiesPerRow = 3;
    @Autowired
    private CatalogService catalogService;

    @GetMapping("/")
    public String redirect(){
        return "redirect:/catalog";
    }

    @GetMapping("/catalog")
    public String staticHTMLCatalogPageHandler() {
        return "catalog";
    }

    @GetMapping("/raw/catalog")
    public ResponseEntity<List<MovieEntity>> rawCatalogRequestHandler
            (HttpServletResponse response,
            @RequestParam Map<String, String> requestParameters)  {
        Long quantity = catalogService.findTotalRelevantRowsAmount(requestParameters);
        List<MovieEntity> paginatedResultList = catalogService.findMoviesByRequiredParameters(requestParameters);
        response.setHeader("EntitiesPerPage", String.valueOf(entitiesPerPage));
        response.setHeader("EntitiesPerRow", String.valueOf(entitiesPerRow));
        response.setHeader("ResultSetSize", String.valueOf(quantity));
        return new ResponseEntity<>(paginatedResultList, HttpStatus.OK);
    }

    @GetMapping("/raw/catalog/{column}")
    public ResponseEntity<List<String>> returnValues(
                        @PathVariable String column,
                        @RequestParam Map<String, String> paramsMap){
        List<String> result = catalogService.findAllDistinctValues(paramsMap, column);
        return new ResponseEntity<List<String>>(result, HttpStatus.OK);

    }

    @GetMapping("/{type}")
    public String redirect1(){
        return "redirect:/catalog";
    }

    @GetMapping("/hibernate/{type}")
    public ResponseEntity hiberTest(@PathVariable String type){
        CatalogDataAccessHibernate dao = new CatalogDataAccessHibernate();
        MovieEntity me = dao.findByWebMapping(type);
        return new ResponseEntity<>(me, HttpStatus.OK);
    }
}
