import junit.framework.Assert.*
import org.junit.Test

class PageTest {

    @Test
    fun `wordRange and couldContain`() {
        val p = Page.load(bookNo = 1, pageNo = 49)
        assertEquals("áhugalítil" to "aka", p.wordRange())

        assertTrue(p.couldContain("áhögg"))
        assertFalse(p.couldContain("alt"))
        assertFalse(p.couldContain("ag"))
    }


}