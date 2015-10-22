/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MortgageCalculator;

import java.util.HashMap;
import static MortgageCalculator.MortgageCalculator.DELIMITER;

/**
 *
 * @author Fred
 */
public class MortgageParameter {
    public double totalPrice;
    public double downPayment;
    public double mortgageAmount;
    public int loanMonthLength;

    public double apr;
    public double monthlyInterestRate;
    public double closingCost;
    public int ownMonthLength;

    //mortgage related params
    public double monthlyPayment;
    public HashMap<Integer, Double> interestPaymentMap;
    public HashMap<Integer, Double> principalPaymentMap;

    public MortgageParameter(double tp, double dp, int lml, double a, double cc, int oml) {
        totalPrice = tp;
        downPayment = dp;
        mortgageAmount = tp - dp;
        loanMonthLength = lml;
        apr = a;
        monthlyInterestRate = a/(double)12;
        closingCost = cc;
        ownMonthLength = oml;

        monthlyPayment = 0;
        interestPaymentMap = new HashMap<>();
        principalPaymentMap = new HashMap<>();
    }

    public void PopulateMortgageParameters() {
        PopulateMonthlyPayment();
        PopulateInterestPaymentMap();
        PopulatePrincipalPaymentMap();
    }

    public void PopulateMonthlyPayment() {
        monthlyPayment = (mortgageAmount * monthlyInterestRate * Math.pow((monthlyInterestRate + 1), loanMonthLength)) / (Math.pow((monthlyInterestRate + 1), loanMonthLength) - 1);
//        System.out.println("ESTIMATED MONTHLY PAYMENT: " + new DecimalFormat("##.##").format(monthlyPayment));
    }

    public void PopulateInterestPaymentMap() {
//        PrintDelimiter();
//        System.out.println("MONTH   |   INTEREST PAYMENT");
        for (int month = 1; month <= loanMonthLength; month++) {
            if (!interestPaymentMap.containsKey(month)) {
                double previousTotalInterestPayment = GetInterestPaymentSum(1, (month -1));
                double currentMonthInterestPayment = (mortgageAmount - (month - 1) * monthlyPayment + previousTotalInterestPayment) * monthlyInterestRate;

                interestPaymentMap.put(month, currentMonthInterestPayment);
//                System.out.println(month + "   |   $" + new DecimalFormat("##.##").format(currentMonthInterestPayment));
            }
        }

    }

    public void PopulatePrincipalPaymentMap() {
//        PrintDelimiter();
//        System.out.println("MONTH   |   PRINCIPAL PAYMENT");
        for (int month = 1; month <= loanMonthLength; month++) {
            if (!principalPaymentMap.containsKey(month)) {
                double currentMonthInterestPayment = interestPaymentMap.containsKey(month) ? interestPaymentMap.get(month) : 0;
                double currentPrincipalPayment = monthlyPayment - currentMonthInterestPayment;

                principalPaymentMap.put(month, currentPrincipalPayment);
//                System.out.println(month + "   |   $" + new DecimalFormat("##.##").format(currentPrincipalPayment));
            }
        }
    }
    
    public double GetInterestPaymentSum() {
        return GetInterestPaymentSum(1, this.ownMonthLength);
    }
    
    public double GetPrincipalPaymentSum() {
        return GetPrincipalPaymentSum(1, this.ownMonthLength);
    }
    
    /**
     * 
     * @param startMonth
     * @param endMonth
     * @return summedInterestPayment
     */
    private double GetInterestPaymentSum(int startMonth, int endMonth) {
        double totalInterestPayment = 0;
        
        totalInterestPayment = interestPaymentMap.entrySet().stream()
                .filter((entry) -> (entry.getKey() >= startMonth && entry.getKey() <= endMonth))
                .map((entry) -> entry.getValue()).reduce(totalInterestPayment, (accumulator, _item) -> accumulator + _item);
        
        return totalInterestPayment;
    }
    
    /**
     * 
     * @param startMonth
     * @param endMonth
     * @return 
     */
    private double GetPrincipalPaymentSum(int startMonth, int endMonth) {
        double totalPrincipalPayment = 0;
        
        totalPrincipalPayment = principalPaymentMap.entrySet().stream()
                .filter((entry) -> (entry.getKey() >= startMonth && entry.getKey() <= endMonth))
                .map((entry) -> entry.getValue()).reduce(totalPrincipalPayment, (accumulator, _item) -> accumulator + _item);
        
        return totalPrincipalPayment;
    }
    
    private static void PrintDelimiter() {
        System.out.println(DELIMITER);
    }
}
