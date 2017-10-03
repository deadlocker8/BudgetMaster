package de.deadlocker8.budgetmaster.logic.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark model classes that only exists in order to allow gson to import and export the database  
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface DatabaseImportExport {}