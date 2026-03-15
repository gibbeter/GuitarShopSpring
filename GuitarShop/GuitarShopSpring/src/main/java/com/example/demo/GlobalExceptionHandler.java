package com.example.demo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("stackTrace", ex.getStackTrace());
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Current working directory: " + currentDirectory);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(("E:\\Java\\GuitarShopSpring\\GuitarShop\\GuitarShopSpring\\src\\main\\webapp\\logs\\log "+ new Date().toString().replaceAll(":", "_") +".txt").replaceAll("\\s", "")))) {
        	writer.write(new Date().toString());
            writer.newLine();
        	writer.write(ex.getMessage());
            writer.newLine();
            String trace = Arrays.stream(ex.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\\n"));
            writer.write(trace);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return "pages/error";
    }
}
