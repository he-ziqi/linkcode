package world.hzq.linkcode.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("邮件信息实体")
public class MailModel implements Serializable {
    @ApiModelProperty("收件人")
    private String target;
    @ApiModelProperty("邮件标题")
    private String title;
    @ApiModelProperty("邮件内容")
    private String content;
    @ApiModelProperty("是否为html格式")
    private Boolean html;

    public MailModel() {
    }

    public MailModel(String target, String title, String content, Boolean html) {
        this.target = target;
        this.title = title;
        this.content = content;
        this.html = html;
    }

    @Override
    public String toString() {
        return "MailModel{" +
                "target='" + target + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", html=" + html +
                '}';
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getHtml() {
        return html;
    }

    public void setHtml(Boolean html) {
        this.html = html;
    }
}
