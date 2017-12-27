package de.sebikopp.dummyjaxrs.test.logging;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name="TestScope", category="Core", elementType="appender", printObject=true)
public final class TestScopeAppender extends AbstractAppender{

	private final ConcurrentMap<Long,List<String>> loggedContent = new ConcurrentHashMap<>();
	private final boolean globalAccessFlag;
	
	private final static ConcurrentMap<String, TestScopeAppender> APPENDERS = new ConcurrentHashMap<>();
	
	private TestScopeAppender(String name, Filter filter, Layout<String> layout, boolean globalAccess) {
		super(name, filter, layout, true);
		this.globalAccessFlag = globalAccess;
		APPENDERS.put(name, this);
	}
	
	@Override
	public void append(LogEvent event) {
		try {
			loggedContent.compute(event.getThreadId(), (key, valOld) -> {
				List<String> contList = (valOld == null) ? new CopyOnWriteArrayList<>() : valOld;
				contList.add((String) getLayout().toSerializable(event));
				return contList;
			});
		} catch (Exception e) {
			if (!ignoreExceptions()) {
				throw new AppenderLoggingException(e);
			}
		}
	}
	
	public List<String> getEvents() {
		if (globalAccessFlag) {
			return Collections.unmodifiableList(loggedContent.values().stream().flatMap(List::stream).collect(Collectors.toList()));
		} else {			
			return Collections.unmodifiableList(
					loggedContent.getOrDefault(Thread.currentThread().getId(), Collections.emptyList()).stream().collect(Collectors.toList()));
		}
	}
	
	public void clear() {
		if (globalAccessFlag) {
			loggedContent.clear();
		} else {			
			loggedContent.remove(Thread.currentThread().getId());
		}
	}

	public static Collection<TestScopeAppender> getTestScopeAppenders() {
		return Collections.unmodifiableCollection(APPENDERS.values());
	}
	
	public static Optional<TestScopeAppender> byName(String name) {
		return Optional.ofNullable(APPENDERS.get(name));
	}
	
	@PluginFactory
	public static TestScopeAppender newAppender(@PluginAttribute("name") String name,
			@PluginElement("Filter") Filter filter,
			@PluginAttribute(defaultBoolean=false, value="globalAccess") boolean globalAccess) {
		if (name == null) {
			LOGGER.error("Name for TestScopeAppender is missing");
		}
		Layout<String> layout = PatternLayout.newBuilder()
				.withCharset(StandardCharsets.UTF_8)
				.withPattern("%m%n")
				.build();
		return new TestScopeAppender(name, filter, layout, globalAccess);
	}
	
}
