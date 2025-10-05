package ru.kukuruzvelt.application.controllers;

import jakarta.servlet.http.*;
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

    private static int entitiesPerPage = 12;
    private static int entitiesPerRow = 3;
    DAO DataAccessObject = new DAOJdbcImpl();

    @GetMapping("/catalog")
    public String staticHTMLCatalogPageHandler() {
        return "catalog";
    }

    @GetMapping("/raw/catalog")
    public ResponseEntity<List<MovieEntity>> rawCatalogRequestHandler
            (HttpServletRequest request,
             HttpServletResponse response,
            @RequestParam Map<String, String> paramsMap)  {
        System.out.println("Fetch to RAW catalog from: " + request.getRemoteAddr());
        Long quantity = DataAccessObject.receiveNotPaginatedListQuantity(paramsMap);
        List paginatedResultList = DataAccessObject.findAndFilterEntitiesPaginated(paramsMap, entitiesPerPage);
        response.setHeader("EntitiesPerPage", String.valueOf(entitiesPerPage));
        response.setHeader("EntitiesPerRow", String.valueOf(entitiesPerRow));
        response.setHeader("ResultSetSize", String.valueOf(quantity));
        return new ResponseEntity<>(paginatedResultList, HttpStatus.OK);
    }

    @GetMapping("/raw/{type}")
    public ResponseEntity<List> returnValues(@PathVariable String type) throws SQLException {
        return new ResponseEntity<>(DataAccessObject.findAllUniqueValueFromRequiredColumn(type), HttpStatus.OK);
    }

}
