// IS_APPLICABLE: false
fun foo(x: Foo) {
    x.<caret>foo(bar = x)
}

trait Foo {
    fun foo(baz: Int = 0, bar: Foo? = null)
}
