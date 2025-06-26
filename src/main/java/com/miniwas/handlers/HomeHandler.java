package com.miniwas.handlers;


import com.miniwas.SessionManager;
import com.miniwas.db.UserDao;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class HomeHandler implements Handler{
    private final UserDao dao;
    private final SessionManager sessionManager;

    public HomeHandler(UserDao dao, SessionManager sessionManager) {
        this.dao  = dao;
        this.sessionManager = sessionManager;
    }
    @Override
    public void handle(BufferedReader in,
                       BufferedWriter out,
                       String method,
                       String path){

    }
}
