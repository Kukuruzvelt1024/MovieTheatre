package ru.kukuruzvelt.application.controllers;

import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kukuruzvelt.application.domain.*;
import ru.kukuruzvelt.application.model.*;
import java.sql.SQLException;
import java.util.*;

@Controller
public class CatalogController {

    private static int entitiesPerPage = 9;
    private static int entitiesPerRow = 3;
    @Autowired
    MovieEntityDAO DataAccessObject;

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
            (HttpServletRequest request,
             HttpServletResponse response,
            @RequestParam Map<String, String> paramsMap)  {
        System.out.println("Fetch to RAW catalog from: "
                + request.getRemoteAddr()
        +" SessionID: " + request.getSession().getId());
        Long quantity = DataAccessObject.getFilteredNotPaginatedListSize(paramsMap);
        List paginatedResultList = DataAccessObject.recievePaginatedFilteredList(paramsMap, entitiesPerPage);
        response.setHeader("EntitiesPerPage", String.valueOf(entitiesPerPage));
        response.setHeader("EntitiesPerRow", String.valueOf(entitiesPerRow));
        response.setHeader("ResultSetSize", String.valueOf(quantity));
        return new ResponseEntity<>(paginatedResultList, HttpStatus.OK);
    }

    @GetMapping("/raw/{type}")
    public ResponseEntity<List> returnValues(@PathVariable String type, HttpServletRequest request) throws SQLException {
        System.out.println("Fetch to raw type SESSION ID: " + request.getSession().getId());
        return new ResponseEntity<>(DataAccessObject.findAllUniqueValueFromRequiredColumn(type), HttpStatus.OK);
    }

    @GetMapping("/{type}")
    public String redirect1(){
        return "redirect:/catalog";
    }

    @GetMapping("hibernate")
    public String hiberTest(){
        MovieEntityDAOHibernate dao = new MovieEntityDAOHibernate();
        return null;
    }
}
