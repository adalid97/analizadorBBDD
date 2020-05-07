package pruebas;

public class Tab {
	String nombre;

	public Tab(String nombre) {
		super();
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Tab [nombre=" + nombre + "]";
	}

}
