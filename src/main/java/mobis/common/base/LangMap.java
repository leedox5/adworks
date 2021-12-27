package mobis.common.base;

import java.text.*;
import java.util.*;



/**
 * 다국어 맵
 */
public class LangMap extends java.util.HashMap {

    private Map map = null;



	/**
	 * 기본 생성자
	 */
	public LangMap() {
		map = new HashMap();
	}



	/**
	 * 해당 Element가 가지고 있는 value를 String으로 리턴
	 */
	public String get(String key) {
		String value = null;

		if (map.containsKey(key)) {
			try {
				value = (String)map.get(key);
			} catch(ClassCastException e) {
				value = key;
			}
		} else {
			value = key;
		}

		return value;
	}



	/**
	 * key 값을 저장
	 */
	public void set(String key, String val) {
		key = key.trim();

		if (val == null) {
			val = "";
		}

		map.put(key, val);
	}



	/**
	 * KEY : VALUE 출력
	 */
	public void print() {
		StringBuffer sb = new StringBuffer();
		SortedMap new_map = new TreeMap();

		new_map.putAll(map);
		Set key_set = (Set)new_map.keySet();
		String[] keys_arr = (String[])key_set.toArray(new String[0]);

		for (int i=0; i < keys_arr.length; i++) {
			sb.append("# [LANG_MAP] ");
			sb.append(keys_arr[i]);
			sb.append(" : ");
			sb.append(get(keys_arr[i]));
			sb.append("\n");
		}

		System.out.println("\n" + sb.toString());
	}
}


