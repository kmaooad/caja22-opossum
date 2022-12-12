package edu.kmaooad.helper;

import edu.kmaooad.constants.bot.GlobalConstants;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.constants.bot.GroupTemplateConstants;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class, allows to build keyboards for users
 */
@Component
public class KeyboardHelper {

    public ReplyKeyboardMarkup buildAdditionalActions(List<String> actions) {
        List<KeyboardButton> buttons = new ArrayList<>();

        for (String action : actions) {
            buttons.add(new KeyboardButton(action));
        }
        KeyboardRow row1 = new KeyboardRow(buttons);

        KeyboardRow row2 = new KeyboardRow(List.of(new KeyboardButton(GlobalConstants.CANCEL_BUTTON_LABEL)));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildMainMenu() {
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(GlobalConstants.GROUP_BUTTON_LABEL);
        keyboardRow1.add(GlobalConstants.GROUP_TEMPLATES_BUTTON_LABEL);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(GlobalConstants.STUDENTS_BUTTON_LABEL);
        keyboardRow2.add(GlobalConstants.ACTIVITIES_BUTTON_LABEL);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(GlobalConstants.GROUP_SHOW_ALL_ASSIGN_LABEL);
        keyboardRow3.add(GlobalConstants.STUDENT_SHOW_ALL_ASSIGN_LABEL);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow1, keyboardRow2, keyboardRow3))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildMenuWithCancel() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(GlobalConstants.CANCEL_BUTTON_LABEL);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildGroupMenuWithCancel() {
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(GroupConstants.GROUP_ADD_BUTTON_LABEL);
        keyboardRow1.add(GroupConstants.GROUP_EDIT_BUTTON_LABEL);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(GroupConstants.GROUP_DELETE_BUTTON_LABEL);
        keyboardRow2.add(GroupConstants.GROUP_SHOW_ALL_BUTTON_LABEL);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(GroupConstants.GROUP_SHOW_ALL_ASSIGN_LABEL);

        KeyboardRow keyboardRow4 = new KeyboardRow();
        keyboardRow4.add(GlobalConstants.CANCEL_BUTTON_LABEL);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow1, keyboardRow2, keyboardRow3, keyboardRow4))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildGroupTemplateMenuWithCancel(){
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(GroupTemplateConstants.GROUP_TEMPLATE_ADD_BUTTON_LABEL);
        keyboardRow1.add(GroupTemplateConstants.GROUP_TEMPLATE_EDIT_BUTTON_LABEL);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(GroupTemplateConstants.GROUP_TEMPLATE_DELETE_BUTTON_LABEL);
        keyboardRow2.add(GroupTemplateConstants.GROUP_TEMPLATE_SHOW_ALL_BUTTON_LABEL);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(GlobalConstants.CANCEL_BUTTON_LABEL);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow1, keyboardRow2, keyboardRow3))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildAdditionalActionsVertical(List<String> options) {

        List<KeyboardRow> rows = options.stream()
                .map(option -> new KeyboardRow(List.of(new KeyboardButton(option))))
                .collect(Collectors.toList());

        KeyboardRow cancelRow = new KeyboardRow();
        cancelRow.add(GlobalConstants.CANCEL_BUTTON_LABEL);
        rows.add(0, cancelRow);

        return ReplyKeyboardMarkup.builder()
                .keyboard(rows)
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }
}
