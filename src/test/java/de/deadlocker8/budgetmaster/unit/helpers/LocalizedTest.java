package de.deadlocker8.budgetmaster.unit.helpers;

import org.springframework.test.context.TestExecutionListeners;

import java.lang.annotation.*;

import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TestExecutionListeners(
		listeners = LocalizationHelpers.class,
		mergeMode = MERGE_WITH_DEFAULTS)
public @interface LocalizedTest
{

}
