package com.edgardleal.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Accessibility {
	boolean Visible() default true;

	boolean InUpdate() default true;

	boolean InInsert() default true;
}
