package com.dd.test.catpaw.annotations;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation used to note items which need work
 * 
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, 
	ElementType.PACKAGE, ElementType.PARAMETER, ElementType.TYPE})
public @interface NeedsAttention {
    String value() default "";
}