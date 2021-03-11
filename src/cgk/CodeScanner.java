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
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException ignored) {}
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


    private interface IODeviceHandler {
        BlockingQueue<Integer> inputQueue = new ArrayBlockingQueue<>(1);
        default int listenForInput() throws InterruptedException {
            inputQueue.clear();
            int input = inputQueue.take();
            inputQueue.add(-1);
            return input;
        }

        default void provideInput(int input) throws IllegalStateException {
            inputQueue.add(input);

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
                    }catch (IllegalStateException | NumberFormatException ignore){
                        System.out.print(String.format("\033[%dA", 1)); // Move up
                        System.out.print("\033[2K"); //clear line
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.start();
        }
    }
}
