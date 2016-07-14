package com.github.ttwd80.loggly.log4j2;

import java.io.Serializable;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import loggly.log4j.threading.LogglyAppenderPool;
import loggly.log4j.threading.LogglyThreadPool;

@Plugin(name = "LogglyAppender", category = "Core", elementType = "appender", printObject = true)
public class Log4jLogglyAppender extends AbstractAppender {

	String endpointUrl;
	LogglyThreadPool logglyThreadPool = new LogglyThreadPool(50);;

	public Log4jLogglyAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout,
			final String endpointUrl, final boolean ignoreExceptions) {
		super(name, filter, layout, ignoreExceptions);
		this.endpointUrl = endpointUrl;
	}

	@Override
	public void append(final LogEvent event) {
		final String msg = new String(getLayout().toByteArray(event));
		final String contentType = getLayout().getContentType();
		final LogglyAppenderPool task = new LogglyAppenderPool(msg, endpointUrl, null, contentType);
		logglyThreadPool.addTask(task);

	}

	public void setEndpointUrl(final String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	@PluginFactory
	public static Log4jLogglyAppender createAppender(@PluginAttribute("name") final String name,
			@PluginElement("Layout") Layout<? extends Serializable> layout,
			@PluginElement("Filter") final Filter filter, @PluginAttribute("endpointUrl") final String endpointUrl) {
		if (name == null) {
			LOGGER.error("No name provided for LogglyAppender");
			return null;
		}
		if (layout == null) {
			layout = PatternLayout.createDefaultLayout();
		}
		return new Log4jLogglyAppender(name, filter, layout, endpointUrl, true);
	}
}
