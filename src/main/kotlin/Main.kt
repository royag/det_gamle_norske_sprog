import no.royag.fritznerordbok.isWordRangeLineISL
import java.io.File

val bind1Start = 28
val bind1End = 862

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

val pageSplit = "|"

// NB:   1:33(isl/dan) :  | aðalmerki  (halvdel 2 første linje slått over i halvdel 1)
// NB:   1:70 (isl) masse forskyvninger... + linje 31 splitt med "!" ++

fun main() {
    dosome1()
}

fun dosome1() {
    //val page = Page.load(bookNo = 1, pageNo = 33)
    //page.printSideBySide() //maxLines = 10)  — - - —-
    for (pno in bind1Start until 100) {
        val p = Page.load(bookNo = 1, pageNo = pno)
        println("PAGE $pno")
        var foundWordRangeCount = 0
        p.ocrIsl.forEach {
            if (it.isWordRangeLineISL()) {
                println(it)
                foundWordRangeCount++
            }
            /*if (it.contains(pageSplit)
                && !it.trim().endsWith(pageSplit)
                && !it.trim().startsWith(pageSplit)) {

                println(it)
            }*/
        }
        if (foundWordRangeCount == 0) println("NOT FOUND")
        else if (foundWordRangeCount > 1) println("FOUND MANY $foundWordRangeCount")//throw RuntimeException("FOUND MANY $foundWordRangeCount")
    }
}