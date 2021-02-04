package no.royag.fritznerordbok

val NORSE_ONLY = """[[áéíóúýüðþ][ÁÉÓÚÝÍÜ]]"""
val DANISH_LETTER = """[[a-z][A-Z][æøöåÆØÖÅ]]"""
val NORSE_LETTER = """[$DANISH_LETTER$NORSE_ONLY]"""
val NORSE_SINGLE_WORD = """$NORSE_LETTER+"""
val DANISH_SINGLE_WORD = """$DANISH_LETTER+"""
val NORSE_SINGLE_OR_DOUBLE_WORD = """$NORSE_LETTER+ ?$NORSE_LETTER*"""
val DANISH_SINGLE_OR_DOUBLE_WORD = """$DANISH_LETTER+ ?$DANISH_LETTER*"""

private val DOLLAR = "$"

val WORD_RANGE_DELIMITER = "[—\\=]{1,2}+"
val WORD_RANGE_DELIMITER_DAN = "[—\\-]{1,3}+" // ——-

object ISL {
    val wordRangeLinePageNumberRight =
        Regex("""^$NORSE_SINGLE_OR_DOUBLE_WORD *$WORD_RANGE_DELIMITER *$NORSE_SINGLE_OR_DOUBLE_WORD *\d+$DOLLAR""")
    val wordRangeLinePageNumberLeft =
        Regex("""^\d+ *$NORSE_SINGLE_OR_DOUBLE_WORD *$WORD_RANGE_DELIMITER *$NORSE_SINGLE_OR_DOUBLE_WORD *$DOLLAR""")
    val wordRangeLine =
        Regex("""^$NORSE_SINGLE_OR_DOUBLE_WORD *$WORD_RANGE_DELIMITER *$NORSE_SINGLE_OR_DOUBLE_WORD *$DOLLAR""")
    val singleWord = Regex("""^$NORSE_SINGLE_WORD$DOLLAR""")
}

object DAN {
    val wordRangeLinePageNumberRight =
        Regex("""^$DANISH_SINGLE_OR_DOUBLE_WORD *$WORD_RANGE_DELIMITER_DAN *$DANISH_SINGLE_OR_DOUBLE_WORD *\d+$DOLLAR""")
    val wordRangeLinePageNumberLeft =
        Regex("""^\d+ *$DANISH_SINGLE_OR_DOUBLE_WORD *$WORD_RANGE_DELIMITER_DAN *$DANISH_SINGLE_OR_DOUBLE_WORD *$DOLLAR""")
    val wordRangeLine =
        Regex("""^$DANISH_SINGLE_OR_DOUBLE_WORD *$WORD_RANGE_DELIMITER_DAN *$DANISH_SINGLE_OR_DOUBLE_WORD *$DOLLAR""")
    val singleWord = Regex("""^$DANISH_SINGLE_WORD$DOLLAR""")
}

fun String.isWordRangeLineISL(): Boolean =
    (this.toLowerCase() == this) && (
            ISL.wordRangeLine.matches(this) ||
                    ISL.wordRangeLinePageNumberRight.matches(this) ||
                    ISL.wordRangeLinePageNumberLeft.matches(this)
                    //|| ISL.singleWord.matches(this))
            )

fun String.isWordRangeLineDAN(): Boolean =
    //(this.toLowerCase() == this) &&
            (
            DAN.wordRangeLine.matches(this) ||
                    DAN.wordRangeLinePageNumberRight.matches(this) ||
                    DAN.wordRangeLinePageNumberLeft.matches(this)
                    //|| DAN.singleWord.matches(this)
            )

