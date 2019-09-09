package command;

import converter.StringDateConverter;
import storage.Storage;
import task.Event;
import task.TaskList;
import ui.Ui;

import java.text.ParseException;
import java.util.Date;

/**
 * Represent an event to be added.
 */
public class EventCommand extends Command {
    private String[] arguments;

    /**
     * Initializes EventCommand with event description and date of event.
     *
     * @param arguments contains event description and date of event
     */
    public EventCommand(String[] arguments) {
        this.arguments = arguments;
    }

    /**
     * Adds event to task list.
     * Print messages to notify users event has
     * been added to task list.
     *
     * @param tasks contains task list
     * @param ui deals with interaction with users
     * @param storage deals with loading and saving of task list
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        try {
            StringDateConverter converter = new StringDateConverter();
            Date at = converter.convertStringToDate(arguments[1]);
            tasks.getTasks().add(new Event(arguments[0], at));
            ui.showEventCommand(tasks);
        } catch (ParseException e) {
            ui.showLoadingError("Please enter a valid date.");
        }
    }
}
