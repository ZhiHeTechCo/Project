package zh.co.common.prop;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;


/**
 * <p>[概要] This is intended to be used with <code>CmnLogger</code>.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class CmnPropertiesResourceBundle extends ResourceBundle {

    public CmnPropertiesResourceBundle(InputStream stream) throws IOException {
        Properties properties = new Properties();
        PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
        propertiesPersister.load(properties, stream);
        lookup = new HashMap(properties);
    }

    public CmnPropertiesResourceBundle(Reader reader) throws IOException {
        Properties properties = new Properties();
        PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
        propertiesPersister.load(properties, reader);
        lookup = new HashMap(properties);
    }

    public Object handleGetObject(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return lookup.get(key);
    }

    public Enumeration<String> getKeys() {
        ResourceBundle parent = this.parent;
        return new CmnResourceBundleEnumeration(lookup.keySet(),
                (parent != null) ? parent.getKeys() : null);
    }

    protected Set<String> handleKeySet() {
        return lookup.keySet();
    }

    // ==================privates====================

    private Map<String, Object> lookup;
}
