package cryptotracker.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/** The class describing a cryptocurrency in a portfolio
 * 
 */
public class Cryptocurrency {
    
    private int id;
    private String name;
    private Portfolio portfolio;
    private List<CryptoBatch> batches;
    
    public Cryptocurrency(int id, String name, Portfolio portfolio) {
        this.id = id;
        this.name = name;
        this.portfolio = portfolio;
        this.batches = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Portfolio getPortfolio() {
        return portfolio;
    }
    
    public List<CryptoBatch> getBatches() {
        return batches;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cryptocurrency)) {
            return false;
        }
        Cryptocurrency other = (Cryptocurrency) object;
        return this.name.equals(other.getName());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
        hash = 67 * hash + Objects.hashCode(this.name);
        return hash;
    }
    
}
