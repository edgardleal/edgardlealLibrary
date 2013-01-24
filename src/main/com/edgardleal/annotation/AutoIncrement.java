package com.edgardleal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @deprecated use javax.persistence.GenerationType
 * @author Edgard Leal
 * @since 24/01/2013
 * 
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoIncrement {
	public AutoIncrementType TYPE() default AutoIncrementType.AUTO_ID;
}
