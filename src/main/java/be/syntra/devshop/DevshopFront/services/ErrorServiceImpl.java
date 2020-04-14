package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.dtos.ErrorDto;
import org.springframework.stereotype.Service;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Service
public class ErrorServiceImpl implements ErrorService {

    @Override
    public ErrorDto determineError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorCode = "nullpointer on status";
        String message = errorCode;

        if (null != status) {
            switch (status.toString()) {
                case "400":
                    errorCode = status.toString() + " BAD REQUEST";
                    message = "We had trouble fetching what you wanted...";
                    break;
                case "404":
                    errorCode = status.toString() + " NOT FOUND";
                    message = "We couldn't find what you're searching for...";
                    break;
                default:
                    errorCode = "";
                    message = "Something went wrong...";
            }
        }
        return ErrorDto.builder()
                .errorCode(errorCode)
                .message(message)
                .build();
    }
}
