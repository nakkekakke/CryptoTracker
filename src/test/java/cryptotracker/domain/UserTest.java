package cryptotracker.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    
    User testUser;

    @Before
    public void setUp() {
        testUser = new User(313, "testUsername");
    }

    @Test
    public void constructorSetsUsernameCorrectly() {
        assertEquals("testUsername", testUser.getUsername());
    }
    
    @Test
    public void constructorSetsIdCorrectly() {
        assertEquals(313, testUser.getId());
    }
    
    @Test
    public void constructorSetsPortfolioNull() {
        assertEquals(null, testUser.getPortfolio());
    }
    
    @Test
    public void setPortfolioSetsPortfolioCorrectly() {
        Portfolio p = new Portfolio(15, testUser);
        testUser.setPortfolio(p);
        
        assertEquals(p, testUser.getPortfolio());
        assertEquals(testUser, testUser.getPortfolio().getUser());
    }
    
    @Test
    public void equalsFailsIfObjectsDifferentTypes() {
        Object stringObject = new String();
        assertFalse(testUser.equals(stringObject));
    }
    
    @Test
    public void equalsFailsIfObjectIsNull() {
        User user = null;
        assertFalse(testUser.equals(user));
    }
    
    @Test
    public void hashCodeTest() {
        User user = new User(1, "test");
        int hash = 3560224;
        assertEquals(user.hashCode(), hash);
        assertNotEquals(testUser.hashCode(), hash);
    }
}