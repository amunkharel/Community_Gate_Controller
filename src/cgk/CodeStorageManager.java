package cgk;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class CodeStorageManager{

    public enum CodeType{
        GENERAL,
        PUBLIC_SERVICE,
        ADMIN
    }
    public int getCurrentCode(CodeType type){
        Queue<Integer> codes = new LinkedList<>();
        codes = getCodes(type);

        if(codes.isEmpty() && type == CodeType.ADMIN) {
            return - 1;
        }
        int code = 0;
        for(Integer item : codes)
            code = item;
        return code;
    }


    public void saveCode(CodeType type, int code){
        Queue<Integer> codes = new LinkedList<>();
        codes = getCodes(type);

        if(codes.size() == 3) {
            codes.remove();
        }
        codes.add(code);

        writeCodes(codes, type);
    }

    public void writeCodes(Queue<Integer> codes, CodeType type) {
        String pathname = getPathName(type);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(pathname, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Integer item : codes) {
            try {
                writer.write(String.valueOf(item));
                writer.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Queue<Integer> getCodes(CodeType type) {
        Queue<Integer> codes = new LinkedList<>();
        String pathname = getPathName(type);
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(pathname));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(scanner.hasNext()){
            codes.add(Integer.parseInt(scanner.nextLine()));

        }
        return codes;
    }

    public String getPathName(CodeType type) {
        String pathname = "resources/codes/";
        if(type == CodeType.GENERAL) {
            pathname = pathname + "General.txt";
        }

        else if(type == CodeType.PUBLIC_SERVICE) {
            pathname = pathname + "Public_Service.txt";
        }

        else if(type == CodeType.ADMIN) {
            pathname = pathname + "Admin.txt";
        }
        return pathname;
    }
}
