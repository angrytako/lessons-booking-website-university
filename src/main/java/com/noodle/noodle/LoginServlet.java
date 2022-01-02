package com.noodle.noodle;

import DAO.Utente;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "loginServlet", value = "/loginServlet")
public class LoginServlet extends HttpServlet {

    public void init(ServletConfig config) {
        try {
            super.init(config);
            ServletContext ctx = config.getServletContext();
            String url = ctx.getInitParameter("url");
            String user = ctx.getInitParameter("user");
            String password = ctx.getInitParameter("password");
            DAO.DAO.registerDriver(url, user, password);
        } catch (ServletException e){
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("UTF-8");

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
        String username =  jobj.get("username").getAsString();
        String pw =  jobj.get("password").getAsString();
        Utente user = DAO.DAO.getUser(username,pw);
        if(user != null) {
            System.out.println(user.toString());
            HttpSession session=request.getSession();
            session.setAttribute("username",user.getUsername());
            session.setAttribute("role",user.getRuolo());
            out.print(jobj.toString());
            response.setStatus(200);
        }
        else{
            out.print("{\"error\":\"username or password are incorrect\"}");
            response.setStatus(400);
        }

        out.flush();
       return;
    }
private String hashPw(String pw){
    try {
        // Static getInstance method is called with hashing MD5
        MessageDigest md = MessageDigest.getInstance("MD5");

        // digest() method is called to calculate message digest
        //  of an input digest() return array of byte
        byte[] messageDigest = md.digest(pw.getBytes("UTF-8"));

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value
        String hashString = no.toString(16);
        while (hashString.length() < 32) {
            hashString = "0" + hashString;
        }
        return hashString;
    }
    catch(NoSuchAlgorithmException e) {
        // For specifying wrong message digest algorithms
        throw new RuntimeException(e);
    }
    catch(UnsupportedEncodingException e) {
        // For specifying wrong message digest algorithms
        throw new RuntimeException(e);
    }
        }

    public void destroy() {
    }
}