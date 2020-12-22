package fr.tangv.sorcicubeapi.requests;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import javax.annotation.Nullable;

@Target(ElementType.METHOD)
public @interface RequestAnnotation {

	RequestType type();
	@Nullable String name();
	
}
