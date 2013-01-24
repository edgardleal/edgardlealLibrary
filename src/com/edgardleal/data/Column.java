package com.edgardleal.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.edgardleal.util.Str;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String name() default Str.EMPTY;
}
