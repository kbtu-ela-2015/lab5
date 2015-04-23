package form.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
	
	public void addStudent(Student student) throws SQLException{
		Connection conn = Database.getInstance().getConnection();
		
		PreparedStatement stmt = conn.prepareStatement("insert into students (first_name, last_name, faculty, specialty, course) values(?,?,?,?,?)");
		
		stmt.setString(1, student.getFirst_name());
		stmt.setString(2, student.getLast_name());
		stmt.setString(3, student.getFaculty());
		stmt.setString(4, student.getSpecialty());
		stmt.setInt(5, student.getCourse());
		
		stmt.executeUpdate();
		System.out.println("added");
		stmt.close();
	}
	
	public void updateStudent(){
		
	}
	
	public void deleteStudent(){
		
	}

	public List<Student> getStudents() throws SQLException{
		List<Student> students = new ArrayList<Student>();
		
		Connection conn = Database.getInstance().getConnection();
		
		String SQL = "select * from students order by id";
		Statement selectStmt = conn.createStatement();
		
		ResultSet results = selectStmt.executeQuery(SQL);
		
		while(results.next()){
			String first_name = results.getString("first_name");
			String last_name = results.getString("last_name");
			String faculty = results.getString("faculty");
			String specialty = results.getString("specialty");
			int course = results.getInt("course");
			
			Student s = new Student(first_name, last_name, faculty, specialty, course);
			students.add(s);
			
		}
		
		results.close();
		selectStmt.close();
		
		return students;
	}
}
