package ru.kukuruzvelt.application.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kukuruzvelt.application.Application;
import ru.kukuruzvelt.application.domain.DAO;
import ru.kukuruzvelt.application.model.MovieEntity;

@Slf4j
@Controller
public class MoviePageController {

    @GetMapping("/movie/{name}")
    public String videoController1(Model model, @PathVariable String name){
        try {
            MovieEntity me = DAO.getInstance(Application.sourceBase).findByWebMapping(name);
            // var loggingEventBuilder = log.atDebug();
            System.out.println("Доступ к странице просмотра: " + name);
            model.addAttribute("pageTitle", me.getTitleRussian());
            model.addAttribute("page", "file/" + name);
            return "video";
        }
        catch (NullPointerException npe){
            return "404";
        }
    }

    @GetMapping("raw/movie/{name}")
    public ResponseEntity movieData(@PathVariable String name){
        MovieEntity me = DAO.getInstance(Application.sourceBase).findByWebMapping(name);
        return new ResponseEntity<MovieEntity>(me, HttpStatus.OK);
    }


}
