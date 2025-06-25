package com.miniwas.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.miniwas.CustomResponse;

public class TestHandler implements Handler{
    @Override
    public void handle(BufferedReader in,
                BufferedWriter out,
                String method,
                String path) throws IOException {
        String res = new CustomResponse("test").getResponse();
        out.write(res);
        out.flush();
    }
}
