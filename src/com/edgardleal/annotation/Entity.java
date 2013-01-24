package com.edgardleal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.edgardleal.util.Str;

/**
 * 
 * @deprecated use javax.persistence.Entity
 * @author Edgard Leal
 * @since 21/01/2013
 * 
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
	public String name() default Str.EMPTY;
}
