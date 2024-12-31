package com.security.simple.auth;

public interface BlackList {

    /**
     * @param username user to block
     */
    void addBlackList(String username);

    /**
     * @param username user to unblock
     */
    void removeBlackList(String username);

    /**
     * @param username user to check
     * @return Whether the user belongs to a blacklist
     */
    boolean isBlackList(String username);

    /**
     * @brief clear blacklist
     */
    void clear();
}
