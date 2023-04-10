package com.example.weathermate

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.LayoutInflater
import java.util.*

class MyContextWrapper(base: Context) : ContextWrapper(base) {

    companion object {
        fun wrap(context: Context, language: String): ContextWrapper {
            val config = context.resources.configuration
            val locale = Locale(language)
            Locale.setDefault(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale)
            } else {
                config.locale = locale
            }
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            return MyContextWrapper(context)
        }
    }

    override fun getSystemService(name: String): Any? {
        if (Context.LAYOUT_INFLATER_SERVICE == name) {
            return LayoutInflater.from(baseContext).cloneInContext(this)
        }
        return super.getSystemService(name)
    }
}
