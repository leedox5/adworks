package kr.leedox.util;

import org.apache.commons.lang3.math.NumberUtils;

public class ValidateParameter {

	public static boolean validateId(String param) {
		return !(null == param || "".equals(param) || !NumberUtils.isCreatable(param));
	}

}
