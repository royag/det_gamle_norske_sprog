package no.royag.fritznerordbok

internal fun String.normalizeLine(): String =
    this
        .replace(".", ". ") // Alle punktum skal ha minst et mellomrom bak
        .replace("\\s+".toRegex(), " ") // Men aldri mer enn ett mellomrom
        .trim() // Men ingen mellomrom før eller etter

class EntryMerger(private val dan: List<String>, private val isl: List<String>) {
    init {
        require(dan.size == isl.size)
    }

    var curLine = 0
    val numLines = dan.size
    fun merge() {
        while (curLine < numLines) {
            val danWords = dan[curLine].normalizeLine().split(' ')
            val islWords = isl[curLine].normalizeLine().split(' ')
            println(danWords)
            println(islWords)
            if (danWords.size != islWords.size) err("Ulikt antall ord (${danWords.size} vs ${islWords.size})")
            curLine++
        }
    }

    private fun err(msg: String): Nothing {
        throw RuntimeException("Line #${curLine + 1}: Error: $msg")
    }

}
