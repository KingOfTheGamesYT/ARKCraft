package com.arkcraft.lib;

import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;

public class Logger {
	private org.apache.logging.log4j.Logger logger;

	private boolean showClient;
	private boolean showServer;

	public Logger(org.apache.logging.log4j.Logger logger) {
		this.logger = logger;
		showClient = true;
		showServer = true;
	}

	public void showClient() {
		showClient = true;
	}

	public void hideClient() {
		showClient = false;
	}

	public void showServer() {
		showServer = true;
	}

	public void hideServer() {
		showServer = false;
	}

	private boolean clientCheck() {
		return showClient && FMLCommonHandler.instance().getSide().isClient();
	}

	private boolean serverCheck() {
		return showServer && FMLCommonHandler.instance().getSide().isServer();
	}

	public void catching(Level level, Throwable t) {
		if (clientCheck()) logger.catching(level, t);
		else if (serverCheck()) logger.catching(level, t);
	}

	public void catching(Throwable t) {
		if (clientCheck()) logger.catching(t);
		else if (serverCheck()) logger.catching(t);
	}

	public void debug(Marker marker, Message msg) {
		if (clientCheck()) logger.debug(marker, msg);
		else if (serverCheck()) logger.debug(marker, msg);
	}

	public void debug(Marker marker, Message msg, Throwable t) {
		if (clientCheck()) logger.debug(marker, msg, t);
		else if (serverCheck()) logger.debug(marker, msg, t);
	}

	public void debug(Marker marker, Object message) {
		if (clientCheck()) logger.debug(marker, message);
		else if (serverCheck()) logger.debug(marker, message);
	}

	public void debug(Marker marker, Object message, Throwable t) {
		if (clientCheck()) logger.debug(marker, message, t);
		else if (serverCheck()) logger.debug(marker, message, t);
	}

	public void debug(Marker marker, String message) {
		if (clientCheck()) logger.debug(marker, message);
		else if (serverCheck()) logger.debug(marker, message);
	}

	public void debug(Marker marker, String message, Object... params) {
		if (clientCheck()) logger.debug(marker, message, params);
		else if (serverCheck()) logger.debug(marker, message, params);
	}

	public void debug(Marker marker, String message, Throwable t) {
		if (clientCheck()) logger.debug(marker, message, t);
		else if (serverCheck()) logger.debug(marker, message, t);
	}

	public void debug(Message msg) {
		if (clientCheck()) logger.debug(msg);
		else if (serverCheck()) logger.debug(msg);
	}

	public void debug(Message msg, Throwable t) {
		if (clientCheck()) logger.debug(msg, t);
		else if (serverCheck()) logger.debug(msg, t);
	}

	public void debug(Object message) {
		if (clientCheck()) logger.debug(message);
		else if (serverCheck()) logger.debug(message);
	}

	public void debug(Object message, Throwable t) {
		if (clientCheck()) logger.debug(message, t);
		else if (serverCheck()) logger.debug(message, t);
	}

	public void debug(String message) {
		if (clientCheck()) logger.debug(message);
		else if (serverCheck()) logger.debug(message);
	}

	public void debug(String message, Object... params) {
		if (clientCheck()) logger.debug(message, params);
		else if (serverCheck()) logger.debug(message, params);
	}

