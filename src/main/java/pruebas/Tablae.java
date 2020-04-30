package pruebas;

public class Tablae {
	private String nombreTabla;
	private String campo;
	private String tipo;
	private String valor;

	public Tablae() {
		super();
	}

	public Tablae(String nombreTabla, String campo, String tipo, String valor) {
		super();
		this.nombreTabla = nombreTabla;
		this.campo = campo;
		this.tipo = tipo;
		this.valor = valor;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
