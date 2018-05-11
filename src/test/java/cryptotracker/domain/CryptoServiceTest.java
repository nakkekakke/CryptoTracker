package cryptotracker.domain;

import cryptotracker.dao.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CryptoServiceTest {
    
    @InjectMocks
    CryptoService service;
    
    @Mock
    DBUserDao mockUserDao;
    
    @Mock
    DBPortfolioDao mockPortfolioDao;
    
    @Mock
    DBCryptocurrencyDao mockCryptoDao;
    
    @Mock
    DBCryptoBatchDao mockBatchDao;
    
    private User testUser;
    private Portfolio testFolio;
    private Cryptocurrency testCrypto;
    private CryptoBatch testBatch;

    @Before
    public void setUp() {
        this.testUser = new User(195, "testUsername");
        this.testFolio = new Portfolio(54, testUser);
        this.testCrypto = new Cryptocurrency(12, "testCryptoName", testFolio);
        this.testBatch = new CryptoBatch(555, 10, 20, LocalDate.now(), testCrypto);
    }

    @Test
    public void usernameLengthValidReturnsTrueIfValid() {
        assertTrue(service.usernameLengthValid(testUser.getUsername()));
    }
    
    @Test
    public void usernameLengthValidReturnsFalseIfTooShortOrLong() {
        assertFalse(service.usernameLengthValid("lol"));
        assertFalse(service.usernameLengthValid("thisisaprettyrelativelyquitesomewhatofalongname"));
    }
    
    @Test
    public void loginReturnsTrueIfUsernameRegistered() throws SQLException {
        CryptoService spyService = spy(service);
        doReturn(testUser).when(mockUserDao).findOneWithUsername("testUsername");
        doReturn(testFolio).when(spyService).findPortfolio(testUser);
        assertTrue(spyService.login("testUsername"));
    }
    
    @Test
    public void loginReturnsFalseIfUsernameNotRegistered() throws SQLException {
        doReturn(null).when(mockUserDao).findOneWithUsername("testUsername");
        assertFalse(service.login("testUsername"));
    }
    
    @Test
    public void loginReturnsFalseIfExceptionOccurs() throws SQLException {
        doThrow(new SQLException()).when(mockUserDao).findOneWithUsername("testUsername");
        assertFalse(service.login("testUsername"));
    }
    
    @Test
    public void logoutResetsLoggedInUserAndPortfolio() throws SQLException {
        CryptoService spyService = spy(service);
        doReturn(testUser).when(mockUserDao).findOneWithUsername("testUsername");
        doReturn(testFolio).when(spyService).findPortfolio(testUser);
        spyService.login("testUsername");
        
        assertNotNull(spyService.getLoggedIn());
        assertNotNull(spyService.getActivePortfolio());
        
        spyService.logout();
        
        assertNull(spyService.getLoggedIn());
        assertNull(spyService.getActivePortfolio());
    }
    
    //
    // USER TESTS
    //
    
    @Test
    public void createUserReturnsTrueIfUserCreated() throws SQLException {
        CryptoService spyService = spy(service);
        doReturn(testUser).when(mockUserDao).save(testUser);
        doReturn(true).when(spyService).createPortfolio(testUser);
        assertTrue(spyService.createUser("testUsername"));
    }
    
    @Test
    public void createUserReturnsFalseIfUserAlreadyExists() throws SQLException {
        doReturn(null).when(mockUserDao).save(testUser);
        assertFalse(service.createUser("testUsername"));
    }
    
    @Test
    public void createUserReturnsFalseIfCouldntCreatePortfolio() throws SQLException {
        CryptoService spyService = spy(service);
        doReturn(testUser).when(mockUserDao).save(testUser);
        doReturn(false).when(spyService).createPortfolio(testUser);
        assertFalse(spyService.createUser("testUsername"));
    }
    
    @Test
    public void createUserReturnsFalseIfExceptionOccurs() throws SQLException {
        doThrow(new SQLException()).when(mockUserDao).save(testUser);
        assertFalse(service.createUser("testUsername"));
    }
    
    @Test
    public void findUserReturnsUserIfUserWasFound() throws SQLException {
        doReturn(testUser).when(mockUserDao).findOneWithUsername(testUser.getUsername());
        assertEquals(service.findUser(testUser), testUser);
    }
    
    @Test
    public void findUserReturnsNullIfExceptionOccurs() throws SQLException {
        doThrow(new SQLException()).when(mockUserDao).findOneWithUsername("testUsername");
        assertNull(service.findUser(testUser));
    }
    
    //
    // PORTFOLIO TESTS
    //
    
    @Test
    public void createPortfolioReturnsTrueIfAddedToUser() throws SQLException {
        CryptoService spyService = spy(service);
        User spyUser = spy(testUser);
        doReturn(spyUser).when(spyService).findUser(spyUser);
        doReturn(testFolio).when(spyService).findPortfolio(spyUser);
        
        boolean created = spyService.createPortfolio(spyUser);
        verify(mockPortfolioDao).save(new Portfolio(1, spyUser));
        verify(spyUser).setPortfolio(testFolio);
        
        assertTrue(created);
    }
    
    @Test
    public void createPortfolioReturnsFalseIfPortfolioAlreadyExists() throws SQLException {
        CryptoService spyService = spy(service);
        doReturn(testUser).when(spyService).findUser(testUser);
        testUser.setPortfolio(testFolio);
        
        assertFalse(spyService.createPortfolio(testUser));
        verify(mockPortfolioDao, never()).save(any(Portfolio.class));
    }
    
    @Test
    public void createPortfolioReturnsFalseIfSavedPortfolioCannotBeFound() throws SQLException {
        CryptoService spyService = spy(service);
        User spyUser = spy(testUser);
        doReturn(spyUser).when(spyService).findUser(spyUser);
        doReturn(null).when(spyService).findPortfolio(spyUser);
        
        assertFalse(spyService.createPortfolio(spyUser));
        verify(mockPortfolioDao).save(new Portfolio(1, spyUser));
        verify(spyUser, never()).setPortfolio(any(Portfolio.class));
    }
    
    @Test
    public void createPortfolioReturnsFalseIfExceptionOccurs() throws SQLException {
        CryptoService spyService = spy(service);
        doReturn(testUser).when(spyService).findUser(testUser);
        doThrow(new SQLException()).when(mockPortfolioDao).save(any(Portfolio.class));
        assertFalse(spyService.createPortfolio(testUser));
        
        verify(spyService, never()).findPortfolio(any(User.class));
    }
    
    @Test
    public void findPortfolioReturnsPortfolioIfFound() throws SQLException {
        doReturn(testFolio).when(mockPortfolioDao).findOneWithUser(testUser);
        Portfolio found = service.findPortfolio(testUser);
        assertNotNull(found);
        assertEquals(found, testFolio);
    }
    
    @Test
    public void findPortfolioReturnsNullIfExceptionOccurs() throws SQLException {
        doThrow(new SQLException()).when(mockPortfolioDao).findOneWithUser(testUser);
        assertNull(service.findPortfolio(testUser));
    }
    
    //
    // CRYPTOCURRENCY TESTS
    //
    
    @Test
    public void createCryptoInstanceReturnsCryptoIfItWasCreated() throws SQLException {
        CryptoService spyService = spy(service);
        doReturn(testCrypto).when(spyService).findCryptoByName("testCrypto");
        doReturn(testFolio).when(spyService).getActivePortfolio();
        
        assertNotNull(spyService.createCryptoInstance("testCrypto"));
        verify(mockCryptoDao).save(any(Cryptocurrency.class), any(Portfolio.class));
    }
    
    @Test
    public void deleteCryptocurrencyReturnsTrueIfCryptoWasDeleted() throws SQLException {
        List<Cryptocurrency> cryptos = new ArrayList<>();
        cryptos.add(testCrypto);
        
        CryptoService spyService = spy(service);
        doReturn(cryptos).when(spyService).getCryptosInPortfolio();
        
        assertTrue(spyService.deleteCryptocurrency(testCrypto));
        verify(spyService).getBatchesOfCrypto(testCrypto);
    }
    
    @Test
    public void deleteCryptocurrencyReturnsFalseIfCryptoDoesntExist() throws SQLException {
        List<Cryptocurrency> cryptos = new ArrayList<>();
        cryptos.add(new Cryptocurrency(1111, "something,", testFolio));
        
        CryptoService spyService = spy(service);
        doReturn(cryptos).when(spyService).getCryptosInPortfolio();
        
        assertFalse(spyService.deleteCryptocurrency(testCrypto));
        verify(spyService, never()).getBatchesOfCrypto(any(Cryptocurrency.class));
    }
    
    @Test
    public void deleteCryptocurrencyReturnsFalseIfExceptionOccurs() throws SQLException {
        List<Cryptocurrency> cryptos = new ArrayList<>();
        cryptos.add(testCrypto);
        
        CryptoService spyService = spy(service);
        doReturn(cryptos).when(spyService).getCryptosInPortfolio();
        doThrow(new SQLException()).when(mockCryptoDao).delete(any(Integer.class));
        
        assertFalse(spyService.deleteCryptocurrency(testCrypto));
        verify(spyService).getBatchesOfCrypto(testCrypto);
    }

}