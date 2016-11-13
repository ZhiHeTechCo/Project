package zh.co.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * <p>[概要] CollectionUtils</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class CollectionUtils {

    /**
     * Whether the specified map contains a key ignoring the case of the key.
     * 
     * @param map
     *            a map
     * @param key
     *            a key
     * @return true if the key specified exists, or false.
     */
    public static boolean containsKeyIgnoreCase(Map<String, Object> map,
            String key) {
        if (map == null) {
            return false;
        }
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            if (iter.next().equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get from the specified map with the specified key ignoring the case of
     * the key.
     * 
     * @param map
     *            a map
     * @param key
     *            a key
     * @return value that corresponds to the specified key, or null if the key
     *         does not exist in the map.
     */
    public static Object getIgnoreCase(Map<String, Object> map, String key) {
        if (map == null) {
            return null;
        }
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String actualKey = iter.next();
            if (actualKey.equalsIgnoreCase(key)) {
                return map.get(actualKey);
            }
        }
        return null;
    }

    public static boolean containsKeyWithSuffix(Map<String, ?> map,
            String keySuffix) {
        if (map == null) {
            return false;
        }
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            if (iter.next().endsWith(keySuffix)) {
                return true;
            }
        }
        return false;
    }

    public static Object getWithSuffix(Map<String, ?> map, String keySuffix) {
        if (map == null) {
            return null;
        }
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String actualKey = iter.next();
            if (actualKey.endsWith(keySuffix)) {
                return map.get(actualKey);
            }
        }
        return null;
    }

    /**
     * remove entries with the same key prefix
     * 
     * @param map
     *            map
     * @param keyPrefix
     *            key prefix
     */
    public static void removeWithKeyPrefix(Map<String, ?> map, String keyPrefix) {
        Set<String> toBeRemoved = new HashSet<String>();
        for (String key : map.keySet()) {
            if (key.startsWith(keyPrefix)) {
                toBeRemoved.add(key);
            }
        }
        map.keySet().removeAll(toBeRemoved);
    }
    
	public static Object getWithPrefix(Map<String, Object> map, String prefix) {
		for (String key : map.keySet()) {
			if (key.startsWith(prefix)) {
				return map.get(key);
			}
		}
		return null;
	}
	
	public static boolean containsKeyWithPrefix(Map<String, Object> map,
			String prefix) {
		return getWithPrefix(map, prefix) != null;
	}

	public static Map<String, String> toStringMap(Map<String, Object> map) {

	    Map<String, String> strMap = new HashMap<String, String>();

	    for (String key : map.keySet()) {
	        Object value = map.get(key);
	        strMap.put(key, value == null ? null : value.toString());
	    }

	    return strMap;
	}

	public static Map<String, String> getWithRegex(Map<String, String> map, String regex) {

	    Map<String, String> matchedMap = new HashMap<String, String>();
	    Pattern pattern = Pattern.compile(regex);

	    for (String key : map.keySet()) {
	        if (pattern.matcher(key).matches()) {
	            matchedMap.put(key, map.get(key));
	        }
	    }

	    return matchedMap;
	}

	public static List<String> getSortedKeyWithRegex(Map<String, String> map, String regex) {
        List<String> keyList = new ArrayList<String>(getWithRegex(map, regex).keySet());
        Collections.sort(keyList);
	    return keyList;
	}
}
