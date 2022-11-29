package edu.kmaooad.constants.bot;

public class GroupConstants {
    public static final String GROUP_MAP_KEY = "group";

    public static final String GROUP_EDIT_BUTTON_LABEL = "Редагувати";
    public static final String GROUP_DELETE_BUTTON_LABEL = "Видалити";
    public static final String GROUP_ADD_BUTTON_LABEL = "Додати";
    public static final String GROUP_SHOW_ALL_BUTTON_LABEL = "Показати всі групи";

    /**
     * Template for full group info.
     * Arguments:
     * First — group name.
     * Second — group id.
     */
    public static final String SHOW_FULL_GROUP = "<b>%s</b>\n" +
            "Id: %s\n" +
            "Grade: %s\n" +
            "Year: %s";

    public static final String ASK_FOR_GROUP_NAME = "Вкажіть назву групи.";
    public static final String ASK_FOR_GROUP_GRADE = "Вкажіть курс групи.";
    public static final String ASK_FOR_GROUP_YEAR = "Вкажіть рік групи.";

    public static final String SUCCESSFULLY_ADDED = "Успішно додано групу.";
    public static final String ERROR_WHILE_ADD = "Не змогли додати групу.";

    public static final String WRONG_GRADE = "Курс <b>%s</b> неправильний.\nСпробуйте ще раз.";
    public static final String WRONG_YEAR = "Рік <b>%s</b> неправильний.\nСпробуйте ще раз.";
}
