package cryptotracker.domain;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class InputChecker {
    
    
    public boolean isInteger(String string) {
        if (string.length() < 1) {
            return false;
        }
        
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
    
    public Integer tryParseInt(String string) {
        if (isInteger(string)) {
            return Integer.parseInt(string);
        }
        
        return null;
    }
    
    public boolean isValidDate(String string) {
        if (string.length() != 10) {
            return false;
        }
        
        for (int i = 0; i < 10; i++) {
            char c = string.charAt(i);
            
            if (i != 4 && i != 7) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            } else {
                if (c != '-') {
                    return false;
                }
            }
        }
        return true;
    }
    
    public LocalDate tryParseDate(String string) {
        try {
            return LocalDate.parse(string);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
