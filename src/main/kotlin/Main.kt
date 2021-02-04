import no.royag.fritznerordbok.isWordRangeLineISL

val bind1Start = 28
val bind1End = 862

fun String.norseToSortable() : String =
    this.map {
        when (it) {
            'á' -> 'a'
            'í' -> 'i'
            'ó' -> 'o'
            'ú' -> 'u'
            'ü' -> 'u'
            'ý' -> 'y'
            'þ' -> 'z'
            'ð' -> 'd'
            else -> it
        }
    }.joinToString("")



val pageSplit = "|"

// NB:   1:33(isl/dan) :  | aðalmerki  (halvdel 2 første linje slått over i halvdel 1)
// NB:   1:70 (isl) masse forskyvninger... + linje 31 splitt med "!" ++

fun main() {
    dosome1()
}

fun String.erDobbelLinje() : Boolean =
    this.contains(pageSplit)
            && !this.trim().endsWith(pageSplit)
            && !this.trim().startsWith(pageSplit)
            && this.length >= 50

fun dosome1() {
    //val page = Page.load(bookNo = 1, pageNo = 33)
    //page.printSideBySide() //maxLines = 10)  — - - —-
    var siderMedDobbellinjer = 0
    for (pno in bind1Start until 100) {
        val p = Page.load(bookNo = 1, pageNo = pno)
        println("PAGE $pno")
        var foundWordRangeCount = 0
        var dobbelLinjer = 0
        p.ocrIsl.forEach {
            if (it.isWordRangeLineISL()) {
                //println(it)
                foundWordRangeCount++
            }
            if (it.erDobbelLinje()) {
                dobbelLinjer++
                //println(it)
            }
            // 062 áhöfn, f.

            /*if (it.contains(pageSplit)
                && !it.trim().endsWith(pageSplit)
                && !it.trim().startsWith(pageSplit)) {

                println(it)
            }*/
        }
        if (foundWordRangeCount == 0) println("NOT FOUND")
        else if (foundWordRangeCount > 1) println("FOUND MANY $foundWordRangeCount")//throw RuntimeException("FOUND MANY $foundWordRangeCount")
        if (dobbelLinjer > 0) siderMedDobbellinjer++
    }
    println("sidermeddobbellinjer=$siderMedDobbellinjer")
}