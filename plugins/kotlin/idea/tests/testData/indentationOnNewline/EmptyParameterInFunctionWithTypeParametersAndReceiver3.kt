class A {
    fun <T, V> Int.Companion.testParam(<caret>) {
    }
}

// SET_TRUE: ALIGN_MULTILINE_METHOD_BRACKETS
// IGNORE_FORMATTER
// KT-39459