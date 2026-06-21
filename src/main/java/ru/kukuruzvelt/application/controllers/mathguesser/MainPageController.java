package ru.kukuruzvelt.application.controllers.mathguesser;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller

public class MainPageController {
    private static HashMap<String, Polynome> sessionPolynome = new HashMap<>();

    @GetMapping("/math")
    public String mainPage(HttpServletRequest request,
                           Model model, @RequestParam(value = "function", required = false) String function,
                           @RequestParam(value = "show", required = false) String show) {
        String currentSessionId = request.getSession().getId();
        request.getSession().setMaxInactiveInterval(300);
        if (sessionPolynome.get(currentSessionId) == null)   {
            Polynome p = Polynome.createNewRandomPolynome(2);
            sessionPolynome.put(currentSessionId, p);

        }
        Polynome p = sessionPolynome.get(currentSessionId);
        Table table = new Table(p, 1, 6);
        if (function != null) {
            if (function.contentEquals(p.toString())) {
                model.addAttribute("result", "Правильно");
            }
            else{
                model.addAttribute("result", "Неправильно");
            }
        }
        if(show != null){

                model.addAttribute("polynome", p.toString());

        }
        System.out.println(p);
        model.addAttribute("myMap", table.getMap());
        return "math";
    }

    @GetMapping ("/math/reset")
    public String postPage(HttpServletRequest req){
        sessionPolynome.remove(req.getSession().getId());
        return  "redirect:/math";

    }

    @GetMapping ("/math/show")
    public String pddPage(HttpServletRequest req){

        return  "redirect:/math?show";

    }
}
