package world.hzq.linkcode.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import world.hzq.linkcode.util.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hzq
 * @version 1.0
 * @description 全局异常处理器
 * @date 2023/2/3 16:02
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(OnlyInternalCallException.class)
    public Result<String> onlyInternalCall(HttpServletRequest request){
        logger.error("内部接口,外部不允许调用：{}",request.getRequestURI());
        return Result.error("内部接口,外部不允许调用");
    }

    public Result<String> totalException(HttpServletRequest request,Exception e){
        logger.info("调用接口{}时出现异常,异常信息：{}",request.getRequestURI(),e.getMessage());
        return Result.error("系统出错啦,请联系管理员~");
    }
}
