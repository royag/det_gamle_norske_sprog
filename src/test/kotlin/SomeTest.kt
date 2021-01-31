import org.junit.Test

class SomeTest {


    @Test
    fun test1() {
        val dollar = "$"
        val norseLetter = """[[a-z][A-Z][áóúíöüæøåðþ][ÁÓÚÍÖÜÆØÅ]]"""
        val s1 = "allúdæl) — allvandlátr 43"
        val s2 = "allúdæll — allvandlátr 43"
        val s3 = "allúdæll — allvandlátr"
        val r = Regex("""^$norseLetter+ — $norseLetter+ *\d*$dollar""")
        println(r.matches(s1))
        println(r.matches(s2))
        println(r.matches(s3))
    }

}