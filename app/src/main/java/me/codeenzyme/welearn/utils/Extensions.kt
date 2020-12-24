package me.codeenzyme.welearn.utils

import android.widget.EditText

fun EditText.getTrimmedStringValue(): String {
    return this.text.toString().trim()
}

fun EditText.getStringValue(): String {
    return this.text.toString()
}