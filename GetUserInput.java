import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class GetUserInput {
    private String DEFAULT_WHITELIST = "[^0-9A-Za-z]";
    private String inputType;
    private Scanner scan;
    private static String[] allowed = {"fileexist", "dir", "direxist", "stringint", "string"};
    private static List<String> ALLOWEDTYPES = new ArrayList<>(Arrays.asList(allowed));

    public GetUserInput() {

    }

    public String requestInput(String inputType) throws InvalidTypeException {
        this.inputType = inputType.toLowerCase();
        if (!(ALLOWEDTYPES.contains(this.inputType))) {
            throw new InvalidTypeException(this.inputType + " is not a valid input type. Only these types are allowed: ", ALLOWEDTYPES);
        }
        this.scan = new Scanner(System.in);
        String userInput = this.scan.nextLine();
        userInput = checkUserInput(userInput);
        return userInput;
    }

    private String reAskInput() {
        System.out.println("Improper input. Try again:");
        return this.scan.nextLine();
    }

    private String checkUserInput(String userInput) {
        boolean success = false;
        while (!success) {
            userInput = userInput.trim();
            if (this.inputType.equals("dir")) {
                File f = new File(userInput);
                if (f.isDirectory()) {
                    success = true;
                } else {
                    userInput = reAskInput();
                }
            }
            if (this.inputType.equals("direxist")) {
                File f = new File(userInput);
                if (f.exists() && f.isDirectory()) {
                    success = true;
                } else {
                    userInput = reAskInput();
                }
            }
            if (this.inputType.equals("fileexist")) {
                File f = new File(userInput);
                if (f.exists() && f.isFile()) {
                    success = true;
                } else {
                    userInput = reAskInput();
                }
            }
            if (this.inputType.equals("stringint")) {
                try{
                    int i = Integer.parseInt(userInput);
                    userInput = String.valueOf(i);
                    success = true;
                }
                catch (NumberFormatException nfe)
                {
                    userInput = reAskInput();
                }
            }
            if (this.inputType.equals("string")){
                success = true;
            }
        }
        return userInput;
    }
}