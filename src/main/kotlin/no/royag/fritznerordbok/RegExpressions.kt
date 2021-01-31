package no.royag.fritznerordbok

val NORSE_ONLY = """[[áíóúýüðþ][ÁÓÚÝÍÜ]]"""
val NORSE_LETTER = """[[a-z][A-Z][æøöåÆØÖÅ]$NORSE_ONLY]"""
val NORSE_SINGLE_WORD = """$NORSE_LETTER+"""
val NORSE_SINGLE_OR_DOUBLE_WORD = """$NORSE_LETTER+ ?$NORSE_LETTER*"""

private val DOLLAR = "$"

object ISL {
    val wordRangeLinePageNumberRight = Regex("""^$NORSE_SINGLE_OR_DOUBLE_WORD *— *$NORSE_SINGLE_OR_DOUBLE_WORD *\d+$DOLLAR""")
    val wordRangeLinePageNumberLeft = Regex("""^\d+ *$NORSE_SINGLE_OR_DOUBLE_WORD *— *$NORSE_SINGLE_OR_DOUBLE_WORD *$DOLLAR""")
    val wordRangeLine = Regex("""^$NORSE_SINGLE_OR_DOUBLE_WORD *— *$NORSE_SINGLE_OR_DOUBLE_WORD *$DOLLAR""")
    val singleWord = Regex("""^$NORSE_SINGLE_WORD$DOLLAR""")
}

fun String.isWordRangeLineISL(): Boolean =
    ISL.wordRangeLine.matches(this) ||
            ISL.wordRangeLinePageNumberRight.matches(this) ||
            ISL.wordRangeLinePageNumberLeft.matches(this) ||
            ISL.singleWord.matches(this)

