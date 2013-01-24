package com.edgardleal.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BooleanField {
	String ValueToTrue() default "true";

	String ValueToFalse() default "false";
}
