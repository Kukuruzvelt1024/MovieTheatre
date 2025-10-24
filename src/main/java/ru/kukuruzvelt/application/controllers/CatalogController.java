package ru.kukuruzvelt.application.controllers;

import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kukuruzvelt.application.domain.*;
import ru.kukuruzvelt.application.model.*;
import java.util.*;

@Controller
public class CatalogController {

    private static final int entitiesPerPage = 9;
    private static final int entitiesPerRow = 3;
    @Autowired
    private CatalogGateway DataAccessObject;

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
        Long quantity = DataAccessObject.getFilteredNotPaginatedListSize(requestParameters);
        List<MovieEntity> paginatedResultList = DataAccessObject.findAllByRequiredParametersPaginated(requestParameters);
        response.setHeader("EntitiesPerPage", String.valueOf(entitiesPerPage));
        response.setHeader("EntitiesPerRow", String.valueOf(entitiesPerRow));
        response.setHeader("ResultSetSize", String.valueOf(quantity));
        return new ResponseEntity<>(paginatedResultList, HttpStatus.OK);
    }

    @GetMapping("/raw/{column}")
    public ResponseEntity<List<String>> returnValues(
                        @PathVariable String column,
                        @RequestParam Map<String, String> paramsMap){
        return new ResponseEntity<List<String>>(
                DataAccessObject.findAllUniqueValuesFromColumn(column, paramsMap), HttpStatus.OK);

    }

    @GetMapping("/{type}")
    public String redirect1(){
        return "redirect:/catalog";
    }

    @GetMapping("/hibernate/{type}")
    public ResponseEntity hiberTest(@PathVariable String type){
        CatalogDAOHibernate dao = new CatalogDAOHibernate();
        MovieEntity me = dao.findByWebMapping(type);
        return new ResponseEntity<>(me, HttpStatus.OK);
    }
}
