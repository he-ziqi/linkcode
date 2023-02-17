package world.hzq.linkcode.exception;

/**
 * @author hzq
 * @version 1.0
 * @description es操作异常
 * @date 2023/2/5 18:11
 */
public class ElasticSearchOperationException extends Exception{
    public ElasticSearchOperationException() {
    }

    public ElasticSearchOperationException(String message) {
        super(message);
    }
}
