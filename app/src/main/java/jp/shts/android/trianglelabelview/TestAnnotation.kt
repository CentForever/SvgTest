package jp.shts.android.trianglelabelview

import java.lang.annotation.*
import java.lang.annotation.Retention
import java.lang.annotation.Target


@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE)
annotation class TestAnnotation()

@Target(ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
annotation class TestAnnotation2(vararg val value: String = [""])