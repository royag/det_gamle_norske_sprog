import no.royag.fritznerordbok.EntryMerger
import no.royag.fritznerordbok.normalizeLine
import org.junit.Assert.assertEquals
import org.junit.Test



class EntryMergerTest {

    @Test
    fun test1() {
        val dan = """
            eiquarorC n. Bistand, Hjælp sont Hel-
            gener gjøre Menneskene ved sin Forbpn
            hos Gud. F!»t. 11, 38619.38915; llIcUu
            XX15; IIOmiL 837. 1014. 199. 8410.             
        """.trimIndent().split('\n')
        val isl = """
            árnaðarorð, n. Biftand, Hjælp jom Hel:
            gener gjóre Menneifene beð fin Forbyn
            boð Gub. Flat. II, 3861. 3895, Mar.
            XX; Homil. 837. 104. 199, 847.
        """.trimIndent().split('\n')

        val res = EntryMerger(dan = dan, isl = isl).merge()
        println(res)
    }


    @Test
    fun `normalize legger til mellomrom bak punktum`() {
        val input = "dfsd fdsf, fdsfdsf. fdsf 38619.38915 dsfds"
        val expected = "dfsd fdsf, fdsfdsf. fdsf 38619. 38915 dsfds"
        assertEquals(expected, input.normalizeLine())
    }


}