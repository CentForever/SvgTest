package jp.shts.android.trianglelabelview

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.NonNull

class FontSizeContextWrapper(base: Context?) : ContextWrapper(base) {
    companion object {
        @NonNull
        fun wrap(context: Context): ContextWrapper {
            var context = context
            val resources = context.resources
            val newConfig = Configuration()
            val metrics = resources.displayMetrics
            newConfig.setToDefaults()
            // 如果没有设置densityDpi, createConfigurationContext对字体大小设置限制无效
            newConfig.densityDpi = metrics.densityDpi
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context = context.createConfigurationContext(newConfig)
            } else {
                resources.updateConfiguration(newConfig, resources.displayMetrics)
            }
            return FontSizeContextWrapper(context)
        }
    }
}