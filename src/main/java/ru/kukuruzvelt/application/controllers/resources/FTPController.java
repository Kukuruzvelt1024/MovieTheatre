package ru.kukuruzvelt.application.controllers.resources;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class FTPController {

    @GetMapping("/ftp")
    public String getPage(Model model){
        Path dir = Paths.get("B:/FTP");
        List<String> listOfFileNames = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(dir)) {
            listOfFileNames = paths
                    .filter(Files::isRegularFile) // Оставляем только файлы
                    .map(s -> s.getFileName())
                    .map(s -> s.toString())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("items", listOfFileNames);
        return "ftp";
    }
}
