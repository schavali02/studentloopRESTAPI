package com.shashank.loop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.model.ModelValidationException;

@Path("/students")
public class StudentAPI {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getStudents() {
	  try {
		  StringBuilder sb = new StringBuilder();
		  Class.forName("com.mysql.cj.jdbc.Driver");
		  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_loop?serverTimezone=UTC", "loop", "loop");
		  Statement stmt = connection.createStatement();
		  ResultSet rs = stmt.executeQuery("SELECT * from student");
		  while(rs.next()) {
			  sb.append("Student ID: " + rs.getInt("studentID") + " ");
			  sb.append(rs.getString("firstName") + " " + rs.getString("lastName"));
			  sb.append("\n");
		  }
		  return sb.toString();
		} catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
  }
  
  @GET
  @Path("{id}") // Read how  the path param is  used
  @Produces(MediaType.APPLICATION_JSON)
  public List<Student> getStudentById(@PathParam("id") String id) throws Exception {
	  List<Student> students = new ArrayList();
	  Class.forName("com.mysql.cj.jdbc.Driver");
	  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_loop?serverTimezone=UTC", "loop", "loop");
	  Statement stmt = connection.createStatement();
	  ResultSet rs = stmt.executeQuery("SELECT * from student WHERE studentID = '" + id + "'")  ;
	  while(rs.next()) {
		  Student  student = new Student(rs.getString("studentID"), rs.getString("firstName"), rs.getString("lastName"));
		  students.add(student);
	  }
	  return students;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Read  the significance of this
  @Produces(MediaType.APPLICATION_JSON)
  public  List<Student> saveStudent(@FormParam("firstName")String firstName, @FormParam("lastName")String lastName, @FormParam("studentID")String studentID) throws ModelValidationException, SQLException, ClassNotFoundException {
	  List<Student> students = new ArrayList();
	  Class.forName("com.mysql.cj.jdbc.Driver");
	  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_loop?serverTimezone=UTC", "loop", "loop");
	  Statement stmt = connection.createStatement();
	  String id = "insert into student (studentID,firstName, lastName) "
				+ " values(" + "'" + studentID + "','" + firstName + "','" + lastName + "')";
	  stmt.executeUpdate(id);
	  Student student = new Student(studentID, firstName, lastName);
	  students.add(student);
	  return students;
	  
  }
  
  // This method is called if XML is request
  @DELETE
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response sayXMLHello(@PathParam("id") String id) throws Exception {
	  Class.forName("com.mysql.cj.jdbc.Driver");
	  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_loop?serverTimezone=UTC", "loop", "loop");
	  Statement stmt = connection.createStatement();
	  stmt.executeUpdate("DELETE from student WHERE studentID = '" + id + "'");
	  return  Response.ok().build();
  }

}