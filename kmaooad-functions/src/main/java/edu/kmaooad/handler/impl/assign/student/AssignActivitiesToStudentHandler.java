package edu.kmaooad.handler.impl.assign.student;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.Activity;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.Student;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.ActivityService;
import edu.kmaooad.service.ServiceException;
import edu.kmaooad.service.StudentService;
import edu.kmaooad.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static edu.kmaooad.constants.bot.GlobalConstants.ASSIGNED;
import static edu.kmaooad.constants.bot.StudentConstants.STUDENT_MAP_KEY;

@Component
@Slf4j
public class AssignActivitiesToStudentHandler implements ButtonRequestHandler {


    private final KeyboardHelper keyboardHelper;
    private final StudentService studentService;
    private final ActivityService activityService;
    private final TelegramService telegramService;

    AssignActivitiesToStudentHandler(KeyboardHelper keyboardHelper, TelegramService telegramService,
                                     StudentService studentService, ActivityService activityService
    ) {
        this.keyboardHelper = keyboardHelper;
        this.telegramService = telegramService;
        this.studentService = studentService;
        this.activityService = activityService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserRequestHandler.isTextMessage(userRequest.getUpdate()) &&
                userRequest.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_STUDENT_ACTIVITY_ASSIGN_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest userRequest) {
        Student student = (Student) userRequest.getUserSession().getData().get(STUDENT_MAP_KEY);
        String text = userRequest.getUpdate().getMessage().getText();
        log.info("Trying to assign/unassign activity " + text + " from student");
        try {
            if (text.contains(ASSIGNED)) {
                String activityName = text.replace(ASSIGNED, "").trim();
                Activity activity = activityService.getActivityByName(activityName);
                studentService.deleteStudentActivity(activity.getId(), student.getId());
                telegramService.sendMessage(userRequest.getChatId(),
                        "Видалена активність " + activity.getName() + " із студента " + student.getFirstName() + student.getLastName());
                log.info("Deleted activity " + activityName + " from student " + student.getEmail());
            } else {
                Activity activity = activityService.getActivityByName(text);
                studentService.addStudentActivity(activity.getId(), student.getId());
                telegramService.sendMessage(userRequest.getChatId(),
                        "Додана активність " + activity.getName() + " до студента " + student.getFirstName() + student.getLastName());
                log.info("Added activity " + activity.getName() + " to group " + student.getEmail());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            log.error("Failed to delete or add activity " + text + " to student" + student.getEmail());
        }

        Student updatedStudent = studentService.getStudentByEmail(student.getEmail());

        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildAdditionalActionsVertical(activityService.getStatusOfActivitiesForStudent(updatedStudent));
        userRequest.getUserSession().getData().put(STUDENT_MAP_KEY, updatedStudent);

        return new HandlerResponse(telegramService.sendMessage(userRequest.getChatId(), "Оберіть активність яку хочете додати до " + updatedStudent.getFirstName() + " " + updatedStudent.getLastName() + " ⤵️", replyKeyboardMarkup), true);
    }
}
