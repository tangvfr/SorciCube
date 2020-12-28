package fr.tangv.sorcicubecore.requests;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface RequestAnnotation {

	RequestType type();
	String name() default "";
	
}
