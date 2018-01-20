package de.sebikopp.dummyjaxrs;

import java.util.regex.Pattern;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class RegexMatchers {
	
	public static <T extends CharSequence> Matcher<T> matchesPattern(String pattern) {
		return new RegexMatcher<T>(pattern, true);
	}
	
	public static <T extends CharSequence> Matcher<T> matchesPatternIgnoreCase(String pattern) {
		return new RegexMatcher<T>(pattern, false);
	}
	
	public static <T extends CharSequence> Matcher<T> isUuid() {
		return matchesPatternIgnoreCase("[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}");
	}
	
	static class RegexMatcher<T extends CharSequence> extends BaseMatcher<T> {

		private final Pattern regex;
		private final boolean caseSensitive;
		
		public RegexMatcher(String regex, boolean caseSensitive) {
			this.regex = Pattern.compile(regex, caseSensitive? 0 : Pattern.CASE_INSENSITIVE);
			this.caseSensitive = caseSensitive;
		}
		
		@Override
		public boolean matches(Object item) {
			return (item instanceof CharSequence) && regex.matcher((CharSequence) item).matches(); 
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("The given CharSequence should match the regex ")
					.appendValue(regex.pattern())
					.appendText(caseSensitive ?" (case sensitive) " : " (case insensitive) ");
			
		}
		
	}

}
