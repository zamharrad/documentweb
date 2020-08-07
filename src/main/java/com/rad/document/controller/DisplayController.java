package com.rad.document.controller;

import com.rad.document.enttiy.Document;
import com.rad.document.repos.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class DisplayController {

    @Autowired
    DocumentRepository documentRepository;

    @RequestMapping(value = "/displayUpload")
    public String displayUpload(ModelMap modelMap) {
        List<Document> documents = (List<Document>) documentRepository.findAll();
        modelMap.addAttribute("documents",documents);
        return "documentUpload";
    }

    @RequestMapping(value = "/upload", headers=("content-type=multipart/*"),method = RequestMethod.POST ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadDocument(@RequestParam("document") MultipartFile multipartFile, @RequestParam("id") Long id , ModelMap modelMap) {

        Document document = new Document();
        document.setId(id);
        document.setName(multipartFile.getOriginalFilename());

        try {
            document.setData(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        documentRepository.save(document);

        List<Document> documents = (List<Document>) documentRepository.findAll();
        modelMap.addAttribute("documents",documents);

        return "documentUpload";
    }

    @RequestMapping("/download")
    public StreamingResponseBody download(@RequestParam("id") Long id, HttpServletResponse response){
        Optional<Document> document = documentRepository.findById(id);
        byte[] data = document.get().getData();

        response.setHeader("Content-Disposition","attachment;filename="+document.get().getName());

        return outputStream -> {
            outputStream.write(data);
        };
    }
}
