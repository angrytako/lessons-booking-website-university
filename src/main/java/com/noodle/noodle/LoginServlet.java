package com.noodle.noodle;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "loginServlet", value = "/loginServlet")
public class LoginServlet extends HttpServlet {
    private String message;

    public void init() {
        DAO.DAO.registerDriver("http://localhost:3306/noodle","admin","");
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
            buffer.append(System.lineSeparator());
        }
        String data = buffer.toString();
        JsonObject jobj = new Gson().fromJson(data, JsonObject.class);

        // 1. JSON file to Java object
        String username =  jobj.get("username").toString();
        String password =  jobj.get("password").toString();

       System.out.println(username+ "  " +password);
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        out.print(data);
        out.flush();
       return;
    }

    public void destroy() {
    }
}