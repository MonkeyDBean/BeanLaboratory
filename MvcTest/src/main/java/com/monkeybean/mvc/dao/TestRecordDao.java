package com.monkeybean.mvc.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface AuthMapper {
    void saveUserAuth(UserAuth userAuth);

    UserAuth isUserExists(String userId);

    void updateUserAuth(UserAuth userAuth);

    UserAndAuth getAuthesByUserId(String userId);

    List<UserAuth> getUserAuthList();

    void deleteUserAuth(String id);
}
