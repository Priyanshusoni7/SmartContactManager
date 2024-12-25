package com.ctm.contactManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ctm.contactManager.entities.UserRoleList;

public interface UserRoleListRepo extends JpaRepository<UserRoleList,Long> {

}
