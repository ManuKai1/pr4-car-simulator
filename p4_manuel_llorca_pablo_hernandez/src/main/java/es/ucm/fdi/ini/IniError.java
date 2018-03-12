package es.ucm.fdi.ini;

public class IniError extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IniError(String msg) {
		super(msg);
	}
}
