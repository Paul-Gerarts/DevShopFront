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
            String statusCode = status.toString();
            switch (statusCode) {
                case "400":
                    errorCode = statusCode + " BAD REQUEST";
                    message = "We had trouble fetching what you wanted...";
                    break;
                case "404":
                    errorCode = statusCode + " NOT FOUND";
                    message = "We couldn't find what you're searching for...";
                    break;
                default:
                    errorCode = statusCode;
                    message = "Something went wrong...";
            }
        }
        return ErrorDto.builder()
                .errorCode(errorCode)
                .message(message)
                .build();
    }
}
