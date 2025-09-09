package imd.ufrn.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import imd.ufrn.interceptors.BeforeInterceptor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InterceptBefore {
  public Class<BeforeInterceptor>[] value();
};
