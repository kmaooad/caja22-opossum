package edu.kmaooad.handler;

import edu.kmaooad.constants.bot.GlobalConstants;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.helper.KeyboardHelper;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KeyboardHelperTest {
    private static final KeyboardHelper keyboardHelper = new KeyboardHelper();

    @Test
    public void buildAdditionalActionsTestConstructingKeyboardWithProperValue() {
        String keyName = "action";

        ReplyKeyboardMarkup result = keyboardHelper.buildAdditionalActions(List.of(keyName));

        assertEquals(result.getKeyboard().get(0).get(0).getText(), keyName);
    }

    @Test
    public void buildMainMenuTestContainsStudentsButton() {
        ReplyKeyboardMarkup result = keyboardHelper.buildMainMenu();

        assertTrue(result.getKeyboard().get(0).contains(GlobalConstants.GROUP_BUTTON_LABEL));
    }

    @Test
    public void buildMainMenuWithCancelTestContainsCancelButton() {
        ReplyKeyboardMarkup result = keyboardHelper.buildMenuWithCancel();

        assertEquals(result.getKeyboard().get(0).get(0).getText(), GlobalConstants.CANCEL_BUTTON_LABEL);
    }

    @Test
    public void buildGroupMenuWithFirstButtonIsAdd() {
        ReplyKeyboardMarkup result = keyboardHelper.buildGroupMenuWithCancel();

        assertEquals(result.getKeyboard().get(0).get(0).getText(), GroupConstants.GROUP_ADD_BUTTON_LABEL);
    }
}
