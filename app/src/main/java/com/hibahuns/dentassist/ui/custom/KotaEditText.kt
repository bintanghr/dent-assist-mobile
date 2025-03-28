package com.hibahuns.dentassist.ui.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.hibahuns.dentassist.R

class KotaEditText : TextInputEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val formatted = it.toString().lowercase().replaceFirstChar { char -> char.uppercaseChar() }
                    if (formatted != it.toString()) {
                        setText(formatted)
                        setSelection(formatted.length) 
                    }

                    error = if (!formatted.matches(Regex("^[A-Z][a-z]*\$"))) {
                        context.getString(R.string.kota_error)
                    } else {
                        null
                    }
                }
            }
        })
    }
}
