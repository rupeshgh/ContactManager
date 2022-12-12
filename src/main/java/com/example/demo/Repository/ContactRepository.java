package com.example.demo.Repository;

import com.example.demo.Model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ContactRepository extends JpaRepository<Contact,Integer> {
    @Modifying
    @Transactional
    @Query(value = "delete  from Contact c where c.id=:contactId")
    void deleteContactById(@Param("contactId")int contactId);



//    @Query(value = "update Contact c set c.name=:, c.email=:, c.work=:, c.description=:contact. where c.id=:cid")

    @Modifying
    @Transactional
    @Query(value = "update Contact c set c.name=?1, c.email=?2, c.phone=?3, c.work=?4,c.description=?5 ,c.image=?6 where c.id=?7")
    void modifyContact(String name,String email,String phone,String work,String description,String image,int contactId);
}
