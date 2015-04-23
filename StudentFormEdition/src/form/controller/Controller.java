package form.controller;

import java.sql.SQLException;




import form.model.Model;
import form.model.Student;
import form.model.StudentDAO;
import form.view.CreateUserEvent;
import form.view.CreateUserListener;
import form.view.View;

public class Controller implements CreateUserListener {

	private View view;
	private Model model;
	
	private StudentDAO studentDAO = new StudentDAO();
	
	public Controller(View view, Model model){
		this.view = view;
		this.model = model;
	}

	@Override
	public void userCreated(CreateUserEvent event) {
		System.out.println("Login event received: " + event.getFirst_name() + "; " + event.getLast_name() + "; " + event.getCourse());
		
		try {
			studentDAO.addStudent(new Student(event.getFirst_name(), event.getLast_name(), event.getFaculty(), event.getSpecialty(), event.getCourse()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error");
			e.printStackTrace();
		}
		
	}
	
}
