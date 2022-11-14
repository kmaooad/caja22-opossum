package edu.kmaooad.helper;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class, allows to build keyboards for users
 */
@Component
public class KeyboardHelper {

    public ReplyKeyboardMarkup buildAdditionalActions(List<String> actions) {
        List<KeyboardButton> buttons = new ArrayList<>();

        for (String action: actions) {
            buttons.add(new KeyboardButton(action));
        }
        KeyboardRow row1 = new KeyboardRow(buttons);

        KeyboardRow row2 = new KeyboardRow(List.of(new KeyboardButton("❌ Скасувати")));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildMainMenu() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Студенти");
//        keyboardRow.add("Студенти");
//        keyboardRow.add("Інше...");
//

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildMenuWithCancel() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("❌ Скасувати");

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }
}
