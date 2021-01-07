package jp.shts.android.trianglelabelview.reflection

import android.annotation.SuppressLint
import android.util.Log


class KotlinAnnotationReflectionExt<T, A : Annotation>(private val clazz: Class<T>, private val annotationClass: Class<A>) {
    @SuppressLint("LongLogTag")
    fun findPropertiesWithAnnotationInternal(){
        Log.e("findPropertiesWithAnnotationInternal:","annotationClass:${annotationClass} ")
        /*clazz.declaredMethods.forEach {
            Log.e("findPropertiesWithAnnotationInternal:","name:${it.name} ")
            Log.e("findPropertiesWithAnnotationInternal:","it.getAnnotation(annotationClass):${it.getAnnotation(annotationClass)} ")
        }*/
        clazz.declaredMethods.filter {
              return@filter it.isAnnotationPresent(annotationClass)
        }.forEach { it ->
            val annotation = it.getAnnotation(annotationClass)
            val propertyName = it.name
            Log.e("findPropertiesWithAnnotationInternal:","propertyName:$propertyName annotation:$annotation ")
            /*val getter =clazz.getDeclaredMethod(propertyName).also {
                check(Modifier.isPublic(it.modifiers))
            }
            val propertyType = getter.returnType
            val setter = clazz.getDeclaredMethod(propertyName, propertyType).also {
                check(Modifier.isPublic(it.modifiers))
            }
            Log.e("findPropertiesWithAnnotationInternal:","propertyName:$propertyName annotation:$annotation getter:$getter setter:$setter")*/
        }
    }

    companion object {
        private const val annotationPostfix = "\$annotations"
    }
}
inline fun <T, reified A : Annotation> Class<T>.findPropertiesWithAnnotationExt() {
    return KotlinAnnotationReflectionExt(this, A::class.java).findPropertiesWithAnnotationInternal()
}
