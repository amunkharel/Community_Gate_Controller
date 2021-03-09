package cgk;

import java.util.Scanner;
import java.util.concurrent.TimeoutException;

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
    public void handleAdmin() throws TimeoutException{
        int input;
        do {
            outputScreenOperator.print("Please enter 1 to change User code and enter 2 to change Public Services code");
            input = codeScanner.scanCode(1);
        } while (input != 1 && input !=2);

        if(input == 1) {

            do {
                outputScreenOperator.print("Enter four digit new user code");
                input = codeScanner.scanCode(4);
            } while ( ! codeStorageManager.saveCode(CodeStorageManager.CodeType.GENERAL, input));
            outputScreenOperator.print("User Code Updated");
        }
        else if(input == 2){
            do {
                outputScreenOperator.print("Enter four digit new public services code");
                input = codeScanner.scanCode(4);
            } while ( ! codeStorageManager.saveCode(CodeStorageManager.CodeType.PUBLIC_SERVICE, input));
            outputScreenOperator.print("Public Services Code Updated");
        }
    }

}
