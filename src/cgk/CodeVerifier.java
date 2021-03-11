package cgk;

import java.util.Random;
import java.util.concurrent.TimeoutException;

public class CodeVerifier{
    public enum Responses{
        VALID,
        INVALID,
        ADMIN;
    }

    private CodeStorageManager codeStorageManager;
    private CodeManager codeManager;
    private OutputScreenOperator outputScreenOperator;

    public CodeVerifier(OutputScreenOperator oso, CodeScanner cs) {
        codeStorageManager = new CodeStorageManager();
        // Check if the admin code already exists and show a new one if it does not
        generateAdminCode(oso);
        codeManager = new CodeManager(cs, oso, codeStorageManager);
    }

    public Responses verifyCode(int code) throws TimeoutException {
        if (code == codeStorageManager.getCurrentCode(CodeStorageManager.CodeType.ADMIN)) {
            codeManager.handleAdmin();
            return Responses.ADMIN;
        } else if (code == codeStorageManager.getCurrentCode(CodeStorageManager.CodeType.GENERAL) ||
                   code == codeStorageManager.getCurrentCode(CodeStorageManager.CodeType.PUBLIC_SERVICE)) {
            return Responses.VALID;
        } else {
            return Responses.INVALID;
        }
    }

    /**
     * Initializes admin code if one does not exist
     * @param oso the Output Screen Operator
     */
    private void generateAdminCode(OutputScreenOperator oso){
        int adminCode = codeStorageManager.getCurrentCode(CodeStorageManager.CodeType.ADMIN);
        if(adminCode == -1){
            Random rando = new Random();
            int newCode = (int) (rando.nextDouble() * 1000 + 1000);
            codeStorageManager.saveCode(CodeStorageManager.CodeType.ADMIN, newCode);
            oso.print("Admin code: " + newCode);
            try{
                Thread.sleep(2000);
            } catch(InterruptedException ignored){}
        }
    }
}
