package com.edgardleal.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignKey {

	String Table();

	String DescriptionField();

	String KeyField();
}
