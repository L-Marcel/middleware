package imd.ufrn.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import imd.ufrn.enums.HttpMethod;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@MethodMapping(HttpMethod.POST)
public @interface PostMapping {
  public String value() default "";
};
