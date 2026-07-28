package utils;

import java.math.BigDecimal;

public class Validators {
    public static boolean isValidCNIC(String cnic) {
        // Simple validation: 13 digits with dashes
        return cnic.matches("\\d{5}-\\d{7}-\\d{1}");
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        // Accept formats like 03001234567, 0300-1234567, 0300 1234567, +923001234567
        String normalized = phone.replaceAll("[^0-9+]+", "");
        return normalized.matches("^(?:\\+92|0)3\\d{9}$");
    }

    public static boolean isValidAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean canWithdraw(BigDecimal balance, BigDecimal amount, String accountType) {
        if (accountType.equals("SAVINGS")) {
            // Minimum balance 1000 for savings
            return balance.subtract(amount).compareTo(new BigDecimal("1000")) >= 0;
        } else if (accountType.equals("CURRENT")) {
            // No minimum for current
            return balance.compareTo(amount) >= 0;
        }
        return false;
    }
}