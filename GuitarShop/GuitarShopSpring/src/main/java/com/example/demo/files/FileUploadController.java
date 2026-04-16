package com.example.demo.files;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.GuitarShopSpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {

    private final GuitarShopSpringApplication guitarShopSpringApplication;

    FileUploadController(GuitarShopSpringApplication guitarShopSpringApplication) {
        this.guitarShopSpringApplication = guitarShopSpringApplication;
    }

    @Value("${app.images.base-dir}")  // same folder as before, e.g. /path/to/png/folder
    private String baseDir;

    @PostMapping("/uploadImages")
    public String handleFileUpload(@RequestParam("webpFiles") MultipartFile[] files,
								   @RequestParam("prodId") Integer prodId,
								   @RequestParam("prodType") String prodType,
								   RedirectAttributes redirectAttributes) {
        if (files == null || files.length == 0) {
            redirectAttributes.addFlashAttribute("uploadStat", "Please select a WEBP file.");
            return "redirect:/product/redirectToModifyProductPage?prodId="+prodId;  // adjust to your actual page URL
        }
        
        List<String> uploaded = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".webp")) {
                errors.add(originalFilename + " is not a WEBP file.");
                continue;
            }

            try {
            	Path uploadPath = Paths.get(baseDir, prodType, "prod_" + prodId);
                System.out.println(uploadPath);
                Path targetLocation = uploadPath.resolve(originalFilename);
                System.out.println(targetLocation);
                
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                uploaded.add(originalFilename);
            } catch (IOException e) {
                errors.add(originalFilename + " failed: " + e.getMessage());
            }
        }

        String message = "";
        if (!uploaded.isEmpty()) {
            message += "Uploaded: " + String.join(", ", uploaded) + ". ";
        }
        if (!errors.isEmpty()) {
            message += "Errors: " + String.join(", ", errors);
        }
        redirectAttributes.addFlashAttribute("uploadStat", message);
        return "redirect:/product/redirectToModifyProductPage?prodId="+prodId;
    }
}
