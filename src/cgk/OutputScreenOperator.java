package cgk;

import display.IOSceneDisplay;

public class OutputScreenOperator{
    private IOSceneDisplay ioSceneDisplay;

    public OutputScreenOperator(){
        ioSceneDisplay = IOSceneDisplay.getInstance();
    }
    public void print(String message){
        ioSceneDisplay.updateMessage(message);
    }
}
