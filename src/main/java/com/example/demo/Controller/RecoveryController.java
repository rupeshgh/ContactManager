package com.example.demo.Controller;

import com.example.demo.Service.EmailService;
import com.example.demo.Service.UserService;
import com.example.demo.config.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Random;

@Controller
public class RecoveryController {

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @GetMapping("/ForgotPassword")
    public String forgetPass(){
        return"Recovery/ForgotPassword";
    }

    @PostMapping("/verifyEmail")
    public String verifyAndSendOtp(@RequestParam("email") String email, HttpSession session) throws MessagingException {

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
            session.setAttribute("message",new Message("otp doesnt match","alert-danger"));
            return "/Recovery/verifyOtp";
        }

    }


}
