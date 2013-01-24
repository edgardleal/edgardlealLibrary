package com.edgardleal.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * @deprecated use javax.persistence.OneToMan
 * @author Edgard Leal
 * @since 24/01/2013
 * 
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignKey {

	String Table();

	String DescriptionField();

	String KeyField();
}
