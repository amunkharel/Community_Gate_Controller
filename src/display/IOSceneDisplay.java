package display;

public class IOSceneDisplay {
    private static IOSceneDisplay ioSceneDisplayInstance;
    private final int displayLength = 30;
    private String currentMessage = "";
    private  final String[] closedGate = {"||-----|\\/|-----||",
                                      "||-----|\\/|-----||",
                                      "||-----|\\/|-----||",
                                      "||-----|\\/|-----||"};
    private  final String[] openedGate ={"||_--/|    |\\--_||",
                                   "||_--/|    |\\--_||",
                                   "||_--/|    |\\--_||",
                                   "||--/|      |\\--||"};
    private String[] currentGate = closedGate;

    /**
     * Singleton IO Scene Display
     */
    private IOSceneDisplay(){

    }

    public static IOSceneDisplay getInstance(){
        if(ioSceneDisplayInstance == null) ioSceneDisplayInstance = new IOSceneDisplay();
        return ioSceneDisplayInstance;
    }

    /**
     * Updates the display with a message, resets gate ascii art accordingly.
     * @param message code/message
     */
    public void updateMessage(String message) {
        if(!message.equals("Welcome")) currentGate = closedGate;
        currentMessage = message;
        updateDisplay();
    }


    /**
     * Set opened gate ascii art and refresh display.
     */
    public void openGate(){
        currentGate = openedGate;
        updateDisplay();
    }



    private void updateDisplay(){
        rollbackDisplay();
        System.out.println();
        System.out.println("  _______   " + repeatChar(' ',displayLength+1) + repeatChar(' ',5)+currentGate[0]);
        System.out.println("  |1|2|3|  |"+ repeatChar('=',displayLength)+"|"+ repeatChar(' ',5)+currentGate[1]);
        System.out.print("  |4|5|6|  |");
        System.out.print(currentMessage + (repeatChar(' ',displayLength - currentMessage.length())) + "|");
        System.out.print(repeatChar(' ',5)+currentGate[3]+"\n");
        System.out.println("  |7|8|9|  |"+ repeatChar('=',displayLength)+"|"+ repeatChar(' ',5)+currentGate[3]);
        System.out.println("    |0|");
    }

    private void rollbackDisplay(){
        try {
            for (int i = 0; i < 6; i++) {
                System.out.print(String.format("\033[%dA", 1)); // Move up
                System.out.print("\033[2K"); //clear line
            }
            System.out.print(String.format("\033[%dA", 1));
        }catch (Exception ignore){}
    }

    private String repeatChar(char c, int length){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<length; i++){
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {

    }

}
