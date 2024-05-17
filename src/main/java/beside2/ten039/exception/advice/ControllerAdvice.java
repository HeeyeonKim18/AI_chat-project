package beside2.ten039.exception.advice;

import beside2.ten039.exception.InternalServerErrorException;
import beside2.ten039.exception.KakaoServerErrorException;
import beside2.ten039.exception.NotFoundException;
import beside2.ten039.exception.NotValidatedValueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> notFoundExceptionHandler(NotFoundException e){
        logError(e);
        ErrorResult errorResult = new ErrorResult("NotFoundException", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> illegalAccessExceptionHandler(IllegalAccessException e){
        logError(e);
        ErrorResult errorResult = new ErrorResult("IllegalAccessException", "유효하지 않은 토큰 값으로 접근할 수 없습니다.");
        return new ResponseEntity(errorResult, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> ExceptionHandler(NotValidatedValueException e){
        logError(e);
        ErrorResult errorResult = new ErrorResult("NotValidatedValueException", "request 값의 형태가 올바르지 않습니다.");
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> ExceptionHandler(KakaoServerErrorException e){
        logError(e);
        ErrorResult errorResult = new ErrorResult("kakaoServerError", "kakao에서 사용자 정보를 가져올 수 없습니다.");
        return new ResponseEntity(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> ExceptionHandler(InternalServerErrorException e){
        logError(e);
        ErrorResult errorResult = new ErrorResult("InternalServerErrorException", "서버에서 오류가 발생했습니다.");
        return new ResponseEntity(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void logError(Exception e){
        log.error("[exceptionHandler] ", e);
    }

}
