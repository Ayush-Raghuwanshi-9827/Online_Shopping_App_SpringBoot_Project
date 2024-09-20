package com.ecomerse.Online_Shopping_App.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {


    @RequestMapping("/error")
            public String handleError(HttpServletRequest request, Model model){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String errorMsg = (String) request.getAttribute("javax.servlet.error.message");
        model.addAttribute("status", statusCode);
        model.addAttribute("error", errorMsg);
        return "message";

    }
}
