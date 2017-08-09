package com.ss.rlib.plugin.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * The annotation to describe a plugin.
 *
 * @author JavaSaBr
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
public @interface PluginDescription {

    /**
     * The plugin id.
     */
    @NotNull
    String id() default "unknown";

    /**
     * Dependencies of this plugin.
     */
    @NotNull
    String[] dependences() default {};

    /**
     * The plugin version.
     */
    @NotNull
    String version() default "0.1.0";

    /**
     * The min version of application.
     */
    @NotNull
    String minAppVersion() default "0.0.0";

    /**
     * The plugin name.
     */
    @NotNull
    String name() default "no name";

    /**
     * The plugin description.
     */
    @NotNull
    String description() default "no description";
}