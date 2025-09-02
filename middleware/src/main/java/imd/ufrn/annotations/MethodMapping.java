package imd.ufrn.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import imd.ufrn.enums.HttpMethod;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodMapping {
  HttpMethod value() default HttpMethod.GET;
};
