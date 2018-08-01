//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
public @interface RestrictTo {
    Scope[] value();

    public static enum Scope {
        LIBRARY,
        LIBRARY_GROUP,
        /** @deprecated */
        @Deprecated
        GROUP_ID,
        TESTS,
        SUBCLASSES;

        private Scope() {
        }
    }
}
