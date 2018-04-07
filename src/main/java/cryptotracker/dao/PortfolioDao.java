package cryptotracker.dao;

import cryptotracker.domain.Database;
import cryptotracker.domain.Portfolio;
import cryptotracker.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PortfolioDao implements Dao<Portfolio, Integer> {
    
    private Database database;
    private UserDao userDao;
    
    public PortfolioDao(Database database, UserDao userDao) {
        this.database = database;
        this.userDao = userDao;
    }

/**  Finds an user by ID
 * 
 *   @param id ID associated with a user
 *   @return User, if any with the specified ID was found; null if no user was found
 *   @throws java.sql.SQLException
 */    
    public Portfolio findOneWithId(int id) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stat = conn.prepareStatement("SELECT * FROM Portfolio WHERE Portfolio.id = ?");
        stat.setInt(1, id);
        ResultSet rs = stat.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            stat.close();
            conn.close();
            return null;
        }
        
        User user = userDao.findOneWithID(rs.getInt("user_id"));
        
        Portfolio portfolio = new Portfolio(rs.getInt("id"), user, rs.getString("name"));
        
        rs.close();
        stat.close();
        conn.close();
        return portfolio;
    }    
    
/**  Finds all users stored in the database
 * 
 *   @return A list containing every user found in the database, an empty list if nothing was found
 *   @throws java.sql.SQLException
 */ 
    @Override
    public List<Portfolio> findAll() throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stat = conn.prepareStatement("SELECT * FROM Portfolio");
        ResultSet rs = stat.executeQuery();
        
        List<Portfolio> portfolios = new ArrayList();
        
        while (rs.next()) {
            User user = userDao.findOneWithID(rs.getInt("user_id"));
            portfolios.add(new Portfolio(rs.getInt("id"), user, rs.getString("name")));
        }
        
        rs.close();
        stat.close();
        conn.close();
        
        return portfolios;
    }
    
/**  Adds a user to the database 
 * 
 *   @param portfolio The portfolio that will be saved to the database
 *   @return The saved portfolio, or null if portfolio was already in the database
 *   @throws java.sql.SQLException
 */ 
    @Override
    public Portfolio save(Portfolio portfolio) throws SQLException {
        
        if (findOneWithId(portfolio.getId()) != null) {     // checks if portfolio already exists in the database
            return null;
        }
        
        Connection conn = database.getConnection();
        PreparedStatement stat = conn.prepareStatement("INSERT INTO Portfolio (name, user_id) VALUES (?, ?)");
        stat.setString(1, portfolio.getNimi());
        stat.setInt(2, portfolio.getUser().getId());
        
        stat.executeUpdate();
        
        stat.close();
        conn.close();
        
        return portfolio;
    }
    
/**  Deletes a portfolio from the database using id
 * 
 *   @param id The id of the portfolio that is to be deleted
 *   @throws java.sql.SQLException
 */ 
    @Override
    public void delete(Integer id) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stat = conn.prepareStatement("DELETE FROM Portfolio WHERE Portfolio.id = " + id);
        
        stat.executeUpdate();
        
        stat.close();
        conn.close();
    }

}