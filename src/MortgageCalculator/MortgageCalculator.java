/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MortgageCalculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Fred
 */
public class MortgageCalculator {

    public static List<MortgageParameter> mortgageParams = new ArrayList<>();
    public static final String DELIMITER = "-----------------------------";
    
    /**
     * 
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws Exception {
        MortgageCalculator calculator = new MortgageCalculator();
        calculator.run(args);
    }
    
    public void run (String[] args) throws Exception
    {
        GettingCalculationInputFromConsole();
        CalculateData();
    }
    
    /**
     * 
     * @throws Exception 
     */
    private void GettingCalculationInputFromConsole() throws Exception {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to mortage calculator");
        PrintDelimiter();
        System.out.println("Please input total price (in unit of $10,000)");
        double totalPrice = 10000 * Double.parseDouble(reader.readLine());
        PrintDelimiter();
        System.out.println("Please input down payment (in unit of $10,000)");
        double downPayment = 10000 * Double.parseDouble(reader.readLine());
        PrintDelimiter();
        System.out.println("Please input loan length (in year)");
        int loanMonthLength = 12 * Integer.parseInt(reader.readLine());
        PrintDelimiter();
        System.out.println("Please input APR, e.g. 5% please input 5");
        System.out.println("If you want to compare multiple APR, input APRs with ';' in between");
        List<Double> aprs = Arrays.asList(reader.readLine().split(";"))
                .stream()
                .map(apr -> {return Double.parseDouble(apr)/(double)100;})
                .collect(Collectors.toList());
        PrintDelimiter();
        System.out.println("Please input closing cost (in unit of $10,000)");
        List<Double> ccs = Arrays.asList(reader.readLine().split(";"))
                .stream()
                .map(cc -> {return 10000 * Double.parseDouble(cc);})
                .collect(Collectors.toList());
        PrintDelimiter();
        System.out.println("Please input expected owning length before selling (in year)");
        int ownMonthLength = 12 * Integer.parseInt(reader.readLine());

        aprs.forEach(
                apr -> {
                    mortgageParams.add(
                            new MortgageParameter(totalPrice, downPayment, loanMonthLength, apr, ccs.get(aprs.indexOf(apr)), ownMonthLength)
                    );
                }
        );
    }
    
    private void CalculateData() throws Exception {
        mortgageParams.forEach(param -> { 
            param.PopulateMortgageParameters();
            SumAndPrint(param);
        });
        
    }
    
    private static void SumAndPrint(MortgageParameter param) {
        double totalPaymentToDate = param.closingCost + param.downPayment + param.monthlyPayment * param.ownMonthLength;
        double totalPrincipalPaymentToDate = param.GetPrincipalPaymentSum();
        double totalInterestPaymentToDate = param.GetInterestPaymentSum();
        double totalPrincipalLeft = param.totalPrice - param.downPayment - totalPrincipalPaymentToDate;
        double owningCost = totalPaymentToDate - (param.totalPrice - totalPrincipalLeft);
        
        PrintDelimiter();
        System.out.println("APR Rate: " + new DecimalFormat("##.######").format(param.apr));
        System.out.println("Total Paid: " + new DecimalFormat("##.##").format(totalPaymentToDate));
        System.out.println("  -- Down Payment: " + new DecimalFormat("##.##").format(param.downPayment));
        System.out.println("  -- Total Principal: " + new DecimalFormat("##.##").format(totalPrincipalPaymentToDate));
        System.out.println("  -- Total Interest: " + new DecimalFormat("##.##").format(totalInterestPaymentToDate));
        System.out.println("Total Principal left: " + new DecimalFormat("##.##").format(totalPrincipalLeft));
        System.out.println("Assume selling at purchase price, owning cost: " + new DecimalFormat("##.##").format(owningCost));
        PrintDelimiter();
    }
    
    private static void PrintDelimiter() {
        System.out.println(DELIMITER);
    }
}
