package net.ninjacat.pop500.api;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created on 21/01/14.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Queued {
}
