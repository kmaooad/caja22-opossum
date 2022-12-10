package edu.kmaooad.constants.bot;

public class GroupTemplateConstants {
    public static final String GROUP_TEMPLATE_MAP_KEY = "group template";

    public static final String GROUP_TEMPLATE_EDIT_BUTTON_LABEL = "Редагувати";
    public static final String GROUP_TEMPLATE_DELETE_BUTTON_LABEL = "Видалити";
    public static final String GROUP_TEMPLATE_ADD_BUTTON_LABEL = "Додати";
    public static final String GROUP_TEMPLATE_SHOW_ALL_BUTTON_LABEL = "Показати всі шаблони груп";

    public static final String SHOW_GROUP_TEMPLATE = "<b>%s</b>\n" +
            "Grade: %s\n" +
            "Year: %s";

    public static final String SHOW_GROUP_TEMPLATE_WITH_ID = "<b>%s</b>\n" +
            "Id: %s\n" +
            "Grade: %s\n" +
            "Year: %s";

    public static final String ASK_FOR_GROUP_TEMPLATE_ID = "Вкажіть ідентифікатор шаблону групи.";
    public static final String ASK_FOR_GROUP_TEMPLATE_NAME = "Вкажіть назву шаблону групи.";
    public static final String ASK_FOR_GROUP_TEMPLATE_GRADE = "Вкажіть курс шаблону групи.";
    public static final String ASK_FOR_GROUP_TEMPLATE_YEAR = "Вкажіть рік шаблону групи.";

    public static final String WRONG_GRADE = "Курс <b>%s</b> неправильний.\nСпробуйте ще раз.";
    public static final String WRONG_YEAR = "Рік <b>%s</b> неправильний.\nСпробуйте ще раз.";

    public static final String CANNOT_CREATE_EMPTY_TEMPLATE = "Не можна створити порожній шаблон для груп. Потрібно вказати хоча би одне поле.";
}
