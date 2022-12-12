package com.example.demo.Controller;


import com.example.demo.Model.Contact;
import com.example.demo.Model.User;
import com.example.demo.Service.ContactService;
import com.example.demo.Service.UserService;
import com.example.demo.config.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userservice;

    @Autowired
   ContactService contactService;

    public void setModel(Model model,Principal principal){
        model.addAttribute("title","User_dashboard");

        //principal is object of customUser detail...
        //here getName() returns email instead of name
        //modified considering email unique
        //else modify CustomeUserDetails method getName to return name instead of email
        String email=principal.getName();

        User currUser=userservice.setUsermodel(email,model);

        model.addAttribute("user",currUser);

    }

@GetMapping("/index")
    public String userLogged(Model model, Principal principal){
        setModel(model,principal);
        return "userTemplates/welcomeUser";
}

@GetMapping("/addContact")
    public String addContactSection(Model model, Principal principal){
        setModel(model,principal);
        return "userTemplates/addContact";
}


@PostMapping("/saveContact")
    public String saveContacts(Principal principal, Model model , @ModelAttribute Contact contact, @RequestParam("Profile")MultipartFile ContactImage, HttpSession session){

        setModel(model,principal);
        userservice.saveUserContact(contact,principal,ContactImage,session);
        return "redirect:/user/addContact";

}

@GetMapping("/viewContacts")
public String showContact(Principal principal,Model model,HttpSession session){
        setModel(model,principal);
      List<Contact> contacts=  userservice.listUserContacts(model);

      if(contacts.isEmpty()){
          session.setAttribute("message",new Message("Your contacts is empty.Add contact to view","alert-secondary"));
      }
      else{
          model.addAttribute("contactList",contacts);
      }

        return "userTemplates/viewContact";
}

@GetMapping("/deleteContact/{id}")
    public String deleteContact(@PathVariable("id") int contactId,Principal principal,Model model,HttpSession session){

        setModel(model,principal);
        if(ownsContact(contactId,model)) {
            userservice.deleteContact(contactId,  session);
        }
        else{
            session.setAttribute("message",new Message("Doesnt belong to this user","alert-danger"));
        }

        return "redirect:/user/viewContacts";


}



//update page
@GetMapping("/updateContact/{id}")
    public String updateContactPage(@PathVariable("id") Integer contactId,Principal principal,Model model,HttpSession session){

        setModel(model,principal);
        if(ownsContact(contactId,model)) {
            Contact contact = userservice.getAContact(contactId);
            model.addAttribute("contact", contact);

            return"userTemplates/updateContact";

        }
        else{
            session.setAttribute("message",new Message("You donot own this contact","alert-danger"));
            return "redirect:/user/viewContacts";
        }

}

//Contact update processing
@PostMapping("/saveModifiedContact/{id}")
public String updateContact(@ModelAttribute Contact contact ,@PathVariable("id") int contactId,@RequestParam("Profile")MultipartFile file,Principal principal,Model model,HttpSession session) throws IOException {

        setModel(model, principal);

        if(ownsContact(contactId,model)) {
            userservice.modifyContact(contact, contactId, file);
            session.setAttribute("message",new Message("Contact modified successfully","alert-success"));
        }
        else{
            session.setAttribute("message",new Message("You dont own that contact.Illegal access","alert-danger"));
        }
        return "redirect:/user/viewContacts";
}


//to check if current user owns the contact that is to be modified.
boolean ownsContact(int cid ,Model model){

        return userservice.checkContactOwner(cid,model);

}


}
