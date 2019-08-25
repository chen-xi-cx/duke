import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Duke {
    private ArrayList<Task> myList;

    public Duke() {
        myList = new ArrayList<Task>();
    }

    private void run() {
        boolean canEnd;

        try {
            TaskFileReader reader = new TaskFileReader();
            reader.loadTaskContents("data/duke.txt", myList);
        } catch (FileNotFoundException e) {
        }
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Hello! I'm Duke\nWhat can I do for you?");
        canEnd = false;
        while (!canEnd) {
            String input;
            input = myScanner.nextLine();
            if (isDone(input) || isRemove(input)) {
                continue;
            }
            switch (input) {
            case "bye":
                System.out.println("Bye. Hope to see you again soon!");
                canEnd = true;
                break;
            case "list":
                System.out.println("Here are the tasks in your list");
                for (int i = 0; i < myList.size(); i = i + 1) {
                    int number = i + 1;
                    System.out.println(number + "." + myList.get(i));
                }
                break;
            default:
                try {
                    processCommandType(input);
                } catch (DukeException e) {
                    System.out.println("\u2639 OOPS!!! " + e.getMessage());
                }
                break;
            }
        }
        TaskFileWriter writer = new TaskFileWriter();
        try {
            writer.writeToFile("data/duke.txt", myList);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns true if input is of the form "delete <int>"
     * @param input
     */
    private boolean isRemove(String input) {
        String[] keywords = input.split(" ");
        if (keywords[0].equals("delete") && keywords.length == 2) {
            Scanner s = new Scanner(keywords[1]);
            if (s.hasNextInt()) {
                int index = s.nextInt() - 1;
                if (index >=0 && index < myList.size()) {
                    System.out.println("Noted. I've removed this task:");
                    System.out.println(myList.get(index));
                    myList.remove(index);
                    System.out.println("Now you have " + myList.size());
                }
                else {
                    System.out.println("Please enter a valid number");
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if input is of the form "done <int>"
     * @param input
     */
    private boolean isDone(String input) {
        String[] keywords = input.split(" ");
        if (keywords[0].equals("done") && keywords.length == 2) {
            Scanner s = new Scanner(keywords[1]);
            if (s.hasNextInt()) {
                int index = s.nextInt() - 1;
                if (index >=0 && index < myList.size()) {
                    myList.get(index).setStatus(true);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("[" + myList.get(index).getStatusIcon()
                            + "]" + myList.get(index).getDescription());
                }
                else {
                    System.out.println("Please enter a valid number");
                }
                return true;
            }
        }
        return false;
    }

    private void processCommandType(String input) throws DukeException {
        String[] type = new String[]{"todo", "deadline", "event"};
        String[] date = new String[]{"", "/by", "/at"};
        boolean enterLoop = false;
        for (int i = 0; i < type.length; i++) {
            int startIdx = input.indexOf(type[i]);
            int endIdx = input.indexOf(date[i]);
            if (input.trim().equals("todo")
                    || (input.split(" ")[0].equals("deadline") && endIdx == -1 && i == 1)
                    || (input.split(" ")[0].equals("event") && endIdx == -1 && i == 2)) {
                throw new DukeException(input);
            }
            if ((startIdx != -1 && i == 1)
                    || (startIdx != -1 && endIdx != -1)) {
                System.out.println("Got it. I've added this task:");
                enterLoop = true;
            } else if (i == 2 && endIdx == -1) {
                throw new DukeException(input);
            }
            if (!enterLoop) {
                continue;
            }
            if (i == 0) {
                myList.add(new ToDo(input.substring(type[i].length())));
            } else if (i == 1) {
                myList.add(new Deadline(input.substring(type[i].length(),endIdx), input.substring(endIdx
                        + date[i].length() + 1)));
            } else {
                myList.add(new Event(input.substring(type[i].length(),endIdx), input.substring(endIdx
                        + date[i].length() + 1)));
            }
            System.out.println(myList.get(myList.size()-1));
            System.out.println("Now you have " + myList.size()
                    + " tasks in the list.");
            break;
        }
    }

    public static void main(String[] args) {
        Duke duke = new Duke();
        duke.run();
    }
}
