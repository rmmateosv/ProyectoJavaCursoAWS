package rds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AlumnoDAO {
	
	private static final String HOSTMYSQL = "bd-mysql.cxb7efc1zowb.us-east-1.rds.amazonaws.com";
	private static final String HOSTORACLE = "bd-oracle.cxb7efc1zowb.us-east-1.rds.amazonaws.com";
	private static final String PORTMYSQL = "3306";
	private static final String PORTORACLE = "1521";
	
	private static final String USERMYSQL = "admin";
	private static final String PSMYSQL = "admindam";
	private static final String USERORACLE = "admin";
	private static final String PSORACLE = "admindam";
	
	private  String url = null;
	private  String user = null;
	private  String ps = null;
	
	private Connection cnx = null;

	public AlumnoDAO(String tipo) {

		try {
			if (tipo.equalsIgnoreCase("ORACLE")) {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				url = "jdbc:oracle:thin:@"+HOSTORACLE+":"+PORTORACLE+":ORCL";
				user = USERORACLE;
				ps=PSORACLE;
			}
			if (tipo.equalsIgnoreCase("MYSQL")) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				url = "jdbc:mysql://"+HOSTMYSQL+":"+PORTMYSQL+"/alumnos";
				user = USERMYSQL;
				ps=PSMYSQL;
			}
			cnx = DriverManager.getConnection(url, user, ps);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error, no se ha podido conectar con la BD " + tipo);
			e.printStackTrace();
		}
	}

	public boolean crearAlumno(Alumno alumno) {
		boolean resultado = false;
		String sql = "INSERT INTO alumnos (nombre, apellido, edad, correo) VALUES (?, ?, ?, ?)";
		try (PreparedStatement statement = cnx.prepareStatement(sql)) {
			statement.setString(1, alumno.getNombre());
			statement.setString(2, alumno.getApellido());
			statement.setInt(3, alumno.getEdad());
			statement.setString(4, alumno.getCorreo());
			if(statement.executeUpdate()==1) resultado=true;
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultado;
	}

	public Alumno obtenerAlumnoPorId(int id) {
		Alumno alumno = null;
		String sql = "SELECT * FROM alumnos WHERE id = ?";
		try (PreparedStatement statement = cnx.prepareStatement(sql)) {
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				alumno = new Alumno(resultSet.getInt("id"), resultSet.getString("nombre"),
						resultSet.getString("apellido"), resultSet.getInt("edad"), resultSet.getString("correo"));
			}
			resultSet.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return alumno;
	}
	public ArrayList<Alumno> obtenerTodosLosAlumnos() {
		ArrayList<Alumno> alumnos = new ArrayList<>();
		try (Statement statement = cnx.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM alumnos");
			while (resultSet.next()) {
				Alumno alumno = new Alumno(resultSet.getInt("id"), resultSet.getString("nombre"),
						resultSet.getString("apellido"), resultSet.getInt("edad"), resultSet.getString("correo"));
				alumnos.add(alumno);
			}
			resultSet.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return alumnos;
	}

	public boolean actualizarAlumno(Alumno alumno) {
		boolean resultado = false;
		String sql = "UPDATE alumnos SET nombre = ?, apellido = ?, edad = ?, correo = ? WHERE id = ?";
		try (PreparedStatement statement = cnx.prepareStatement(sql)) {
			statement.setString(1, alumno.getNombre());
			statement.setString(2, alumno.getApellido());
			statement.setInt(3, alumno.getEdad());
			statement.setString(4, alumno.getCorreo());
			statement.setInt(5, alumno.getId());
			if(statement.executeUpdate()==1) resultado = true;
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultado;
	}

	public boolean eliminarAlumno(int id) {
		boolean resultado = false;
		String sql = "DELETE FROM alumnos WHERE id = ?";
		try (PreparedStatement statement = cnx.prepareStatement(sql)) {
			statement.setInt(1, id);
			if(statement.executeUpdate()==1) resultado = true;;
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return resultado;
	}

	public Connection getCnx() {
		return cnx;
	}

	public void setCnx(Connection cnx) {
		this.cnx = cnx;
	}
	
}