	public void debug(String message, Throwable t) {
		if (clientCheck()) logger.debug(message, t);
		else if (serverCheck()) logger.debug(message, t);
	}

	
	public void entry() {
		if (clientCheck()) logger.entry();
		else if (serverCheck()) logger.entry();
	}

	
	public void entry(Object... params) {
		if (clientCheck()) logger.entry(params);
		else if (serverCheck()) logger.entry(params);
	}

	
	public void error(Marker marker, Message msg) {
		if (clientCheck()) logger.error(marker, msg);
		else if (serverCheck()) logger.error(marker, msg);
	}

	
	public void error(Marker marker, Message msg, Throwable t) {
		if (clientCheck()) logger.error(marker, msg, t);
		else if (serverCheck()) logger.error(marker, msg, t);
	}

	
	public void error(Marker marker, Object message) {
		if (clientCheck()) logger.error(marker, message);
		else if (serverCheck()) logger.error(marker, message);
	}

	
	public void error(Marker marker, Object message, Throwable t) {
		if (clientCheck()) logger.error(marker, message, t);
		else if (serverCheck()) logger.error(marker, message, t);
	}

	
	public void error(Marker marker, String message) {
		if (clientCheck()) logger.error(marker, message);
		else if (serverCheck()) logger.error(marker, message);
	}

	
	public void error(Marker marker, String message, Object... params) {
		if (clientCheck()) logger.error(marker, message, params);
		else if (serverCheck()) logger.error(marker, message, params);
	}

	
	public void error(Marker marker, String message, Throwable t) {
		if (clientCheck()) logger.error(marker, message, t);
		else if (serverCheck()) logger.error(marker, message, t);
	}

	
	public void error(Message msg) {
		if (clientCheck()) logger.error(msg);
		else if (serverCheck()) logger.error(msg);
	}

	
	public void error(Message msg, Throwable t) {
		if (clientCheck()) logger.error(msg, t);
		else if (serverCheck()) logger.error(msg, t);
	}

	
	public void error(Object message) {
		if (clientCheck()) logger.error(message);
		else if (serverCheck()) logger.error(message);
	}

	
	public void error(Object message, Throwable t) {
		if (clientCheck()) logger.error(message, t);
		else if (serverCheck()) logger.error(message, t);
	}

	
	public void error(String message) {
		if (clientCheck()) logger.error(message);
		else if (serverCheck()) logger.error(message);
	}

	
	public void error(String message, Object... params) {
		if (clientCheck()) logger.error(message, params);
		else if (serverCheck()) logger.error(message, params);
	}

	
	public void error(String message, Throwable t) {
		if (clientCheck()) logger.error(message, t);
		else if (serverCheck()) logger.error(message, t);
	}

	
	public void exit() {
		logger.exit();
	}

	
	public <R> R exit(R result) {
		return logger.exit(result);
	}

	
	public void fatal(Marker marker, Message msg) {
		if (clientCheck()) logger.fatal(marker, msg);
		else if (serverCheck()) logger.fatal(marker, msg);
	}

	
	public void fatal(Marker marker, Message msg, Throwable t) {
		if (clientCheck()) logger.fatal(marker, msg, t);
		else if (serverCheck()) logger.fatal(marker, msg, t);
	}

	
	public void fatal(Marker marker, Object message) {
		if (clientCheck()) logger.fatal(marker, message);
		else if (serverCheck()) logger.fatal(marker, message);
	}

	
	public void fatal(Marker marker, Object message, Throwable t) {
		if (clientCheck()) logger.fatal(marker, message, t);
		else if (serverCheck()) logger.fatal(marker, message, t);
	}

	
	public void fatal(Marker marker, String message) {
		if (clientCheck()) logger.fatal(marker, message);
		else if (serverCheck()) logger.fatal(marker, message);
	}

	
	public void fatal(Marker marker, String message, Object... params) {
		if (clientCheck()) logger.fatal(marker, message, params);
		else if (serverCheck()) logger.fatal(marker, message, params);
	}

	
	public void fatal(Marker marker, String message, Throwable t) {
		if (clientCheck()) logger.fatal(marker, message, t);
		else if (serverCheck()) logger.fatal(marker, message, t);
	}

	
	public void fatal(Message msg) {
		if (clientCheck()) logger.fatal(msg);
		else if (serverCheck()) logger.fatal(msg);
	}

	
	public void fatal(Message msg, Throwable t) {
		if (clientCheck()) logger.fatal(msg, t);
		else if (serverCheck()) logger.fatal(msg, t);
	}

	
	public void fatal(Object message) {
		if (clientCheck()) logger.fatal(message);
		else if (serverCheck()) logger.fatal(message);
	}

	
	public void fatal(Object message, Throwable t) {
		if (clientCheck()) logger.fatal(message, t);
		else if (serverCheck()) logger.fatal(message, t);
	}

	
	public void fatal(String message) {
		if (clientCheck()) logger.fatal(message);
		else if (serverCheck()) logger.fatal(message);
	}

	
	public void fatal(String message, Object... params) {
		if (clientCheck()) logger.fatal(message, params);
		else if (serverCheck()) logger.fatal(message, params);
	}

	
	public void fatal(String message, Throwable t) {
		if (clientCheck()) logger.fatal(message, t);
		else if (serverCheck()) logger.fatal(message, t);
	}

	
	public MessageFactory getMessageFactory() {
		return logger.getMessageFactory();
	}

	
	public String getName() {
		return logger.getName();
	}

	
	public void info(Marker marker, Message msg) {
		if (clientCheck()) logger.info(marker, msg);
		else if (serverCheck()) logger.info(marker, msg);
	}

	
	public void info(Marker marker, Message msg, Throwable t) {
		if (clientCheck()) logger.info(marker, msg, t);
		else if (serverCheck()) logger.info(marker, msg, t);
	}

	
	public void info(Marker marker, Object message) {
		if (clientCheck()) logger.info(marker, message);
		else if (serverCheck()) logger.info(marker, message);
	}

	
	public void info(Marker marker, Object message, Throwable t) {
		if (clientCheck()) logger.info(marker, message, t);
		else if (serverCheck()) logger.info(marker, message, t);
	}

	
	public void info(Marker marker, String message) {
		if (clientCheck()) logger.info(marker, message);
		else if (serverCheck()) logger.info(marker, message);
	}

	
	public void info(Marker marker, String message, Object... params) {
		if (clientCheck()) logger.info(marker, message, params);
		else if (serverCheck()) logger.info(marker, message, params);
	}

	
	public void info(Marker marker, String message, Throwable t) {
		if (clientCheck()) logger.info(marker, message, t);
		else if (serverCheck()) logger.info(marker, message, t);
	}

	
	public void info(Message msg) {
		if (clientCheck()) logger.info(msg);
		else if (serverCheck()) logger.info(msg);
	}

	
	public void info(Message msg, Throwable t) {
		if (clientCheck()) logger.info(msg, t);
		else if (serverCheck()) logger.info(msg, t);
	}

	
	public void info(Object message) {
		if (clientCheck()) logger.info(message);
		else if (serverCheck()) logger.info(message);
	}

	
	public void info(Object message, Throwable t) {
		if (clientCheck()) logger.info(message, t);
		else if (serverCheck()) logger.info(message, t);
	}

	
	public void info(String message) {
		if (clientCheck()) logger.info(message);
		else if (serverCheck()) logger.info(message);
	}

	
	public void info(String message, Object... params) {
		if (clientCheck()) logger.info(message, params);
		else if (serverCheck()) logger.info(message, params);
	}

	
	public void info(String message, Throwable t) {
		if (clientCheck()) logger.info(message, t);
		else if (serverCheck()) logger.info(message, t);
	}

	
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	
	public boolean isDebugEnabled(Marker marker) {
		return logger.isDebugEnabled(marker);
	}

	
	public boolean isEnabled(Level level) {
		return logger.isEnabled(level);
	}

	
	public boolean isEnabled(Level level, Marker marker) {
		return logger.isEnabled(level, marker);
	}

	
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	
	public boolean isErrorEnabled(Marker marker) {
		return logger.isErrorEnabled(marker);
	}

	
	public boolean isFatalEnabled() {
		return logger.isFatalEnabled();
	}

	
	public boolean isFatalEnabled(Marker marker) {
		return logger.isFatalEnabled(marker);
	}

	
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	
	public boolean isInfoEnabled(Marker marker) {
		return logger.isInfoEnabled(marker);
	}

	
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	
	public boolean isTraceEnabled(Marker marker) {
		return logger.isTraceEnabled(marker);
	}

	
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	
	public boolean isWarnEnabled(Marker marker) {
		return logger.isWarnEnabled(marker);
	}

	
	public void log(Level level, Marker marker, Message msg) {
		if (clientCheck()) logger.log(level, marker, msg);
		else if (serverCheck()) logger.log(level, marker, msg);
	}

	
	public void log(Level level, Marker marker, Message msg, Throwable t) {
		if (clientCheck()) logger.log(level, marker, msg, t);
		else if (serverCheck()) logger.log(level, marker, msg, t);
	}

	
	public void log(Level level, Marker marker, Object message) {
		if (clientCheck()) logger.log(level, marker, message);
		else if (serverCheck()) logger.log(level, marker, message);
	}

	
	public void log(Level level, Marker marker, Object message, Throwable t) {
		if (clientCheck()) logger.log(level, marker, message, t);
		else if (serverCheck()) logger.log(level, marker, message, t);
	}

	
	public void log(Level level, Marker marker, String message) {
		if (clientCheck()) logger.log(level, marker, message);
		else if (serverCheck()) logger.log(level, marker, message);
	}

	
	public void log(Level level, Marker marker, String message, Object... params) {
		if (clientCheck()) logger.log(level, marker, message, params);
		else if (serverCheck()) logger.log(level, marker, message, params);
	}

	
	public void log(Level level, Marker marker, String message, Throwable t) {
		if (clientCheck()) logger.log(level, marker, message, t);
		else if (serverCheck()) logger.log(level, marker, message, t);
	}

	
	public void log(Level level, Message msg) {
		if (clientCheck()) logger.log(level, msg);
		else if (serverCheck()) logger.log(level, msg);
	}

	
	public void log(Level level, Message msg, Throwable t) {
		if (clientCheck()) logger.log(level, msg, t);
		else if (serverCheck()) logger.log(level, msg, t);
	}

	
	public void log(Level level, Object message) {
		if (clientCheck()) logger.log(level, message);
		else if (serverCheck()) logger.log(level, message);
	}

	
	public void log(Level level, Object message, Throwable t) {
		if (clientCheck()) logger.log(level, message, t);
		else if (serverCheck()) logger.log(level, message, t);
	}

	
	public void log(Level level, String message) {
		if (clientCheck()) logger.log(level, message);
		else if (serverCheck()) logger.log(level, message);
	}

	
	public void log(Level level, String message, Object... params) {
		if (clientCheck()) logger.log(level, message, params);
		else if (serverCheck()) logger.log(level, message, params);
	}

	
	public void log(Level level, String message, Throwable t) {
		if (clientCheck()) logger.log(level, message, t);
		else if (serverCheck()) logger.log(level, message, t);
	}

	
	public void printf(Level level, Marker marker, String format, Object... params) {
		if (clientCheck()) logger.printf(level, marker, format, params);
		else if (serverCheck()) logger.printf(level, marker, format, params);
	}

	
	public void printf(Level level, String format, Object... params) {
		if (clientCheck()) logger.printf(level, format, params);
		else if (serverCheck()) logger.printf(level, format, params);
	}

	
	public <T extends Throwable> T throwing(Level level, T t) {
		return logger.throwing(level, t);
	}

	
	public <T extends Throwable> T throwing(T t) {
		return logger.throwing(t);
	}

	
	public void trace(Marker marker, Message msg) {
		if (clientCheck()) logger.trace(marker, msg);
		else if (serverCheck()) logger.trace(marker, msg);
	}

	
	public void trace(Marker marker, Message msg, Throwable t) {
		if (clientCheck()) logger.trace(marker, msg, t);
		else if (serverCheck()) logger.trace(marker, msg, t);
	}

	
	public void trace(Marker marker, Object message) {
		if (clientCheck()) logger.trace(marker, message);
		else if (serverCheck()) logger.trace(marker, message);
	}

	
	public void trace(Marker marker, Object message, Throwable t) {
		if (clientCheck()) logger.trace(marker, message, t);
		else if (serverCheck()) logger.trace(marker, message, t);
	}

	
	public void trace(Marker marker, String message) {
		if (clientCheck()) logger.trace(marker, message);
		else if (serverCheck()) logger.trace(marker, message);
	}

	
	public void trace(Marker marker, String message, Object... params) {
		if (clientCheck()) logger.trace(marker, message, params);
		else if (serverCheck()) logger.trace(marker, message, params);
	}

	
	public void trace(Marker marker, String message, Throwable t) {
		if (clientCheck()) logger.trace(marker, message, t);
		else if (serverCheck()) logger.trace(marker, message, t);
	}

	
	public void trace(Message msg) {
		if (clientCheck()) logger.trace(msg);
		else if (serverCheck()) logger.trace(msg);
	}

	
	public void trace(Message msg, Throwable t) {
		if (clientCheck()) logger.trace(msg, t);
		else if (serverCheck()) logger.trace(msg, t);
	}

	
	public void trace(Object message) {
		if (clientCheck()) logger.trace(message);
		else if (serverCheck()) logger.trace(message);
	}

	
	public void trace(Object message, Throwable t) {
		if (clientCheck()) logger.trace(message, t);
		else if (serverCheck()) logger.trace(message, t);
	}

	
	public void trace(String message) {
		if (clientCheck()) logger.trace(message);
		else if (serverCheck()) logger.trace(message);
	}

	
	public void trace(String message, Object... params) {
		if (clientCheck()) logger.trace(message, params);
		else if (serverCheck()) logger.trace(message, params);
	}

	
	public void trace(String message, Throwable t) {
		if (clientCheck()) logger.trace(message, t);
		else if (serverCheck()) logger.trace(message, t);
	}

	
	public void warn(Marker marker, Message msg) {
		if (clientCheck()) logger.warn(marker, msg);
		else if (serverCheck()) logger.warn(marker, msg);
	}

	
	public void warn(Marker marker, Message msg, Throwable t) {
		if (clientCheck()) logger.warn(marker, msg, t);
		else if (serverCheck()) logger.warn(marker, msg, t);
	}

	
	public void warn(Marker marker, Object message) {
		if (clientCheck()) logger.warn(marker, message);
		else if (serverCheck()) logger.warn(marker, message);
	}

	
	public void warn(Marker marker, Object message, Throwable t) {
		if (clientCheck()) logger.warn(marker, message, t);
		else if (serverCheck()) logger.warn(marker, message, t);
	}

	
	public void warn(Marker marker, String message) {
		if (clientCheck()) logger.warn(marker, message);
		else if (serverCheck()) logger.warn(marker, message);
	}

	
	public void warn(Marker marker, String message, Object... params) {
		if (clientCheck()) logger.warn(marker, message, params);
		else if (serverCheck()) logger.warn(marker, message, params);
	}

	
	public void warn(Marker marker, String message, Throwable t) {
		if (clientCheck()) logger.warn(marker, message, t);
		else if (serverCheck()) logger.warn(marker, message, t);
	}

	
	public void warn(Message msg) {
		if (clientCheck()) logger.warn(msg);
		else if (serverCheck()) logger.warn(msg);
	}

	
	public void warn(Message msg, Throwable t) {
		if (clientCheck()) logger.warn(msg, t);
		else if (serverCheck()) logger.warn(msg, t);
	}

	
	public void warn(Object message) {
		if (clientCheck()) logger.warn(message);
		else if (serverCheck()) logger.warn(message);
	}

	
	public void warn(Object message, Throwable t) {
		if (clientCheck()) logger.warn(message, t);
		else if (serverCheck()) logger.warn(message, t);
	}

	
	public void warn(String message) {
		if (clientCheck()) logger.warn(message);
		else if (serverCheck()) logger.warn(message);
	}

	
	public void warn(String message, Object... params) {
		if (clientCheck()) logger.warn(message, params);
		else if (serverCheck()) logger.warn(message, params);
	}

	
	public void warn(String message, Throwable t) {
		if (clientCheck()) logger.warn(message, t);
		else if (serverCheck()) logger.warn(message, t);
	}
}
