package poc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Maps a destination field to one or more candidate source types.
 * <p>
 * If {@code containerField} is specified (non-empty), then for each candidate type,
 * the copier will first retrieve that field from the source candidate and then
 * look for the field named {@code fieldName} on that inner container.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldMapping {
    Class<?>[] types();
    String fieldName();
    String containerField() default "";
}
