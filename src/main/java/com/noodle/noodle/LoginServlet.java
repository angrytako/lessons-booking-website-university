package com.noodle.noodle;

import DAO.Utente;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "loginServlet", value = "/loginServlet")
public class LoginServlet extends HttpServlet {
    private String message;

    public void init() {
        DAO.DAO.registerDriver("jdbc:mysql://localhost:3306/noodle","admin","");

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
        System.out.println(hashPw(password));

        Utente user = DAO.DAO.getUser(username,hashPw(password));
        if(user != null)
            System.out.println(user.toString());
//        HttpSession session=request.getSession();
//        session.setAttribute("uname","");
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        out.print(data);
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