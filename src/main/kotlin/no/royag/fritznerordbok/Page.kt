import no.royag.fritznerordbok.WORD_RANGE_DELIMITER
import no.royag.fritznerordbok.isWordRangeLineISL
import java.io.File
import java.lang.RuntimeException

class Page(val ocrDan: List<String>, val ocrIsl: List<String>) {
    companion object {
        private fun readFile(path: String): List<String> = File(path).useLines { it.toList()  }
        fun load(bookNo: Int, pageNo: Int) : Page {
            val pno = pageNo.toString().padStart(3, '0')
            return Page(
                ocrDan = readFile("ocr/dan_frak/bind${bookNo}/bind${bookNo}-${pno}_dan.txt"),
                ocrIsl = readFile("ocr/isl/bind${bookNo}/bind${bookNo}-${pno}_isl.txt"),
            )
        }
    }

    fun wordRange() : Pair<String, String> {
        val rangeLines = ocrIsl.filter { it.isWordRangeLineISL() }
        if (rangeLines.size != 1) throw RuntimeException("Klarte ikke velge wordRangeLine: $rangeLines")
        return rangeLines.first().split(Regex(WORD_RANGE_DELIMITER)).map { it.trim() }.let {
            require(it.size == 2)
            it.first() to it.last()
        }
    }

    fun couldContain(norseWord: String) : Boolean =
        norseWord.norseToSortable().let { word ->
            wordRange().let { range ->
                word >= range.first.norseToSortable() && word <= range.second.norseToSortable()
            }
        }

    fun sameLength() : Boolean = ocrDan.size == ocrIsl.size
    fun maxLength() = maxOf(ocrDan.size, ocrIsl.size)
    private fun List<String>.line(n:Int) : String = if (n >= this.size) "" else this[n]
    fun printSideBySide(maxLines: Int = 1000) {
        val widthLeft = ocrDan.map { it.length }.maxOrNull()!! + 10
        if (!sameLength()) println("NB: Not the same number of lines!") // feks: page 39
        for (i in 0 until minOf(maxLines, maxLength())) {
            println(ocrDan.line(i).padEnd(widthLeft) + ocrIsl.line(i))
        }
    }
}