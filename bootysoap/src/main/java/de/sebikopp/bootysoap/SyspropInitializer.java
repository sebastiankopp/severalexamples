package de.sebikopp.bootysoap;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

class SyspropInitializer {
	
	static void extendSysprops() {
		try {
			Properties additionalProps = new Properties();
			final InputStream propStream = SyspropInitializer.class.getClassLoader().getResourceAsStream("default-sysprops.properties");
			additionalProps.load(propStream);
			Properties sysprops = System.getProperties();
			for (Object key: additionalProps.keySet()) {
				if (!sysprops.containsKey(key) && key instanceof CharSequence) {
					System.setProperty(key.toString(), Objects.toString(additionalProps.get(key)));
				}
			}
		} catch (Exception e) {
			System.err.println("Could not load properties due to an exception! This might lead to unexpected behaviour.");
			e.printStackTrace();
		}
	}

}
