package ginyi.framework.security.service;

import ginyi.common.mysql.MyPage;
import ginyi.system.domain.SysLogLogin;
import ginyi.system.domain.SysLogOperation;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class SysLogServiceImpl implements ISysLogService {

    @Resource
    private MongoTemplate mongoTemplate;


    @Override
    public BaseVo<SysLogLogin> getLoginLogList(Long page, Long pageSize) {
        MyPage IPage = new MyPage(page, pageSize);
        BaseVo<SysLogLogin> baseVo = new BaseVo<>();
        Query query = new Query();

        long count = mongoTemplate.count(query, SysLogLogin.class);

        query.skip((int) (IPage.getPageSize() * (IPage.getPageNum() - 1)));
        query.limit(IPage.getPageSize().intValue());
        query.with(Sort.by(Sort.Order.desc("createTime")));
        List<SysLogLogin> list = mongoTemplate.find(query, SysLogLogin.class);

        baseVo.setList(list);
        baseVo.setCount((int) count);
        return baseVo;
    }

    @Override
    public BaseVo<SysLogOperation> getOperationLogList(Long page, Long pageSize) {
        MyPage IPage = new MyPage(page, pageSize);
        BaseVo<SysLogOperation> baseVo = new BaseVo<>();
        Query query = new Query();

        long count = mongoTemplate.count(query, SysLogOperation.class);

        int skip = (int) (IPage.getPageSize() * (IPage.getPageNum() - 1));
        query.skip(skip).limit(IPage.getPageSize().intValue());
        query.with(Sort.by(Sort.Order.desc("createTime")));
        List<SysLogOperation> list = mongoTemplate.find(query, SysLogOperation.class);

        baseVo.setList(list);
        baseVo.setCount((int) count);
        return baseVo;
    }
}
