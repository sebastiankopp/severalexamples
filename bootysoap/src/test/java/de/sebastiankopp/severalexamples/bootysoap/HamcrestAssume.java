package de.sebastiankopp.severalexamples.bootysoap;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.opentest4j.TestAbortedException;

public class HamcrestAssume {

	public static <T> void assumeThat(T actual, Matcher<? super T> matcher) {
		assumeThat("", actual, matcher);
	}
	
	public static <T> void assumeThat(String reason, T actual, Matcher<? super T> matcher) {
		if (!matcher.matches(actual)) {
			Description description = new StringDescription();
			description.appendText(reason)
					.appendText("\nExpected: ")
					.appendDescriptionOf(matcher)
					.appendText("\n but: ");
			matcher.describeMismatch(actual, description);
			throw new TestAbortedException(description.toString());
		}
	}
}
