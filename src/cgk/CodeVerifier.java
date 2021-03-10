package cgk;

import java.util.concurrent.TimeoutException;

public class CodeVerifier{
    public enum Responses{
        VALID,
        INVALID,
        ADMIN;
    }

    private CodeStorageManager codeStorageManager;
    private CodeManager codeManager;

    public CodeVerifier(OutputScreenOperator oso, CodeScanner cs) {
        codeStorageManager = new CodeStorageManager();
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
}
