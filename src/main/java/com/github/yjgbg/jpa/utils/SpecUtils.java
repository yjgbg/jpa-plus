package com.github.yjgbg.jpa.utils;

import javax.persistence.criteria.Path;
import java.util.Arrays;

public class SpecUtils {
	public static <A,B> Path<B> str2Path(Path<A> from,String path) {
	 	@SuppressWarnings("unchecked")
	 	final var x = (Path<B>) from;
	 	return Arrays.stream(path.split("\\."))
				 .reduce(x,Path::get,(a,b) -> null);
	 }
}
