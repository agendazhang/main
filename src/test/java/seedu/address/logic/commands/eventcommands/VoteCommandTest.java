package seedu.address.logic.commands.eventcommands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalEvents.getTypicalAddressBook;

import org.junit.Test;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;
import seedu.address.testutil.EventBuilder;
import seedu.address.testutil.PersonBuilder;

public class VoteCommandTest {
    private static Index INDEX = Index.fromOneBased(1);
    private static String OPTION_NAME = "Generic option";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_acceptedVoteOption() {
        VoteCommand command = new VoteCommand(INDEX, OPTION_NAME);
        Person user = new PersonBuilder().build();
        commandHistory.setSelectedPerson(user);
        EventBuilder eventBuilder = new EventBuilder();
        eventBuilder.withOrganiser(user);
        Event event = eventBuilder.withPoll().build();
        event.getPoll(INDEX).addOption(OPTION_NAME);
        commandHistory.setSelectedEvent(event);
        String expectedMessage = String.format(command.MESSAGE_SUCCESS, OPTION_NAME, INDEX.getOneBased());
        expectedModel.commitAddressBook();
        expectedModel.updateEvent(event, event);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noEventVoteOption() {
        VoteCommand command = new VoteCommand(INDEX, OPTION_NAME);
        String expectedMessage = String.format(Messages.MESSAGE_NO_EVENT_SELECTED);
        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_noPollVoteOption() {
        VoteCommand command = new VoteCommand(INDEX, OPTION_NAME);
        Person user = new PersonBuilder().build();
        commandHistory.setSelectedPerson(user);
        EventBuilder eventBuilder = new EventBuilder();
        eventBuilder.withOrganiser(user);
        Event event = eventBuilder.build();
        commandHistory.setSelectedEvent(event);
        String expectedMessage = String.format(Messages.MESSAGE_NO_POLL_AT_INDEX);
        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_noOptionVoteOption() {
        VoteCommand command = new VoteCommand(INDEX, OPTION_NAME);
        Person user = new PersonBuilder().build();
        commandHistory.setSelectedPerson(user);
        EventBuilder eventBuilder = new EventBuilder();
        eventBuilder.withOrganiser(user);
        Event event = eventBuilder.withPoll().build();
        commandHistory.setSelectedEvent(event);
        String expectedMessage = String.format(Messages.MESSAGE_NO_SUCH_OPTION);
        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_noUserVoteOption() {
        VoteCommand command = new VoteCommand(INDEX, OPTION_NAME);
        EventBuilder eventBuilder = new EventBuilder();
        Event event = eventBuilder.withPoll().build();
        commandHistory.setSelectedEvent(event);
        String expectedMessage = String.format(Messages.MESSAGE_NO_USER_LOGGED_IN);
        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }
}
