package ru.kukuruzvelt.application.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kukuruzvelt.application.service.CatalogService;
import ru.kukuruzvelt.application.model.MovieEntity;

@Slf4j
@Controller
public class MoviePageController {

    @Autowired
    CatalogService CatalogService;

    @GetMapping("/movie/{name}")
    public String videoController1(Model model, @PathVariable String name){
        MovieEntity me = CatalogService.findMovieByWebmapping(name);
        setInfo(model, me);
        return "MoviePageTemplate";
    }

    @GetMapping("/movie/random")
    public String videoController2(Model model,String name){
        MovieEntity me = CatalogService.findRandomMovie(null);
        setInfo(model, me);
        return "MoviePageTemplate";
    }

    @GetMapping("raw/movie/{name}")
    public ResponseEntity movieData(@PathVariable String name){
        MovieEntity me = CatalogService.findMovieByWebmapping(name);
        return new ResponseEntity<MovieEntity>(me, HttpStatus.OK);
    }

    private void setInfo(Model model, MovieEntity me){
        model.addAttribute("pageTitle", me.getTitleRussian());
        model.addAttribute("page", "file/" + me.getWebMapping());
        model.addAttribute("RussianTitle", me.getTitleRussian());
        model.addAttribute("Countries", me.getCountriesAsString());
        model.addAttribute("Genres", me.getGenresAsString());
        model.addAttribute("Duration", me.getDuration() + " мин.");
        model.addAttribute("Year", me.getYear());
        model.addAttribute("OriginalTitle", me.getTitleOriginal());
        model.addAttribute("Directors", me.getDirectorsAsString());
    }


}
