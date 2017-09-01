package de.sebikopp.jaxb.mtom;

import java.util.regex.Pattern;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * A Hamcrest matcher which checks whether an exception or one of its underlying causes is type compatible with a given exception class
 * and whether its message matches a given regular expression  (no case sensitivity).
 * 
 * This class is applicable for any {@linkplain Throwable} instances which do not match the class {@linkplain Exception}.
 * @author sebi
 *
 * @param <T>
 */
public class CustomExceptionMatcher<T extends Exception> extends BaseMatcher<T>{
	private String errMsg = "";
	private final String regex;
	private final Class<T> excCls;
	private final Pattern pttrn;
	
	public static <U extends Exception> CustomExceptionMatcher<U> of(String regex, Class<U> excCls) {
		return new CustomExceptionMatcher<>(excCls, regex);
	}
	
	private CustomExceptionMatcher(final Class<T> exceptionClass, final String regex) {
		this.pttrn = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		this.excCls = exceptionClass;
		this.regex = regex;
	}
	
	@Override
	public boolean matches(Object arg0) {
		if (!(arg0 instanceof Exception)) {
			this.errMsg = "The given object is not an exception. Runtime class: " + arg0.getClass();
			return false;
		}
		final Exception e = (Exception) arg0;
		final String message = e.getMessage();
		if (excCls.isAssignableFrom(e.getClass()) && message != null
				&& pttrn.matcher(message).find()) {
			return true;
		} else {
			final Throwable cause = e.getCause();
			if (cause != null && cause instanceof Exception) {
				return matches(cause);
			} else {
				this.errMsg = "No exception with the specified criteria contained in stacktrace: Expected Exception class " + this.excCls.getCanonicalName()
					+ ", Exception regex: " + this.regex;
				return false;
			}
		}
	}

	@Override
	public void describeTo(Description arg0) {
		arg0.appendText(errMsg);
	}

}
