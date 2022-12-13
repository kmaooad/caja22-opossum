package edu.kmaooad.handler.impl.student;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.MassStudentsService;
import edu.kmaooad.service.ServiceCSVException;
import edu.kmaooad.service.StudentService;
import edu.kmaooad.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static edu.kmaooad.constants.bot.StudentConstants.STUDENT_CSV_FORMAT_EXAMPLE;

@Component
@Slf4j
public class GetAllStudentsCSVButtonHandler implements ButtonRequestHandler {
    private final StudentService studentService;

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final StudentButtonsHandler studentButtonsHandler;
    private final MassStudentsService massStudentsService;

    public GetAllStudentsCSVButtonHandler(StudentService studentService, MassStudentsService massStudentsService, TelegramService telegramService, KeyboardHelper keyboardHelper, StudentButtonsHandler studentButtonsHandler) {
        this.studentService = studentService;
        this.massStudentsService = massStudentsService;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.studentButtonsHandler = studentButtonsHandler;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return request.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_STUDENT_CSV);
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        log.info("Getting students CSV");
        String message = dispatchRequest.getUpdate().getMessage().getText();
        try {
            massStudentsService.replaceAllStudents(massStudentsService.parseStudentCSV(message));
        } catch (ServiceCSVException e) {
            log.warn("Catched " + e);
            dispatchRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_STUDENT_CSV);
            return notifyUserAboutError(dispatchRequest.getChatId(), e);
        } catch (Exception e) {
            log.error(e.getMessage());
            dispatchRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_STUDENT_CSV);
            return new HandlerResponse(telegramService.sendMessage(dispatchRequest.getChatId(),
                    "Невідома помилка", keyboardHelper.buildMenuWithCancel()), true);
        }
        dispatchRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
        return studentButtonsHandler.handle(dispatchRequest);
    }

    public HandlerResponse notifyUserAboutError(Long chatId, ServiceCSVException e) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildStudentMenuWithCancel();
        switch (e.getErrorType()) {
            case EMAIL_DUPLICATE:
                telegramService.sendMessage(chatId, "Дублікація імейлу \n" + e.getLine());
                return new HandlerResponse(telegramService.sendMessage(chatId, STUDENT_CSV_FORMAT_EXAMPLE, replyKeyboardMarkup), true);
            case ACTIVITY_DOESNT_EXIST:
                telegramService.sendMessage(chatId, "Такої активності не існує \n" + e.getLine());
                return new HandlerResponse(telegramService.sendMessage(chatId, STUDENT_CSV_FORMAT_EXAMPLE, replyKeyboardMarkup), true);
            case NOT_ENOUGH_PARAMS_ON_LINE:
                telegramService.sendMessage(chatId, "Не вистачає параметрів імейлу \n" + e.getLine());
                return new HandlerResponse(telegramService.sendMessage(chatId, STUDENT_CSV_FORMAT_EXAMPLE, replyKeyboardMarkup), true);
            default:
                telegramService.sendMessage(chatId, "Невідома помилка на лінії \n" + e.getLine());
        }
        return new HandlerResponse(telegramService.sendMessage(chatId, STUDENT_CSV_FORMAT_EXAMPLE, replyKeyboardMarkup), true);
    }
}