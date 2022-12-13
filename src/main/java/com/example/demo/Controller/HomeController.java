package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Service.EmailService;
import com.example.demo.Service.UserService;
import com.example.demo.config.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Random;

@Controller
public class HomeController {

    @Autowired
    EmailService emailService;
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



    @GetMapping("/ForgotPassword")
    public String forgetPass(){
        return"Recovery/ForgotPassword";
    }

    @PostMapping("/verifyEmail")
    public String verifyAndSendOtp(@RequestParam("email") String email,HttpSession session) throws MessagingException {

      if(userService.checkEmail(email) ) {
            session.setAttribute("email",email);
          Random random=new Random();
          int otp= random.nextInt(0000,99999);
          System.out.println(otp);
          emailService.sendEmail("Heres the otp",
                  otp,
                email

          );
          session.setAttribute("generatedOTP",otp);

      return "/Recovery/verifyOtp";
      }
      else{
          session.setAttribute("message",new Message("No such user","alert-danger"));
          return "signin";
      }

    }

    @PostMapping("/verifyOTP")
    public String verifyOtp(@RequestParam("userOTP") String userOtp,HttpSession session){
       int otp= (int) session.getAttribute("generatedOTP");
        int uOtp=Integer.parseInt(userOtp);
       if(otp==uOtp) {
           System.out.println("otp matches");

           return "/Recovery/changePassword";
       }
       else {
           return "home";
       }

    }




}
