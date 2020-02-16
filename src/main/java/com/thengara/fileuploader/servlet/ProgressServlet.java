package com.thengara.fileuploader.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/progress")
public class ProgressServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProgressServlet() {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		PrintWriter printWriter = response.getWriter();
		printWriter.println(
				"<html><head><title>Progress Servlet</title></head><body>");
		printWriter.println("Entering doGet() ==> "
				+ Thread.currentThread().getName() + "<br/>");
		printWriter.println("<span id='myspan'> Initial </span><br/>");
		printWriter
				.println("<progress id='progress' max='100'></progress><br/>");
		int i = 0;

		String running = "Running";

		while (i < 100) {
			try {
				StringBuilder builder = new StringBuilder();
				builder.append(running);
				printWriter.println(
						"<script>document.getElementById('progress').value=\""
								+ i++ + "\";</script>");
				printWriter.flush();
				for (int j = 0; j < 5; j++) {
					builder.append(".");
					printWriter.println(
							"<script>document.getElementById('myspan').innerHTML=\""
									+ builder.toString() + "\";</script>");
					printWriter.flush();
					Thread.sleep(20);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		printWriter.println(
				"<script>document.getElementById('myspan').innerHTML=\"Done\";</script>");
		printWriter.println(
				"<script>document.getElementById('progress').style.display='none';<script>");
		printWriter.println(
				"[time consuming and generated and returned result]<br>");
		printWriter.println("Entering doGet() ==> "
				+ Thread.currentThread().getName() + "<br/>");
		printWriter.println("</body></html>");

	}

}
