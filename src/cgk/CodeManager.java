package cgk;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

public class CodeManager{

    private CodeScanner codeScanner;
    private OutputScreenOperator outputScreenOperator;
    private CodeStorageManager codeStorageManager;

    public CodeManager(CodeScanner codeScanner,
                       OutputScreenOperator outputScreenOperator,
                       CodeStorageManager codeStorageManager) {
        this.codeScanner = codeScanner;
        this.outputScreenOperator = outputScreenOperator;
        this.codeStorageManager = codeStorageManager;
    }
    public void handleAdmin() throws TimeoutException {
        int input;
        do {
            outputScreenOperator.print("1 User; 2 PS");
            input = codeScanner.scanCode(1);
        } while (input != 1 && input !=2);

        if(input == 1) {

            do {
                outputScreenOperator.print("Enter code");
                input = codeScanner.scanCode(4);
            } while (matchesPreviousCodes(input));
            codeStorageManager.saveCode(CodeStorageManager.CodeType.GENERAL, input);
            outputScreenOperator.print("Updated");
            sleep();
        }
        else if(input == 2){
            do {
                outputScreenOperator.print("Enter code");
                input = codeScanner.scanCode(4);
            } while (matchesPreviousCodes(input));
            codeStorageManager.saveCode(CodeStorageManager.CodeType.PUBLIC_SERVICE, input);
            outputScreenOperator.print("Updated");
            sleep();
        }
    }

    public boolean matchesPreviousCodes(int input) {
        Queue<Integer> codes = new LinkedList<>();

        codes = codeStorageManager.getCodes(CodeStorageManager.CodeType.GENERAL);

        for(Integer item : codes)
            if(item == input) return true;

        codes = codeStorageManager.getCodes(CodeStorageManager.CodeType.PUBLIC_SERVICE);

        for(Integer item : codes)
            if(item == input) return true;


        codes = codeStorageManager.getCodes(CodeStorageManager.CodeType.ADMIN);

        for(Integer item : codes)
            if(item == input) return true;


        return false;
    }

      private void sleep(){
        try {
            TimeUnit.MILLISECONDS.sleep(800);
        } catch (InterruptedException ignored) {}
    }

}
