package form.view;

public class CreateUserEvent {
	private String first_name;
	private String last_name;
	private String faculty;
	private String specialty;
	private int course;
	
	public CreateUserEvent(String first_name, String last_name, String faculty, String specialty, int course){
		this.first_name = first_name;
		this.last_name = last_name;
		this.faculty = faculty;
		this.specialty = specialty;
		this.course = course;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getFaculty() {
		return faculty;
	}

	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public int getCourse() {
		return course;
	}

	public void setCourse(int course) {
		this.course = course;
	}
}
