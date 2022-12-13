package com.example.demo.Service;

import com.example.demo.Model.Contact;
import com.example.demo.Model.User;
import com.example.demo.Repository.ContactRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.config.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;


    @Autowired
    ContactRepository contactRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public  void saveUserContact(Contact contact, Principal principal, MultipartFile contactImage, HttpSession session) {

        try {
            //email
            String email = principal.getName();
            Optional<User> curUser = userRepository.findByEmail(email);

            if (curUser.isPresent()) {

                //bi-directional association

                String filename=contactImage.getOriginalFilename();

              contact.setImage(Base64.getEncoder().encodeToString(contactImage.getBytes()));
                contact.setUser(curUser.get());
                curUser.get().getContacts().add(contact);

                userRepository.save(curUser.get());
                session.setAttribute("message",new Message("Added to contact successfully","alert-success"));
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public String saveUser(User user, Model model, HttpSession session, BindingResult result) {



        try {
            String encryptedPass= passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPass);
            user.setRole("user");
            user.setEnabled(true);
            user.setImage("logo.jpg");
            userRepository.save(user);
            model.addAttribute("user",user);


        } catch (Exception e) {

            e.printStackTrace();
           session.setAttribute("message",new Message("something went wrong "+e.getMessage(),"alert-error"));
            return "signup";

        }

        session.setAttribute("message",new Message("Registration Success ","alert-success"));
        return "signup";
    }
    public String verifyUser(String email, String password, HttpSession session){

        Optional<User> user=userRepository.findByEmail(email);

        if (user.isPresent()) {

                User currUser=user.get();

                if(currUser.getPassword().equals(password)){
                    session.setAttribute("currentUser",currUser);
                    session.setAttribute("message",new Message("Success","alert-error"));

                }
                else{
                    session.setAttribute("message",new Message("Incorrect details.","alert-error"));
                }
        }
        else{
    session.setAttribute("message",new Message("Incorrect details.","alert-error"));
        }
        return "login";

    }

    public User setUsermodel(String email,Model model) {

        Optional<User> user= Optional.ofNullable(userRepository.findUserByUsername(email));
        if(user.isPresent()){

            return user.get();
        }
        else{
            return null;
        }
    }

    public List<Contact> listUserContacts(Model model) {

        User currUser= (User) model.getAttribute("user");
        return currUser.getContacts();

    }



    public void deleteContact(int contactId, HttpSession session) {

            contactRepository.deleteContactById(contactId);
            session.setAttribute("message",new Message("Contact deleted succesfully","alert-success"));

    }

    public void modifyContact(Contact newcontact,int contactId,MultipartFile file) throws IOException {
try{
        if(file.isEmpty()){
            //get exisiting image if not provide....
            //decode encode and set
            Contact con=contactRepository.getReferenceById(contactId);
            String image= con.getImage();
            byte[] decodedBytes=Base64.getDecoder().decode(image);

            newcontact.setImage(Base64.getEncoder().encodeToString(decodedBytes));
        }
        else{
            //if new image provided
            newcontact.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
        }


        contactRepository.modifyContact( newcontact.getName(),
                 newcontact.getEmail(),
                 newcontact.getPhone(),
                 newcontact.getWork(),
                 newcontact.getDescription(),
                newcontact.getImage(),
                contactId);
}catch (IOException e){
    System.out.println(e.getMessage());
}


    }

    public Contact getAContact(Integer contactId) {

        return contactRepository.getReferenceById(contactId);
    }

    public boolean checkContactOwner(int cid, Model model) {

        User user= (User) model.getAttribute("user");

        Contact contact=contactRepository.getReferenceById(cid);

        return contact.getUser().getId() == user.getId();
    }

    public boolean checkEmail(String email) {

       Optional<User> user= userRepository.findByEmail(email);

       if(user.isPresent()){
           return true;
       }
       else return false;


    }
}


