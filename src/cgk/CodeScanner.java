package cgk;

import java.util.Scanner;
import java.util.concurrent.*;

public class CodeScanner {
    private final long timeout = 10;
    private final IODeviceHandler IODeviceHandler = new SystemIODevice();
    private final OutputScreenOperator outputScreenOperator;

    public CodeScanner(OutputScreenOperator outputScreenOperator) {
        this.outputScreenOperator = outputScreenOperator;
    }

    public int scanCode(int codeLength) throws TimeoutException {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<codeLength; i++){
            Integer input = getSingleInput(i > 0 || codeLength == 1);
            if(input == null) return -1;
            sb.append(input);
            outputScreenOperator.print(sb.toString().replaceAll(".","*"));
        }
        return Integer.parseInt(sb.toString());
    }

    private Integer getSingleInput(boolean isTimeoutOperation) throws TimeoutException {
        FutureTask<Integer> singleScanTask = new FutureTask<>(IODeviceHandler::listenForInput);
        Integer input = null;
        try {
            Thread thread = new Thread(singleScanTask);
            thread.setDaemon(true);
            thread.start();
            if(isTimeoutOperation) input = singleScanTask.get(timeout, TimeUnit.SECONDS);
            else input = singleScanTask.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            singleScanTask.cancel(true);
            throw new TimeoutException();
        }
        return input;
    }

    public static void main(String[] args) throws TimeoutException, InterruptedException {
        CodeScanner codeScanner = new CodeScanner(new OutputScreenOperator());
        System.out.println("CODE SCANNER");
        while(true) {
            int codeLength = 4;
            System.out.println("The Code Length is: " + codeLength);
            System.out.println("Enter one digit at a time...");
            try {
                System.out.println("Complete Code: " + codeScanner.scanCode(codeLength));
            } catch (TimeoutException  e) {
                System.out.println("ERROR: Timeout Expired - "+e+"\n Try Again...");
            }
        }
    }

    private interface IODeviceHandler {
        BlockingQueue<Integer> inputQueue = new LinkedBlockingQueue<>();
        default int listenForInput() throws InterruptedException {
            return inputQueue.take();
        }

        default void provideInput(int input) throws InterruptedException {
            inputQueue.put(input);
        }
    }

    private static class SystemIODevice implements IODeviceHandler {
        private Scanner scanner;

        public SystemIODevice() {
            Runnable runnable = () ->{
                scanner = new Scanner(System.in);
                while(true){
                    try {
                        String rawInput = scanner.nextLine();
                        if(rawInput.length()>1) throw new NumberFormatException();
                        int input = Integer.parseInt(rawInput);
                        provideInput(input);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }catch (NumberFormatException e){
                        System.out.println("Invalid Input");
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.start();
        }
    }
}
