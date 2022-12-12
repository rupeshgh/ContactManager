package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String home(Model model){
      model.addAttribute("title","Homepage");

        return "home";
    }


    @RequestMapping("/signup")
    public String signup(@Valid @ModelAttribute("user") User newUser){

        return "signUp";
    }

    @PostMapping("/register")
    public String registration(@ModelAttribute User user, Model model, HttpSession session, BindingResult result){

        return userService.saveUser(user,model,session,result);

    }

    @RequestMapping("/signin")
    public String getLogin(){
        return "login";
    }


//    @PostMapping ("/login")
//    public String login(@RequestParam("username")String email, @RequestParam("password") String password ,HttpSession session){
//
//
//    return userService.verifyUser(email,password,session);
//
//    }

}
