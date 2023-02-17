package world.hzq.linkcode.util;

/**
 * @author hzq
 * @version 1.0
 * @description 验证码生成器
 * @date 2023/2/6 22:51
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码生成器
 */
public class VerificationCodeGenerator {
    //图片宽
    private static final int WIDTH = 150;
    //图片高
    private static final int HEIGHT = 70;
    //图片类型
    public static final String TYPE = "jpg";
    //验证码字符数
    private static final byte CODES = 5;
    //随机线条数
    private static final byte LINES = 20;
    //随机背景色
    private static final Color[] bgColors = {
            Color.DARK_GRAY,Color.pink,Color.LIGHT_GRAY
    };
    //所有验证码的集合
    private static final String verificationCode = "ASDFGHJKLZXCVBNMQWERTYUIOP1234567890zxcvbnmasdfghjklqwertyuiop";//"ASDFGHJKLZXCVBNMQWERTYUIOP1234567890zxcvbnmasdfghjklqwertyuiop";
    //随机验证码色
    private static final Color[] codeColors = {
            Color.CYAN,Color.BLUE,Color.GREEN,Color.magenta,Color.YELLOW,Color.WHITE
    };
    //随机线条的颜色
    private static final Color[] lineColors = {
            Color.BLUE,Color.WHITE,Color.CYAN,Color.black
    };
    //验证码字体
    private static final Font font = new Font("微软雅黑",Font.BOLD,30);
    //随机类
    private static final Random random = new Random();

    //构造器私有化
    private VerificationCodeGenerator(){}

    //获取验证码图片
    public static BufferedImage getImage(String code){
        return getImage(code,WIDTH,HEIGHT);
    }

    public static BufferedImage getImage(String code,int width,int height){
        //初始化图片
        BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_3BYTE_BGR);
        //获取画笔
        Graphics g = image.getGraphics();
        //背景颜色随机索引
        int bgIndex = getRandomAmong(bgColors.length);
        //将画笔颜色设置为背景索引所指的颜色
        g.setColor(bgColors[bgIndex]);
        //背景颜色充满图片
        g.fillRect(0,0,width,height);
        //画边框
        g.setColor(Color.black);
        g.drawRect(0,0, width - 1,height - 1);
        //画随机验证字符
        for (int i = 0; i < code.length(); i++) {
            //设置画笔字体
            g.setFont(font);
            //获取随机字符
            String randomCode = String.valueOf(code.charAt(i));
            //获取验证码的随机颜色
            int codeColorIndex = getRandomAmong(codeColors.length);
            Color codeColor = codeColors[codeColorIndex];
            //将画笔颜色设置为验证码的随机色
            g.setColor(codeColor);
            //将随机字符画到图片上
            g.drawString(randomCode,width / code.length() * i,height >> 1);
        }
        //画随机线条
        for (int i = 1; i < LINES; i++) {
            //获取随机线条的颜色
            int lineIndex = getRandomAmong(lineColors.length);
            //设置画笔颜色为随机线条的颜色
            g.setColor(lineColors[lineIndex]);
            //设置线条的随机坐标
            int x1 = getRandomAmong(width);
            int x2 = getRandomAmong(width);
            int y1 = getRandomAmong(height);
            int y2 = getRandomAmong(height);
            //画随机线条
            g.drawLine(x1,y1,x2,y2);
        }
        return image;
    }

    //生成指定位数的随机验证码
    public static String generateVerificationCode(int among){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < among; i++) {
            //随机字符的索引
            int codeIndex = getRandomAmong(verificationCode.length());
            //获取随机字符
            Character randomCode = verificationCode.charAt(codeIndex);
            sb.append(randomCode);
        }
        return sb.toString();
    }

    //生成默认位数的随机验证码
    public static String generateVerificationCode(){
        return generateVerificationCode(CODES);
    }

    //获取随机数
    private static int getRandomAmong(int max){
        return random.nextInt(max);
    }

    //获取图片类型
    public static String getType(){
        return VerificationCodeGenerator.TYPE;
    }

}