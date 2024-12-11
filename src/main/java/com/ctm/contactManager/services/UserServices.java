package com.ctm.contactManager.services;

import java.util.*;
import com.ctm.contactManager.entities.user;

public interface UserServices {

    user saveUser(user user);

    Optional<user> getUserById(String id);

    Optional<user> updateUser(user user);

    void deleteUser(String id);

    boolean checkUserExist(String userId);

    boolean checkUserExistByEmail(String email);

    List<user> getAllUsers();

    user getUserByEmail(String email);

    void resendVerificationEmail(user user);

}
