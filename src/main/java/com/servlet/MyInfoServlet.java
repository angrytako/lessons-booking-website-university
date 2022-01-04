package com.noodle.noodle;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MyInfoServlet", value = "/MyInfoServlet")
public class MyInfoServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		response.setStatus(200);
		if (session == null || session.getAttribute("role") == null) {
			System.out.println("{\"username\":null,\"role\":\"guest\" }");
			out.print("{\"username\":\"\",\"role\":\"guest\" }");
		} else {
			System.out.println("{\"username\":\"" + (String) session.getAttribute("username") +
					"\",\"role\":\"" + (String) session.getAttribute("role") + "\"}");
			out.print("{\"username\":\"" + (String) session.getAttribute("username") +
					"\",\"role\":\"" + (String) session.getAttribute("role") + "\"}");
		}
		out.flush();
	}
}
