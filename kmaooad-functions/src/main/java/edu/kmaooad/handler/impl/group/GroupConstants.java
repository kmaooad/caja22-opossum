package edu.kmaooad.handler.impl.group;

public class GroupConstants {
    public static final String GROUP_MAP_KEY = "group";

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

    public static final String WRONG_GRADE = "Курс <b>%s</b> неправильний.\nСпробуйте ще раз.";
    public static final String WRONG_YEAR = "Рік <b>%s</b> неправильний.\nСпробуйте ще раз.";

}
