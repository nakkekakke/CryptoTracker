package cryptotracker.domain;

import cryptotracker.dao.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**  The class that handles the application logic
 * 
 */ 
public class CryptoService {
    
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final CryptocurrencyDao cryptoDao;
    private final CryptoBatchDao batchDao;
    private int usernameMinLength;
    private int usernameMaxLength;
    private User loggedIn;
    private Portfolio activePortfolio;
    
    public CryptoService(UserDao userDao, PortfolioDao portfolioDao, CryptocurrencyDao cryptoDao, CryptoBatchDao batchDao) {
        this.userDao = userDao;
        this.portfolioDao = portfolioDao;
        this.cryptoDao = cryptoDao;
        this.batchDao = batchDao;
        this.usernameMinLength = 4;
        this.usernameMaxLength = 20;
        this.loggedIn = null;
        this.activePortfolio = null;
    }
    
    public int getUsernameMinLength() {
        return usernameMinLength;
    }
    
    public void setUsernameMinLength(int length) {
        this.usernameMinLength = length;
    }
    
    public int getUsernameMaxLength() {
        return usernameMaxLength;
    }
    
    public void setUsernameMaxLength(int length) {
        this.usernameMaxLength = length;
    }
    
    public User getLoggedIn() {
        return loggedIn;
    }
    
    public void setLoggedIn(User user) {
        this.loggedIn = user;
    }
    
    public Portfolio getActivePortfolio() {
        return activePortfolio;
    }
    
    public void setActivePortfolio(Portfolio portfolio) {
        this.activePortfolio = portfolio;
    }
    
    //
    // GENERAL
    //
    
    
    /** Checks if the length of a username is valid
     * 
     * @param username The username that will be checked
     * @return True if the length of the username is valid, false if too short or too long
     */ 
    public boolean usernameLengthValid(String username) {
        if (username.length() >= getUsernameMinLength() 
                && username.length() <= getUsernameMaxLength()) {
            return true;
        }
        return false;
    }
    
 
    /** Logs the user in
     * 
     * @param username The username used to log in
     * @return True if login was successful, otherwise false
     */ 
    public boolean login(String username) {
        User user;
        try {
            user = userDao.findOneWithUsername(username);
        } catch (SQLException e) {
            return false;
        }
        
        if (user == null) {
            return false;
        }
        
        setLoggedIn(user);
        
        setActivePortfolio(findPortfolio(user));
        
        return true;
    }
    
    /** Logs out the user who is currently logged in
     *   
     */ 
    public void logout() {
        setLoggedIn(null);
        setActivePortfolio(null);
    }
    
    //
    // USER
    //
    
    /** Creates a new user if the username is not in use
     * 
     * @param username Username of the new user 
     * @return True if username was available, false if user with the same username already exists
     */     
    public boolean createUser(String username) {
        User user = new User(1, username);
        try {
            user = userDao.save(user);
        } catch (SQLException e) {
            return false;
        }
        
        if (user == null) {
            return false;
        }
        
        if (createPortfolio(user) == false) {
            return false;
        }
        return true;
    }
    
    /** Finds out if an user is already stored in the application
     * 
     * @param user The user who will be looked for
     * @return The found user, or null if nothing was found
     */
    public User findUser(User user) {
        User foundUser;
        
        try {
            foundUser = userDao.findOneWithUsername(user.getUsername());
        } catch (SQLException e) {
            return null;
        }
        
        return foundUser;
    }
    
    //
    // PORTFOLIO
    //
    
    
    /** Creates a new portfolio for a user; used when creating a new user
     * 
     * @param user The user for which a new portfolio will be created
     * @return True if a portfolio was added for the user, otherwise false
     */
    public boolean createPortfolio(User user) {
        User foundUser = findUser(user);
        if (foundUser.getPortfolio() != null) {
            return false;
        }
        
        Portfolio portfolio = new Portfolio(1, foundUser);
        
        try {
            portfolioDao.save(portfolio);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        Portfolio p = findPortfolio(user);
        if (p == null) {
            return false;
        }
        
        user.setPortfolio(p);
        return true;
    }
    
    /** Finds the portfolio of an user, stored in the application
     * 
     * @param user The user whose portfolio will be looked for
     * @return The found portfolio, null if nothing was found
     */
    public Portfolio findPortfolio(User user) {
        
        try {
            return portfolioDao.findOneWithUser(user);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        
    }
    
    //
    // CRYPTOCURRENCY
    //
    
    Cryptocurrency createCryptoInstance(String name) {

        try {
            Cryptocurrency newCrypto = new Cryptocurrency(1, name, getActivePortfolio());
            cryptoDao.save(newCrypto, getActivePortfolio());
            return findCryptoByName(name);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    /** Deletes a cryptocurrency and all of its CryptoBatches from the active portfolio
     * 
     * @param crypto The cryptocurrency which will be deleted
     * @return True if deletion was successful, false if an error occurred
     */
    public boolean deleteCryptocurrency(Cryptocurrency crypto) {
        for (Cryptocurrency c : getCryptosInPortfolio()) {
            if (c.equals(crypto)) {
                try {
                    deleteBatchesOfCrypto(c);
                    cryptoDao.delete(c.getId());
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return false;
                }
                return true;
            }
        }
        
        return false;
    }
    
    /** Finds all cryptocurrencies in the currently active portfolio
     * 
     * @return A list of cryptocurrencies found in the portfolio, or null if an error occurred
     */
    public List<Cryptocurrency> getCryptosInPortfolio() {
        try {
            return cryptoDao.findAllInPortfolio(getActivePortfolio());
        } catch (SQLException e) {
            return null;
        }
    }
    
    Cryptocurrency findCryptoByName(String name) {
        for (Cryptocurrency c : getCryptosInPortfolio()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        
        return null;
    }
    
    //
    // CRYPTOBATCH
    //
    
    /** Creates a new batch of cryptocurrency. Also creates a new cryptocurrency instance if there is no cryptocurrency with the same name.
     * 
     * @param name The name of the cryptocurrency
     * @param amount The amount of cryptocurrency in the batch
     * @param totalPaid The value of the batch
     * @param date The purchase date of the batch
     * @return 
     */
    public CryptoBatch createCrypto(String name, int amount, int totalPaid, LocalDate date) {
        Cryptocurrency crypto = findCryptoByName(name);
        if (crypto == null) {
            crypto = createCryptoInstance(name);
            if (crypto == null) {
                return null;
            }
        }
        
        CryptoBatch newBatch = new CryptoBatch(1, amount, totalPaid, date, crypto);
        try {
            return batchDao.save(newBatch, crypto);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    /** Finds all batches of a cryptocurrency in the active portfolio
     * 
     * @param crypto The cryptocurrency whose batches will be looked for
     * @return A list of CryptoBatches found; an empty list if nothing was found
     */
    public List<CryptoBatch> getBatchesOfCrypto(Cryptocurrency crypto) {
        List<CryptoBatch> batches = new ArrayList<>();
        try {
            batches = batchDao.findAllFromCryptocurrency(crypto);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return batches;
    }
    
    void deleteBatchesOfCrypto(Cryptocurrency crypto) {
        getBatchesOfCrypto(crypto).forEach((batch) -> {
            deleteBatch(batch.getId());
        });
    }
    
    /** Deletes a CryptoBatch, specified by an id
     * 
     * @param id The id of the CryptoBatch that will be deleted
     */
    public void deleteBatch(Integer id) {
        try {
            batchDao.delete(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
