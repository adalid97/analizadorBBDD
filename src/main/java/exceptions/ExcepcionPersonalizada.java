package exceptions;

public class ExcepcionPersonalizada extends RuntimeException {

	private static final long serialVersionUID = -1710180715597574718L;

	public ExcepcionPersonalizada(String mensaje) {
		super(mensaje);
	}

}
