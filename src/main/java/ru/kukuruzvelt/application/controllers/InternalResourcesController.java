package ru.kukuruzvelt.application.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import ru.kukuruzvelt.application.domain.CatalogDAOJDBC;
import ru.kukuruzvelt.application.model.MovieEntity;
import java.io.*;
import java.sql.SQLException;

@Controller
public class InternalResourcesController {
    private static String sourceFolder = "B:\\src\\";
    private static String cssFolder = "B:\\IdeaProjects\\application\\src\\main\\resources\\templates\\css\\";
    private static String jsFolder = "B:\\IdeaProjects\\application\\src\\main\\resources\\templates\\js\\";
    private static String posterFolder = "B:\\posters\\";
    private static String assetsFolder = "B:\\assets\\";

    @Autowired
    private MyResourceHttpRequestHandler handler;

    @GetMapping("/file/{name}")
    public void getVideoFile(HttpServletRequest request,
                             HttpServletResponse response,
                             @PathVariable String name)
            throws ServletException, IOException {
        System.out.println("Отправка видеофрагмента: " + name
                + " на адрес: " + request.getRemoteAddr()
                + " Range: " + request.getHeader("Range"));
        File source = new File(sourceFolder, name);
        request.setAttribute(MyResourceHttpRequestHandler.ATTR_FILE, source);
        handler.handleRequest(request, response);

    }

    @GetMapping("internal/{sourceType}/{fileName}")
    public StreamingResponseBody getFileFromLocalFileSystem(
            @PathVariable String sourceType,
            @PathVariable String fileName) throws IOException, SQLException {
        String localStorageSourcehPath = null;
        if (sourceType.contentEquals("css")) localStorageSourcehPath = cssFolder.concat(fileName);
        if (sourceType.contentEquals("javascript")) localStorageSourcehPath = jsFolder.concat(fileName);
        if (sourceType.contentEquals("assets")) localStorageSourcehPath = assetsFolder.concat(fileName);
        if (sourceType.contentEquals("poster")){
            CatalogDAOJDBC dao = new CatalogDAOJDBC(); //System.out.println("Отправка постера: " + me.getPosterFileName());
            MovieEntity me = dao.findByWebMapping(fileName);
            localStorageSourcehPath = posterFolder.concat(me.getPosterFileName());
        }
        final InputStream fileStream = new FileInputStream(localStorageSourcehPath);
        long size = fileStream.available();
        return (os) -> {readAndWrite(fileStream, os);
        };
    }

    private void readAndWrite(final InputStream is, OutputStream os)
            throws IOException {
        byte[] data = new byte[1048576];
        int read = 0;
        while ((read = is.read(data)) > 0) {
            os.write(data, 0, read);

        }
        os.flush();
    }

    @Component
    final static class MyResourceHttpRequestHandler extends ResourceHttpRequestHandler {

        private final static String ATTR_FILE = MyResourceHttpRequestHandler.class.getName() + ".file";
        @Override
        protected Resource getResource(HttpServletRequest request) throws IOException {
            final File file = (File) request.getAttribute(ATTR_FILE);
            return new FileSystemResource(new File(sourceFolder.concat(new CatalogDAOJDBC().
                    findByWebMapping(extractWebMapping(request)).getVideoFileName())));
        }

        private String extractWebMapping(HttpServletRequest request){
            return request.getRequestURL().
                            toString().
                            split("/file/")[1];
        }
    }
}
