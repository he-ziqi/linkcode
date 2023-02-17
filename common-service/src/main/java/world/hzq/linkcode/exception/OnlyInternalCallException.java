package world.hzq.linkcode.exception;

/**
 * @author hzq
 * @version 1.0
 * @description 仅内部调用异常
 * @date 2023/2/3 16:04
 */
public class OnlyInternalCallException extends RuntimeException{
    public OnlyInternalCallException() {
    }

    public OnlyInternalCallException(String message) {
        super(message);
    }
}
