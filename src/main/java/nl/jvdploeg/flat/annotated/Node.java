package nl.jvdploeg.flat.annotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation describing Path to node. Including symbolic expressions.<br>
 * Example: @Node(path="Cars/{id}", value="value", children="lane")
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Node {

  /** Field names for child nodes. */
  String[] children();

  /**
   * Path to node. Including symbolic expressions. Example: path="collection/{id}"
   */
  String path();

  /** Field name for node value. */
  String value() default "";
}