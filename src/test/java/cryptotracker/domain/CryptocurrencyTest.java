package cryptotracker.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class CryptocurrencyTest {
    
    Cryptocurrency testCrypto;
    User assistanceUser;
    Portfolio assistancePortfolio;

    @Before
    public void setUp() {
        this.assistanceUser = new User(15, "username");
        this.assistancePortfolio = new Portfolio(21, assistanceUser);
        
        this.testCrypto = new Cryptocurrency(29, "TestCoin", assistancePortfolio);
    }

    @Test
    public void constructorSetsUsernameCorrectly() {
        assertEquals("TestCoin", testCrypto.getName());
    }
    
    @Test
    public void constructorSetsIdCorrectly() {
        assertEquals(29, testCrypto.getId());
    }
    
    @Test
    public void constructorSetsPortfolioCorrectly() {
        assertEquals(assistancePortfolio, testCrypto.getPortfolio());
    }
    
    @Test
    public void constructorInitializesBatchesAsArrayList() {
        assertTrue(testCrypto.getBatches() != null);
        assertEquals("java.util.ArrayList", testCrypto.getBatches().getClass().getName());
    }
    
    @Test
    public void equalsFailsIfObjectsDifferentTypes() {
        Object o = new String();
        assertFalse(testCrypto.equals(o));
    }
    
    @Test
    public void hashCodeTest() {
        Cryptocurrency anotherCrypto = new Cryptocurrency(1, "something", assistancePortfolio);
        int hash = -1082608455;
        assertEquals(testCrypto.hashCode(), hash);
        assertNotEquals(anotherCrypto.hashCode(), hash);
    }

}