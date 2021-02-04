import junit.framework.Assert.assertEquals
import no.royag.fritznerordbok.isWordRangeLineISL
import org.junit.Test

class SplitFixTest {

    fun List<String>.print(lineno:Boolean = true) {
        this.forEachIndexed { i, s ->
            if (lineno) print((i+1).toString().padStart(3, '0') + " ")
            println(s)
        }
    }

    fun String.erDobbelLinje() : Boolean =
        this.contains(pageSplit)
                && !this.trim().endsWith(pageSplit)
                && !this.trim().startsWith(pageSplit)
                && this.length >= 50


    // bind3-1078 -> þ -> æ1
    // NB: bind-3 1086: æ -> æ    så: æ(1085) < æ(1087)
/*
val NORSE_ONLY = """[[áíóúýüðþ][ÁÓÚÝÍÜ]]"""
val NORSE_LETTER = """[[a-z][A-Z][æøöåÆØÖÅ]$NORSE_ONLY]"""
 */


    @Test
    fun test3() {
        "" < ""
        "".compareTo("")
        val a = listOf("t", "á", "í", "ö", "d", "ð", "å", "þ", "æ", "s", "u")   // "þ" er etter "y" her
        println(a.sorted())
    }

    fun Page.wordEntryOf(line: String) : String? {
        line.trim().split(" ").firstOrNull().let { firstWordWithComma ->
            if (firstWordWithComma != null && firstWordWithComma.endsWith(",")) {
                val plainWord = firstWordWithComma.removeSuffix(",")
                if (this.couldContain(plainWord)) return plainWord
            }
        }
        return null
    }

    @Test
    fun testWordEntryOf() {
        // TODO: áhyggjusamliga,adr. meb alvorsfulbt Einb.
        val p = Page.load(bookNo = 1, pageNo = 49)
        val s = "áifóðr, n. Qvilefober. Stj. 214."
        assertEquals("áifóðr", p.wordEntryOf(s))
    }

    @Test
    fun test1() {
        val linesPerColumn = 61
        val p = Page.load(bookNo = 1, pageNo = 49)
        println(p.wordRange())
        val isl = p.ocrIsl
        var l = isl.filter {
            !it.isWordRangeLineISL()
                    && it.trim().length > 0
        }
        if (l[0].trim().length < 5) {
            l = l.drop(1) // linenumber
        }
        // so... should be 122 ?   (=61 * 2)

        /*
        l.mapNotNull { p.wordEntryOf(it) }.forEach { word ->
            println(word)
        }*/


        var last = ""
        l.forEach {
            if ((p.wordEntryOf(it) != null)
                || (last.length < 25) ) {
                println("----------BLOCK-START-------------")
            }
            println(it)
            last = it
        }

        if (true) return



        l.print()
        // UGH: 062 áhöfn, f. ->eg. start på 065
        // and try fix:
        val tooFew =  (linesPerColumn*2) - l.size
        l.forEach { if (it.erDobbelLinje()) println(it) }
        val antallDobelllinjer = l.count { it.erDobbelLinje() }
        if (tooFew != antallDobelllinjer) throw RuntimeException("OH NO!")

        while (l.any { it.erDobbelLinje() }) {
            l = l.let { thelines ->
                val lines = thelines.toMutableList()
                for (n in lines.size - 1 downTo 0) {
                    lines[n].also {
                        if (it.erDobbelLinje()) {
                            val splittet = it.split(pageSplit)
                            lines.add(n + linesPerColumn, splittet[1])
                            lines[n] = splittet[0]
                            return@let lines
                        }
                    }
                }
                throw RuntimeException("fant ingen dobbellinjer")
            }
        }

        l.print()


        println(isl.size)
        println(l.size)
    }



}