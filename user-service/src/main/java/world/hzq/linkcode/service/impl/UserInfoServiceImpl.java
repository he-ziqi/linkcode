package world.hzq.linkcode.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import world.hzq.linkcode.entity.SolutionRecord;
import world.hzq.linkcode.entity.User;
import world.hzq.linkcode.es.index.ElasticSearchIndexType;
import world.hzq.linkcode.service.UserInfoService;
import world.hzq.linkcode.type.InvokeRecordStatusType;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.ResultState;
import world.hzq.linkcode.util.Tools;
import world.hzq.linkcode.util.WebUtil;
import world.hzq.linkcode.vo.InfoVO;
import world.hzq.linkcode.vo.UserVO;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author hzq
 * @version 1.0
 * @description 用户信息获取实现
 * @date 2023/2/8 21:06
 */
@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private WebUtil webUtil;

    @Value("${compile.root.path}")
    private String userPath;

    @Value("${compile.tourist.path}")
    private String touristPath;

    @Override
    public Result<UserVO> getUserInfo(String token) {
        token = token.replaceFirst("bearer ","");
        String userJsonStr = stringRedisTemplate.opsForValue().get(token);
        User user = JSONObject.parseObject(userJsonStr, User.class);
        if(user == null){
            return Result.fail("登录信息过期,请重新登录");
        }
        //用户数据脱敏
        String phoneNumber = Tools.phoneNumberChange(user.getPhoneNumber());
        String email = Tools.emailChange(user.getEmail());
        UserVO userVO = new UserVO(user.getId(),user.getNickName(),phoneNumber,email,user.getRoleType());
        return Result.success("获取成功",userVO);
    }

    @Override
    public void getAvatar(HttpServletRequest request, HttpServletResponse response) {
        User user = webUtil.currentUser();
        if(user == null){
            try {
                //响应结果
                response.getWriter().println(JSONObject.toJSON(Result.fail("登录信息过期,请重新登录")));
                response.setStatus(ResultState.FAIL.getCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        //获取用户头像地址
        String avatarAddr = user.getAvatarAddr();
        //读取文件
        File avatarFile = new File(avatarAddr);
        if(!avatarFile.exists() || !avatarFile.isFile()){
            response.setStatus(502);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> map = new HashMap<>();
            map.put("code",ResultState.FAIL.getCode());
            map.put("msg","头像不存在,系统异常");
            try {
                response.getOutputStream().write(objectMapper.writeValueAsBytes(map));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(avatarFile);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("头像获取失败");
            return;
        }
        try {
            String format = "jpg";
            if(avatarAddr.endsWith("png") || avatarAddr.endsWith("PNG")){
                format = "png";
            }
            ImageIO.write(bufferedImage,format,response.getOutputStream());
        } catch (IOException e) {
            log.info("头像写入响应流时失败");
            e.printStackTrace();
        }
    }

    //由于时间戳,invoke-code只请求一次,但造成了后续请求都不再获取编译路径...未解决======
    @Override
    public Result<String> getCompilePath() {
        User user = webUtil.currentUser();
        if(user == null){ //游客
            //游客目录 防止重复+时间戳
            return Result.success("获取成功","file-center" + File.separator + touristPath + File.separator + new Date().getTime() + File.separator);
        }
        Long userId = webUtil.currentUser().getId();
        return Result.success("获取成功","file-center" + File.separator + userPath + File.separator + userId + File.separator);
    }

    @Override
    public Result<InfoVO<SolutionRecord>> getInvokeRecord(Long topicId,String languageType,Integer pageNo,Integer pageSize) {
        User user = webUtil.currentUser();
        if(Tools.isNull(user)){
            return Result.error("未登录不允许获取");
        }
        //从es中分页获取代码执行记录
        SearchRequest searchRequest = new SearchRequest(ElasticSearchIndexType.INDEX_SOLUTION_RECORD.getCode());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(new TermQueryBuilder("userId",user.getId()));
        boolQueryBuilder.must(new TermQueryBuilder("languageType",languageType));
        boolQueryBuilder.must(new TermQueryBuilder("topicId",topicId));
        searchSourceBuilder.from((pageNo - 1) * pageSize);
        searchSourceBuilder.size(pageSize);
        searchSourceBuilder.query(boolQueryBuilder);
        //按时间降序排序
        searchSourceBuilder.sort("updateTime", SortOrder.DESC);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info("查询执行代码记录异常,user:{},topicId:{},异常信息:{}",user,topicId,e.getMessage());
            e.printStackTrace();
            return Result.fail("查询失败,请稍后重试");
        }
        SearchHits hits = response.getHits();
        List<SolutionRecord> solutionRecordList = new ArrayList<>(hits.getHits().length);
        hits.forEach(hit -> {
            String resultStr = hit.getSourceAsString();
            SolutionRecord record = JSONObject.parseObject(resultStr, SolutionRecord.class);
            record.setUserId(null); //用户id脱敏
            solutionRecordList.add(record);
        });
        return Result.success("获取成功",new InfoVO<>(solutionRecordList,response.getHits().getTotalHits().value));
    }

    @Override
    public Result<List<String>> getInvokeRecordStatusType() {
        List<String> recordStatusTypeList = new ArrayList<>(InvokeRecordStatusType.values().length);
        for (InvokeRecordStatusType type : InvokeRecordStatusType.values()) {
            recordStatusTypeList.add(type.getCode());
        }
        return Result.success(recordStatusTypeList);
    }
}
