package cryptotracker.domain;

import java.time.LocalDate;


/** The class describing a single purchase of a cryptocurrency
 * 
 */
public class CryptoBatch {
    
    private int id;
    private int amount;
    private int totalPaid;
    private LocalDate date;
    private Cryptocurrency crypto;
    
    public CryptoBatch(int id, int amount, int totalPaid, LocalDate date, Cryptocurrency crypto) {
        this.id = id;
        this.amount = amount;
        this.totalPaid = totalPaid;
        this.date = date;
        this.crypto = crypto;
    }
    
    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public int getTotalPaid() {
        return totalPaid;
    }

    public LocalDate getDate() {
        return date;
    }
    
    public Cryptocurrency getCrypto() {
        return crypto;
    }
    
    public double getExchangeRate() {
        return totalPaid / amount;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CryptoBatch)) {
            return false;
        }
        
        CryptoBatch other = (CryptoBatch) object;
        return this.id == other.getId();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id;
        return hash;
    }
}
