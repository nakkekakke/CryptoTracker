package cryptotracker.domain;

import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class CryptoBatchTest {
    
    CryptoBatch testBatch;
    Cryptocurrency assistanceCrypto;


    @Before
    public void setUp() {
        User assistanceUser = new User(4, "testUser");
        Portfolio assistancePortfolio = new Portfolio(5, assistanceUser);
        this.assistanceCrypto = new Cryptocurrency(6, "testCrypto", assistancePortfolio);
        
        int testAmount = 50;
        int testTotalPaid = 10000;
        LocalDate testDate = LocalDate.parse("2018-01-01");
        
        this.testBatch = new CryptoBatch(33, testAmount, testTotalPaid, testDate, assistanceCrypto);
        
    }
    
    @Test
    public void constructorSetsIdCorrectly() {
        assertEquals(33, testBatch.getId());
    }

    @Test
    public void constructorSetsCryptoCorrectly() {
        assertEquals(assistanceCrypto, testBatch.getCrypto());
    }
    
    @Test
    public void constructorSetsAmountCorrectly() {
        assertEquals(50, testBatch.getAmount());
    }
    
    @Test
    public void constructorSetsTotalPaidCorrectly() {
        assertEquals(10000, testBatch.getTotalPaid());
    }
    
    @Test
    public void constructorSetsDateCorrectly() {
        assertEquals(LocalDate.parse("2018-01-01"), testBatch.getDate());
    }
    
    @Test
    public void getExchangeRateWorksCorrectly() {
        assertTrue(testBatch.getExchangeRate() == 200.0);
    }
    
    @Test
    public void equalsReturnsTrueIfSameIds() {
        User user = new User(1, "lol");
        Portfolio p = new Portfolio(1, user);
        Cryptocurrency crypto = new Cryptocurrency(1, "Ethereum", p);
        Object o = new CryptoBatch(33, 1, 10, LocalDate.parse("2016-06-21"), crypto);
        
        assertEquals(o, testBatch);
    }
    
    @Test
    public void equalsFailsIfObjectsDifferentTypes() {
        Object o = new String();
        assertFalse(testBatch.equals(o));
    }
    
    @Test
    public void hashCodeTest() {
        CryptoBatch anotherBatch = new CryptoBatch(1, 1, 1, LocalDate.now(), assistanceCrypto);
        int hash = 586;
        assertEquals(testBatch.hashCode(), hash);
        assertNotEquals(anotherBatch.hashCode(), hash);
    }

}