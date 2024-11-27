import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        String result ="";
        System.out.println("Input:");
        Scanner in = new Scanner(System.in);
        String received_data = in.nextLine();
        result = calc(received_data);
        System.out.println("Output: \n" + result);
    }

    public static String calc(String input)  {
        String result = "";
        try {
            Calc.parser(input);
            if (Calc.searchException() == 1)
                result = Calc.arabicCalc();
            if (Calc.searchException() == 2)
                result = Calc.romanCalc();

        } catch (CalcException e) {
            result = e.toString();
             }
        return result;
    }
}

class Calc{
    private static int operator = 0;
    private static int operand1;
    private static int operand2;
    private static int index_operation = 0;
    private static String substr1;
    private static String substr2;
    private static String[] operation = new String[]{"+", "-", "*", "/", ""};


    private static int calculation (int operand1, int operand2, int operator){
        int res = 0;
        switch(operation[operator]){
            case "+": res = operand1 + operand2; break;
            case "-": res = operand1 - operand2; break;
            case "*": res = operand1 * operand2; break;
            case "/": res = operand1 / operand2; break;
        }
        return res;
    }

    public static void parser(String received_data) throws CalcException {
        for (int i = 0; i < operation.length; i++) {
            if (i > 3) throw new CalcException("строка не является математической операцией");
            if (received_data.indexOf(operation[i]) > 0) {
                index_operation = received_data.indexOf(operation[i]);
                operator = i;
                break;
            }
        }
        int length_received_data = received_data.length();
        substr1 = received_data.substring(0, index_operation);
        substr2 = received_data.substring(index_operation + 1, length_received_data);

        for (int i = 0; i < operation.length; i++) {
            if (substr2.indexOf(operation[i]) > 0) {
                throw new CalcException("формат математической операции не удовлетворяет заданию");
            }
        }
        substr1 = substr1.trim();  // убираем пробелы если есть
        substr2 = substr2.trim();  // убираем пробелы если есть
    }

    public static int searchException () throws CalcException {
        String[] roman = new String[] {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        String[] operand = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        byte status;
        boolean arabicStr1 = false;
        boolean arabicStr2 = false;
        boolean romanStr1 = false;
        boolean romanStr2 = false;

        for(int i = 0; i < operand.length; i++){
            if (operand[i].equals(substr1)) arabicStr1 = true;
            if (roman[i].equals(substr1)) romanStr1 = true;
            if (operand[i].equals(substr2)) arabicStr2 = true;
            if (roman[i].equals(substr2)) romanStr2 = true;
        }

        if (arabicStr1 && arabicStr2) status = 1;
        else if (romanStr1 && romanStr2) status = 2;
        else if((arabicStr1 && romanStr2) || (arabicStr2 && romanStr1)) throw new CalcException("используются одновременно разные системы счисления");
        else  throw new CalcException("введены неправильные данные");
        return status;

    }

    public static String romanCalc() throws CalcException{
        String[] roman1 = new String[]{" ", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        String[] roman2 = new String[]{" ", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC", "C"};
        int res;
        for (operand1 = 0; operand1 < roman1.length; operand1++) {
            if (roman1[operand1].equals(substr1)) break;
        }
        for (operand2 = 0; operand2 < roman1.length; operand2++) {
            if (roman1[operand2].equals(substr2)) break;
        }
        res = calculation(operand1, operand2, operator);

        if (res == 0) throw new CalcException ("в римской системе исчисления нет ноля");
        if (res < 0) throw new CalcException ("в римской системе исчисления нет отрицательных чисел");
        int one = res % 10;
        int tens =  res / 10;
        return (roman2[tens] + roman1[one]);
    }

    public static String arabicCalc(){
        int res;
        operand1 = Integer.parseInt(substr1);
        operand2 = Integer.parseInt(substr2);
        res = calculation(operand1, operand2, operator);
        return Integer.toString(res);
    }
}

class CalcException extends Exception {
    public CalcException(String message) {
        super(message);
    }
}