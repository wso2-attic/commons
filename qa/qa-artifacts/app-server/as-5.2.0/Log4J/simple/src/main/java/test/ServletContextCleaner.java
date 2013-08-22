package test;

import java.beans.Introspector;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;

public class ServletContextCleaner implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LogManager.shutdown();
		Introspector.flushCaches();
	}
}
