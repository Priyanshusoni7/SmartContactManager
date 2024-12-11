package com.ctm.contactManager.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ctm.contactManager.entities.contact;
import com.ctm.contactManager.entities.user;
import java.util.List;


@Repository
public interface ContactRepo extends JpaRepository<contact,String> {


    // find by contact by user

    //custion finder method
    Page<contact> findByUser(user user, Pageable pageable);

    //custon query method
    @Query("SELECT c FROM contact c WHERE c.user.id = :userId")
    List<contact> findByUserId(@Param("userId") String userId);

    Page<contact> findByUserAndNameContaining(user user, String name, Pageable pageable);

    Page<contact> findByUserAndEmailContaining(user user, String email, Pageable pageable);

    Page<contact> findByUserAndPhoneNumberContaining(user user, String phoneNumber, Pageable pageable);
}