import java.io.File



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
    fun printSideBySide() {
        val widthLeft = ocrDan.map { it.length }.maxOrNull()!! + 10
        if (!sameLength()) println("NB: Not the same number of lines!") // feks: page 39
        for (i in 0 until maxLength()) {
            println(ocrDan.line(i).padEnd(widthLeft) + ocrIsl.line(i))
        }
    }
}


fun main() {
    val page = Page.load(bookNo = 1, pageNo = 100)
    page.printSideBySide()
}