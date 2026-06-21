package ru.kukuruzvelt.application.controllers.mathguesser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kukuruzvelt.application.model.Polynome;

@Controller
public class MathguesserController {

    @GetMapping("/newrandompolynome")
    public ResponseEntity<String> mathPage(HttpServletRequest req,
                                           HttpServletResponse resp,
                                           @RequestParam(name = "maxPower", required = false, defaultValue = "1") int maxPower){
        Polynome p = Polynome.createNewRandomPolynome(maxPower);
        System.out.println(req.getSession().getCreationTime());
        return new ResponseEntity<String>(p.toString(), HttpStatus.OK);
    }
}
