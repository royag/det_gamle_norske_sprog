import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import no.royag.fritznerordbok.NORSE_LETTER
import no.royag.fritznerordbok.isWordRangeLineDAN
import no.royag.fritznerordbok.isWordRangeLineISL
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

class SplitFixTest {

    fun List<String>.print(lineno: Boolean = true) {
        this.forEachIndexed { i, s ->
            if (lineno) print((i + 1).toString().padStart(3, '0') + " ")
            println(s)
        }
    }

    fun String.erDobbelLinje(): Boolean =
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

    fun Page.wordEntryOf(line: String): String? {
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

    val charOrNumber = "[$NORSE_LETTER[0-9]]"
    fun String.countCharOrNumber() =
        this.filter { Regex(charOrNumber).containsMatchIn("$it") }.length
    val rubbishSymbol = "[«»·—]"
    fun String.countRubbishSymbols() =
        this.filter { Regex(rubbishSymbol).containsMatchIn("$it") }.length

    val predefinedRubbish = listOf(
        "${(12.toByte()).toChar()}",
        //'\f',
        "sov-» ·s·s sessss", // dan
        "#08", // isl39
        "s-« s-:«--s-skt»-Os", // dan 104
        "———————————.....—.A—A.A.2.2.2.2A.2.AALUÐUÐAÐ8ÐUÐ8ÐUApUAUpUAÁpþ\$W(WVWVWÝ£WA#AW—W—W£W£—AtA—t—t—tTtTLPLPLPLPLPLPLPLPLPLPPPPPP„P„P„——————22—2—————.—.", // isl 70:119
    )

    fun String.isWordRangeLineANY() = this.isWordRangeLineISL() || this.isWordRangeLineDAN()
    fun String.isRubbishLine(): Boolean =
        this.trim().let {
            it.length <= 2
                    || (it.length <= 3 && it.endsWith("«"))
                    || !Regex("[$NORSE_LETTER[0-9]]+").containsMatchIn(it)
                    || (it.countCharOrNumber() < it.length / 3)
                    || (it.countCharOrNumber() < 2)
                    //|| (it.length < 20 && it.countRubbishSymbols().toDouble() >= maxOf(1.1, it.length.toDouble() / 3.0))
                    || (it.countRubbishSymbols().toDouble() > it.countCharOrNumber().toDouble() / 2.0)
                    || predefinedRubbish.contains(it)
        }
    //   || !Regex("[$NORSE_LETTER[0-9]]{2,}").containsMatchIn(this)


    @Test
    fun utilTest() {
        println("o Ude ««-s»s« ( ".let {
            println(it.length)
            println(it.length.toDouble() / 4.0)
            println(it.countCharOrNumber())
            println(it.countRubbishSymbols())
           // (it.length < 20 && it.countRubbishSymbols().toDouble() >= maxOf(1.1, it.length.toDouble() / 3.0))
            (it.countRubbishSymbols().toDouble() > it.countCharOrNumber().toDouble() / 2.0)
        })
        println("Pr.".countRubbishSymbols())
        assertFalse("Pr.".isRubbishLine())
        assertFalse("Sd Mart".isRubbishLine())
        assertFalse("Sér. 9. 75.".isRubbishLine())
        assertFalse("1X, 319U·«.".isRubbishLine())
        assertFalse("l, 6 4.".isRubbishLine())
        assertFalse("(= þat á þik) N). 6.".isRubbishLine())
        assertFalse("vanslcegtez afkyujast".isRubbishLine())
        assertFalse("vanslcegtez afkyujast".isWordRangeLineDAN())

        assertTrue("sov-» ·s·s sessss            ".isRubbishLine())
        assertTrue("o Ude ««-s»s« ( ".isRubbishLine())
        assertTrue("»·———k".isRubbishLine())
        assertTrue("— Id »-".isRubbishLine())
        assertTrue("1 - ".isRubbishLine())
        assertTrue("l.«  ".isRubbishLine())
        assertTrue(". ,..—- ———,« -  ".isRubbishLine())
        assertTrue("s-« s-:«--s-skt»-Os".isRubbishLine())

        assertFalse("Sd Mart".isWordRangeLineANY())

        assertTrue("-—————————————————————————————".isRubbishLine())
        assertTrue("———————————.....—.A—A.A.2.2.2.2A.2.AALUÐUÐAÐ8ÐUÐ8ÐUApUAUpUAÁpþ\$W(WVWVWÝ£WA#AW—W—W£W£—AtA—t—t—tTtTLPLPLPLPLPLPLPLPLPLPPPPPP„P„P„——————22—2—————.—."
            .isRubbishLine())
    }

    fun List<String>.fjernBlanke(): List<String> =
        this.filter {
            //(!it.isWordRangeLineANY())
              //      &&
                    //(!it.isRubbishLine())
                    //&&
                            it.trim().length > 0
        }.let {
            //println(it)
            if (it.isNotEmpty() && it[0].trim().length < 5) {
                it.drop(1) // linenumber
            } else {
                it
            }
        }


    fun List<String>.utenSmåtingPaaStarten() : List<String> =
        mutableListOf<String>().also { ret ->
            var ignore = true
            this.forEach {
                if (it.length > 10 && !it.isRubbishLine()) {
                    ignore = false
                }
                if (!ignore) ret.add(it)
            }
        }


    @Test
    fun tryMatchISLDAN() {
/*
        val s = Page.load(1, 46).ocrDan[12]
        println(":" + s + ":")
        println(":" + s.trim().isWordRangeLineDAN() + ":")
        println("Pr.".isRubbishLine())
        //println("Sd Mart".isWordRangeLineDAN())
        if (true) return*/

        var first = true
        for (pn in 30..128) {
            val p = Page.load(bookNo = 1, pageNo = pn)
            val ocrDan = p.ocrDan //.utenSmåtingPaaStarten()
            val ocrIsl = p.ocrIsl //.utenSmåtingPaaStarten()
            val danRange = ocrDan.filter { it.isWordRangeLineDAN() }
            val islRange = ocrIsl.filter { it.isWordRangeLineISL() }
            var isl = ocrIsl //.fjernBlanke()
            var dan = ocrDan //.fjernBlanke()
            // 113: atdj 11 p —- atferd
            println("--------------------- $pn -----------------------")
            if (isl.size != dan.size) {
                println("try adjust...page=$pn (${isl.size} vs ${dan.size})")
                //println(danRange)
                //println(islRange)
                val i = isl.toMutableList()
                val d = dan.toMutableList()
                val i2 = mutableListOf<String>()
                val d2 = mutableListOf<String>()
                while (i.isNotEmpty() && d.isNotEmpty()) {
                    var dw = d.removeAt(0)
                    var iw = i.removeAt(0)
                    if (dw.isRubbishLine() && iw.isRubbishLine()) {
                        print("double rubbish ($dw    VS     $iw)")
                        continue
                    }
                    println("TRY: " + dw.padEnd(70) + iw)
                    var didthrow = false
                    while (dw.length.toDouble() > iw.length.toDouble() * 2.0) {
                        println("throwing ISL \t\t\t\t $iw    (does not match $dw)")
                        iw = i.removeAt(0)
                        didthrow = true
                    }
                    while (iw.length.toDouble() > dw.length.toDouble() * 2.0) {
                        println("throwing DAN $dw    (does not match $iw)")
                        dw = d.removeAt(0)
                        didthrow = true
                    }
                    if (didthrow) println("USE: " + dw.padEnd(70) + iw)
                    i2.add(iw)
                    d2.add(dw)
                    if (i.isEmpty() != d.isEmpty()) {
                        println("!!!! TIL OVERS !!!!! : $i , $d")
                        println("FRA:")
                        Page(dan, isl).printSideBySide()
                        println("TIL:")
                        Page(d2, i2).printSideBySide()
                        throw RuntimeException("TIL OVERS")
                    }
                }
                //println("RES = ${i2.size} vd ${d2.size}")
                isl = i2
                dan = d2
                //Page(dan, isl).printSideBySide()
            } else {
                println("len = ${isl.size}")
            }
            println(isl.last() + "\t\t" + dan.last())
            if (isl.last().length == 1) {
                println(isl.last()[0].toByte())
                println("${(12.toByte()).toChar()}" == isl.last())
            }

            if (isl.size != dan.size) {
                println("$pn OH NO!")
                println(isl.size - dan.size)
                println(danRange)
                println(islRange)
                if (first) Page(dan, isl).printSideBySide()
                first = false
            }
        }
    }

    @Test
    fun test1() {
        val linesPerColumn = 61
        val p = Page.load(bookNo = 1, pageNo = 49)
        println(p.wordRange())
        val isl = p.ocrIsl
        var l = isl.fjernBlanke()
        val d = p.ocrDan.fjernBlanke()
        Page(d, l).printSideBySide()
        // so... should be 122 ?   (=61 * 2)
        if (true) return
        /*
        l.mapNotNull { p.wordEntryOf(it) }.forEach { word ->
            println(word)
        }*/


        var last = ""
        l.forEach {
            if ((p.wordEntryOf(it) != null)
                || (last.length < 25)
            ) {
                println("----------BLOCK-START-------------")
            }
            println(it)
            last = it
        }





        l.print()
        // UGH: 062 áhöfn, f. ->eg. start på 065
        // and try fix:
        val tooFew = (linesPerColumn * 2) - l.size
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


