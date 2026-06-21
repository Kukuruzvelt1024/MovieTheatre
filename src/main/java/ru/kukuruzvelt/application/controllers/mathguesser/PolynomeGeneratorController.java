package ru.kukuruzvelt.application.controllers.mathguesser;

//import com.vaadin.copilot.shaded.io.netty.handler.codec.http.HttpScheme;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Handler;

@Controller
public class PolynomeGeneratorController {

    @GetMapping("/rndpol")
    public ResponseEntity<String> rndPolynome(){
        return new ResponseEntity<String>(Polynome.createNewRandomPolynome(3).toString(), HttpStatus.OK);
    }
}
