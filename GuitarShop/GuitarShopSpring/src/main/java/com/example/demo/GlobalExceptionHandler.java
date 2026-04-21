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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.exception.BusinessException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {
//        model.addAttribute("errorMessage", ex.getMessage());
//        model.addAttribute("stackTrace", ex.getStackTrace());
//        String currentDirectory = System.getProperty("user.dir");
//        System.out.println("Current working directory: " + currentDirectory);
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
	
	@ExceptionHandler(BusinessException.class)
    public String handleAllExceptions(BusinessException ex, Model model) {
        model.addAttribute("exMessage", ex.getMessage());
//        model.addAttribute("stackTrace", ex.getStackTrace());
//        String currentDirectory = System.getProperty("user.dir");
//        System.out.println("Current working directory: " + currentDirectory);
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
	
//	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req, RedirectAttributes redirects) {
//        String error = String.format("Parameter " + ex.getName() + " has invalid value " + ex.getValue() + " - expected type " + ex.getRequiredType().getSimpleName());
////        return ResponseEntity.badRequest().body(error);
//        if(ex.getName().equalsIgnoreCase("phonenumber"))
////        	req.getSession().setAttribute("updatePhoneNumberStatus", "Number is too long");
//        	redirects.addFlashAttribute("updatePhoneNumberStatus", "Number is too long");
//        return "redirect:redirectToAccount";
//    }
	
	@ExceptionHandler(IllegalStateException.class)
  public String handleTypeMismatch(IllegalStateException ex, HttpServletRequest req, RedirectAttributes redirects) {
		redirects.addFlashAttribute("exMessage", "Internal Error");
		return "redirect:/user/redirectToIndex";
  }
	
	
//	@ExceptionHandler(DuplicateEntityException.class)
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public ErrorResponse handleDuplicate(DuplicateEntityException e) {
//        return new ErrorResponse(e.getCode(), e.getMessage());
//        // {"code": "DUPLICATE", "message": "Username already taken"}
//    }
	
	
}
