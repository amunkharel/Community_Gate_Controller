package cgk;

import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;

/**
 * The main controller class for the CGK system.
 *
 * This class boots up the system, and then starts up the code scanner waiting
 * for new code entries. Upon a successful code entry, the manager sends the
 * code to CodeValidation to check if the code is a gate code or admin code.
 * If it is a correct code, the manager tells the gate controller to tell the
 * gate to open and then starts waiting for a new code input. If the code was
 * invalid, it will notify the user and ask for a new code.
 */
public class KeypadManager {
    private static CodeScanner codeScanner;
    private static OutputScreenOperator outputScreenOperator;
    private static CodeVerifier codeVerifier;
    private static GateCommunicator gateCommunicator;

    public static void main(String[] args) {
        // Spin up the sub-components
        outputScreenOperator = new OutputScreenOperator();
        codeScanner = new CodeScanner(outputScreenOperator);
        codeVerifier = new CodeVerifier(outputScreenOperator, codeScanner);
        gateCommunicator = new GateCommunicator();

        // Start the main state loop
        processingLoop();
    }

    /**
     * This method represents the state machine that is the KeypadManager and
     * manages the states and transitions between states.
     */
    private static void processingLoop(){
        int enteredCode;
        int invalidCodeAttempts = 0;
        CodeVerifier.Responses entryStatus;
        while(true){
            if(invalidCodeAttempts >= 5){
                for(int i = 10; i>0;i--) {
                    outputScreenOperator.print("Cooldown " + i);
                    try {
                        sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            outputScreenOperator.print("Enter Code");
            try{
                enteredCode = codeScanner.scanCode(4);
                entryStatus = codeVerifier.verifyCode(enteredCode);
            } catch(TimeoutException e) {
                // If there was a time out, either in the code scanner or the
                // admin menu, go back to the idle state.
                outputScreenOperator.print("Idle Timeout");
                try {
                    sleep(800);
                } catch (InterruptedException ignored) {}
                continue;
            }
            switch(entryStatus){
                case VALID:
                    invalidCodeAttempts = 0;
                    outputScreenOperator.print("Welcome");
                    gateCommunicator.openGate();
                    try {
                        sleep(3000);
                    } catch (InterruptedException ignored) {}
                    break;
                case ADMIN:
                    invalidCodeAttempts = 0;
                    // Do not need to handle the admin stuff, CodeManagement already handled it
                    break;
                case INVALID:
                    invalidCodeAttempts++;
                    outputScreenOperator.print("INVALID CODE");
                    try {
                        sleep(800);
                    } catch (InterruptedException ignored) {}
                    break;
            }
        }
    }
}
