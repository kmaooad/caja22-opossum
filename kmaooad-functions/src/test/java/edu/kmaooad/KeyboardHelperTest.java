package edu.kmaooad;

import edu.kmaooad.helper.KeyboardHelper;
import org.junit.Test;
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

        assertTrue(result.getKeyboard().get(0).contains("Студенти"));
    }

    @Test
    public void buildMainMenuWithCancelTestContainsCancelButton() {
        ReplyKeyboardMarkup result = keyboardHelper.buildMenuWithCancel();

        assertEquals(result.getKeyboard().get(0).get(0).getText(), "❌ Скасувати");
    }
}
