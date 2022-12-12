package edu.kmaooad.constants.bot;

public class GroupTemplateConstants {
    public static final String GROUP_TEMPLATE_MAP_KEY = "group template";

    public static final String GROUP_TEMPLATE_EDIT_BUTTON_LABEL = "Редагувати";
    public static final String GROUP_TEMPLATE_DELETE_BUTTON_LABEL = "Видалити";
    public static final String GROUP_TEMPLATE_ADD_BUTTON_LABEL = "Додати";
    public static final String GROUP_TEMPLATE_SHOW_ALL_BUTTON_LABEL = "Показати всі шаблони груп";

    public static final String SHOW_GROUP_TEMPLATE = "Group template:\n<b>%s %s %s</b>";
    public static final String SHOW_GROUP_TEMPLATE_WITH_ID = "Group template:\n<b>%s %s %s</b>\nid = <b>%s</b>";

    public static final String ASK_FOR_GROUP_TEMPLATE_ID = "Вкажіть ідентифікатор шаблону групи.";
    public static final String ASK_FOR_GROUP_TEMPLATE_NAME = "Вкажіть назву шаблону групи.";
    public static final String ASK_FOR_GROUP_TEMPLATE_GRADE = "Вкажіть курс шаблону групи.";
    public static final String ASK_FOR_GROUP_TEMPLATE_YEAR = "Вкажіть рік шаблону групи.";

    public static final String TEMPLATE_SUCCESSFULLY_ADDED = "Шаблон успішно додано";
    public static final String TEMPLATE_SUCCESSFULLY_UPDATED = "Шаблон успішно відредаговано";
    public static final String TEMPLATE_SUCCESSFULLY_DELETED = "Шаблон успішно видалено";

    public static final String WRONG_GRADE = "Курс <b>%s</b> неправильний.\nСпробуйте ще раз.";
    public static final String WRONG_YEAR = "Рік <b>%s</b> неправильний.\nСпробуйте ще раз.";

    public static final String CANNOT_CREATE_EMPTY_TEMPLATE = "Не можна створити порожній шаблон для груп. Потрібно вказати хоча би одне поле.";
    public static final String CANNOT_UPDATE_TEMPLATE_EXISTS = "Неможливо оновити шаблон. Інший шаблон з такими даними вже існує.";
    public static final String CANNOT_GET_TEMPLATE = "Шаблону з таким ідентифікатором не існує. Спробуйте ще раз.";
    public static final String CANNOT_DELETE_TEMPLATE_NOT_EXISTS = "Неможливо видалити шаблон. Шаблону з таким ідентифікатором не існує.";
}
