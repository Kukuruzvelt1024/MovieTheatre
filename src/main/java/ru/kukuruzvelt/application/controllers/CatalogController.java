package ru.kukuruzvelt.application.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kukuruzvelt.application.domain.DAOTextFile;
import ru.kukuruzvelt.application.domain.DAOJdbcImpl;
import ru.kukuruzvelt.application.model.MovieEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
public class CatalogController {

    private static int entitiesPerPage = 12;
    private static int entitiesPerRow = 3;

    @GetMapping("/catalog")
    public String searchForMovie() {
        return "catalog";
    }

    @GetMapping("/raw/catalog")
    public ResponseEntity<List<MovieEntity>> rawCatalogRequestHandler
            (HttpServletRequest request,
             HttpServletResponse response,
            @RequestParam Map<String, String> paramsMap) throws SQLException {
        System.out.println("Fetch to RAW catalog");
        for(Map.Entry<String, String> entry : paramsMap.entrySet()){
            System.out.print(entry.getKey() + " = " + entry.getValue() + "; ");
        }
        DAOJdbcImpl dao = new DAOJdbcImpl();
        List<MovieEntity> melist = dao.findAllEntitiesFilteredByHTTPRequestParams(paramsMap);
        List<MovieEntity> reduced = dao.reduceToLimitOfEntitiesPerPage(paramsMap.get("page"), entitiesPerPage).getListOfEntities();
        response.setHeader("EntitiesPerPage", String.valueOf(entitiesPerPage));
        response.setHeader("EntitiesPerRow", String.valueOf(entitiesPerRow));
        response.setHeader("ResultSetSize", String.valueOf(melist.size()));
        return new ResponseEntity<>(reduced, HttpStatus.OK);
    }

    @GetMapping("/raw/{type}")
    public ResponseEntity<List> returnValues(@PathVariable String type) throws SQLException {
        DAOJdbcImpl dao = new DAOJdbcImpl();
        List list = dao.findAllDistinctParameterOfEntitiesByType(type);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }



    @GetMapping("/serialize")
    public void serialize(){
        DAOTextFile dao = new DAOTextFile();
        dao.prepareData();
        dao.serializeToCsv();
    }


}
