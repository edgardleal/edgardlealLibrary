package com.edgardleal.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Accessibility {
	public boolean Visible() default true;

	public boolean showInGrid() default true;

	public boolean showInForm() default true;

	public boolean InUpdate() default true;

	public boolean InInsert() default true;
}
