package rds;

import java.util.ArrayList;
import java.util.Scanner;

public class Principal {

	private static AlumnoDAO alumnoDAO = null;
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {

		System.out.println("Seleccione el tipo de base de datos:");
		System.out.println("1. MySQL");
		System.out.println("2. Oracle");
		int opcion = scanner.nextInt();
		scanner.nextLine();
		switch (opcion) {
		case 1:
			alumnoDAO = new AlumnoDAO("MYSQL");
			break;
		case 2:
			alumnoDAO = new AlumnoDAO("ORACLE");
			break;
		default:
			System.out.println("Opción inválida.");
			return;
		}
		if (alumnoDAO.getCnx() != null) {
			while (true) {
				System.out.println("Seleccione una operación:");
				System.out.println("1. Crear Alumno");
				System.out.println("2. Leer Alumno por ID");
				System.out.println("3. Leer Todos los Alumnos");
				System.out.println("4. Actualizar Alumno");
				System.out.println("5. Eliminar Alumno");
				System.out.println("6. Salir");

				int operacion = scanner.nextInt();
				scanner.nextLine();

				try {
					switch (operacion) {
					case 1:
						crearAlumno();
						break;
					case 2:
						leerAlumnoPorId();
						break;
					case 3:
						leerTodosLosAlumnos();
						break;
					case 4:
						actualizarAlumno();
						break;
					case 5:
						eliminarAlumno();
						break;
					case 6:
						System.out.println("Saliendo...");
						return;
					default:
						System.out.println("Operación inválida.");
					}
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		else {
			System.out.println("No se ha podido conectar con al BD");
		}
	}

	private static void crearAlumno() {
		System.out.println("Ingrese nombre:");
		String nombre = scanner.nextLine();
		System.out.println("Ingrese apellido:");
		String apellido = scanner.nextLine();
		System.out.println("Ingrese edad:");
		int edad = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Ingrese correo:");
		String correo = scanner.nextLine();

		Alumno alumno = new Alumno(nombre, apellido, edad, correo);
		if (alumnoDAO.crearAlumno(alumno))
			System.out.println("Alumno creado exitosamente.");
	}

	private static void leerAlumnoPorId() {
		System.out.println("Ingrese ID del alumno:");
		int id = scanner.nextInt();
		scanner.nextLine();
		Alumno alumno = alumnoDAO.obtenerAlumnoPorId(id);
		if (alumno != null) {
			System.out.println(alumno);
		} else {
			System.out.println("Alumno no encontrado.");
		}
	}

	private static void leerTodosLosAlumnos() throws Exception {
		ArrayList<Alumno> alumnos = alumnoDAO.obtenerTodosLosAlumnos();
		for (Alumno alumno : alumnos) {
			System.out.println(alumno);
		}
	}

	private static void actualizarAlumno() {
		System.out.println("Ingrese ID del alumno:");
		int id = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Ingrese nuevo nombre:");
		String nombre = scanner.nextLine();
		System.out.println("Ingrese nuevo apellido:");
		String apellido = scanner.nextLine();
		System.out.println("Ingrese nueva edad:");
		int edad = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Ingrese nuevo correo:");
		String correo = scanner.nextLine();

		Alumno alumno = new Alumno(id, nombre, apellido, edad, correo);
		if (alumnoDAO.actualizarAlumno(alumno))
			System.out.println("Alumno actualizado exitosamente.");
	}

	private static void eliminarAlumno() {
		System.out.println("Ingrese ID del alumno:");
		int id = scanner.nextInt();
		scanner.nextLine();
		if (alumnoDAO.eliminarAlumno(id))
			System.out.println("Alumno eliminado exitosamente.");
	}
}