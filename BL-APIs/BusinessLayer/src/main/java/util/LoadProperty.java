package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by Satya Prakash Solanki
 */
public class LoadProperty {
        private final String PROPS_PATH = System.getProperty("user.dir") + "/Config.properties";
        private static Properties loadProps = null;
        private boolean isLoaded = false;

        public Object getProperty(String propertyName, String defaultValue) {
            if (!isLoaded)
                load();
            String result = System.getProperty(propertyName);
            if (result == null) {
                result = defaultValue;
                if (result != null)
                    System.setProperty(propertyName, result);
            }
            return result;
        }

        public Object getProperty(String propertyName) {
            return getProperty(propertyName, null);
        }


        protected boolean load() {
            return load(PROPS_PATH);
        }

        public boolean load(String filePath) {

            if (loadProps != null) {
                Iterator<?> names = loadProps.keySet().iterator();
                while (names.hasNext()) {
                    String name = (String) names.next();
                    System.clearProperty(name);
                }
            }

            loadProps = new java.util.Properties();
            try {
                /*InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath);*/
                InputStream inputStream = new FileInputStream(filePath);
                loadProps.load(inputStream);

                Enumeration<?> names = loadProps.propertyNames();
                while (names.hasMoreElements()) {
                    String key = (String) names.nextElement();
                    System.setProperty(key, loadProps.getProperty(key));
                }
            } catch (IOException ioe) {
                System.out.println("IO Error while loading properties");
            }

            isLoaded = true;
            return isLoaded;
        }

        public boolean isLoaded() {
            return isLoaded;
        }
}
