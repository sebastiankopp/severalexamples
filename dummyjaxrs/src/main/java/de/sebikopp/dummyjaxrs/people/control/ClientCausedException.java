package de.sebikopp.dummyjaxrs.people.control;

public class ClientCausedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Type type;
	
	public static enum Type {
		NOT_FOUND,
		ALREADY_EXISTS,
		
		
		SOMETHING_DIFFERENT
	}

	public ClientCausedException() {
		super();
		this.type = Type.SOMETHING_DIFFERENT;
	}
	
	public ClientCausedException(String msg) {
		super(msg);
		this.type = Type.SOMETHING_DIFFERENT;
	}
	
	public ClientCausedException(String msg, Throwable cause) {
		super(msg, cause);
		this.type = Type.SOMETHING_DIFFERENT;
	}
	
	public ClientCausedException(Throwable cause) {
		super(cause);
		this.type = Type.SOMETHING_DIFFERENT;
	}
	
	public ClientCausedException(Type type) {
		super();
		this.type = type;
	}
	
	public ClientCausedException(String msg, Type type) {
		super(msg);
		this.type = type;
	}
	
	public ClientCausedException(String msg, Throwable cause, Type type) {
		super(msg, cause);
		this.type = type;
	}
	
	public ClientCausedException(Throwable cause, Type type) {
		super(cause);
		this.type = type;
	}
	
	public Type getType() {
		return this.type;
	}
}
