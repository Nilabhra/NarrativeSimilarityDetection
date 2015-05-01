package com.missionarogya.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.missionarogya.logic.DBHandler;
import com.missionarogya.logic.InsertBMJData;
import com.missionarogya.logic.SemanticDemo;

/**
 * Servlet implementation class Servlet
 */
@WebServlet("/servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Vector<JSONObject> fullCollection;
	static {
		try {
			if (DBHandler.getCount() == 0) {
				InsertBMJData.xmain();
			}
			fullCollection = DBHandler.retriveAll();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("should load once");
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Servlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// PrintWriter pw = response.getWriter();
		// pw.print("<html><head><meta charset='ISO-8859-1'><title>Demo</title></head><body><form action='servlet' id='inputform' method='post'></form><b>Enter Patient narrative: </b><br> <textarea rows='15' cols='50' form='inputform' name='txtarea' style='height: 244px; '>Enter text...</textarea><br><input type='submit' form='inputform' value='Get similar cases'> <hr/>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PriorityQueue<Double> scores = new PriorityQueue<>(
				Collections.reverseOrder());
		HashMap<Double, JSONObject> narrativemap = new HashMap<>();
		for (JSONObject s : fullCollection) {
			double score = SemanticDemo.computeSimilarity(
					request.getParameter("txtarea"),
					s.getString("dc:description"));
			if (score > 0) {
				scores.add(score);
				narrativemap.put(score, s);
			}
			// System.out.println(s.getString("dc:title"));
		}
		List<JSONObject> best3 = new ArrayList<>();
		for (int i = 0; i < 3 || scores.isEmpty(); ++i) {
			Double key = scores.poll();
			if (key != null) {
				System.out.println(key);
				best3.add(narrativemap.get(key));
			} else
				break;
		}
		PrintWriter pw = response.getWriter();
		pw.print("<html> <head><meta charset='ISO-8859-1'><title> Matches </title></head><body>");
		int rank = 1;
		for (JSONObject json : best3) {
			pw.print("<h4>Rank : " + rank + "</h4><h3>Title:</h3>"
					+ json.getString("dc:title") + "<br><h3>Description</h3>"
					+ json.getString("dc:description") + "<br><br><hr>");
			++rank;
		}
		pw.print("</body></html>");
	}
}
