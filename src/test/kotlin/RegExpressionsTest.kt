import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import no.royag.fritznerordbok.DAN
import no.royag.fritznerordbok.DANISH_SINGLE_WORD
import no.royag.fritznerordbok.ISL
import no.royag.fritznerordbok.isWordRangeLineDAN
import org.junit.Test

class RegExpressionsTest {

    @Test
    fun matchWordRangeLine() {
        assertTrue(ISL.wordRangeLinePageNumberRight.matches("atbrýða — afganga 11"))
        assertFalse(ISL.wordRangeLinePageNumberRight.matches("atbrýða  afganga 11"))
        assertTrue(ISL.wordRangeLine.matches("afskiptalítill —— attelja"))
    }

    @Test
    fun matchWordRangeLineDAN() {
        //assertTrue(Regex(DANISH_SINGLE_WORD).matches("Fata-tin-"))
        assertTrue(DAN.wordRangeLinePageNumberRight.matches("kihugalitil —- aka   11"))
        assertTrue(DAN.wordRangeLinePageNumberRight.matches("sidrejting ——y af 7"))
        assertTrue(DAN.wordRangeLine.matches("kihugalitil —- aka   "))
        assertTrue(DAN.wordRangeLine.matches("tifa ——— afbrugdnjng "))
        assertTrue(DAN.wordRangeLine.matches("aftigna ——— Kgangs      "))

        assertTrue("aftigna ——— Kgangs".isWordRangeLineDAN())

        //assertTrue(DAN.wordRangeLine.matches("Fata-tin- — atietjn"))

        assertFalse(DAN.wordRangeLinePageNumberRight.matches("kihugalitil  aka  11"))
    }


    @Test
    fun matchDoubleWordsInWordRangeLine() {
        assertTrue(ISL.wordRangeLine.matches("annarr hvárr — annboði"))
        assertTrue(ISL.wordRangeLine.matches("annarr — annarra bröðra"))
        assertTrue(ISL.wordRangeLinePageNumberLeft.matches("60 annarr — annarra bröðra"))
        assertTrue(ISL.wordRangeLinePageNumberRight.matches("annarr — annarra bröðra 60"))

    }
}