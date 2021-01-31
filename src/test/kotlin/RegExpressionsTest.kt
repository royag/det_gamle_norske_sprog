import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import no.royag.fritznerordbok.ISL
import org.junit.Test

class RegExpressionsTest {

    @Test
    fun matchWordRangeLine() {
        assertTrue(ISL.wordRangeLinePageNumberRight.matches("atbrýða — afganga 11"))
        assertFalse(ISL.wordRangeLinePageNumberRight.matches("atbrýða  afganga 11"))

    }

    @Test
    fun matchDoubleWordsInWordRangeLine() {
        assertTrue(ISL.wordRangeLine.matches("annarr hvárr — annboði"))
        assertTrue(ISL.wordRangeLine.matches("annarr — annarra bröðra"))
        assertTrue(ISL.wordRangeLinePageNumberLeft.matches("60 annarr — annarra bröðra"))
        assertTrue(ISL.wordRangeLinePageNumberRight.matches("annarr — annarra bröðra 60"))

    }
}