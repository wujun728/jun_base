package ginyi.framework.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ginyi.common.constant.CacheConstants;
import ginyi.common.constant.CommonMessageConstants;
import ginyi.common.exception.CommonException;
import ginyi.common.mysql.MyPage;
import ginyi.common.redis.cache.RedisCache;
import ginyi.common.result.StateCode;
import ginyi.common.utils.StringUtils;
import ginyi.system.domain.SysDept;
import ginyi.system.domain.SysUser;
import ginyi.system.domain.model.dto.DeptDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.DeptVo;
import ginyi.system.mapper.SysDeptMapper;
import ginyi.system.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysDeptServiceImpl implements ISysDeptService {

    @Resource
    private SysDeptMapper deptMapper;
    @Resource
    private RedisCache redisCache;

    /**
     * 获取部门列表
     *
     * @param deptDto
     * @param page
     * @param pageSize
     */
    @Override
    public BaseVo<SysDept> list(DeptDto deptDto, Long page, Long pageSize) {
        IPage<SysDept> list = deptMapper.list(deptDto, new MyPage(page, pageSize).getPage());
        // 转成树
        List<SysDept> deptList = list.getRecords().stream()
                .filter(dept -> dept.getParentId().equals(0L))
                .map(dept -> convertToDeptTree(dept, list.getRecords())).collect(Collectors.toList());
        BaseVo<SysDept> baseVo = new BaseVo<>();
        baseVo.setList(deptList);
        baseVo.setCount(deptList.size());
        return baseVo;
    }

    /**
     * 根据部门id获取部门详情
     *
     * @param deptId
     * @return
     */
    @Override
    public DeptVo getDeptByDeptId(Long deptId) {
        // 检查缓存中是否存在空id
        if (redisCache.hasKey(CacheConstants.DEPT_NOT_EXIST_KEY + deptId)) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, deptId + CommonMessageConstants.DEPT_NOT_EXIST);
        }
        DeptVo deptVo = new DeptVo();
        // 检查缓存中是否存在
        SysDept dept = redisCache.getCacheObject(CacheConstants.DEPT_DETAILS_BY_DEPTID_KEY + deptId, SysDept.class);
        if (StringUtils.isNotNull(dept)) {
            BeanUtils.copyProperties(dept, deptVo);
            return deptVo;
        }
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getDeptId, deptId);
        dept = deptMapper.selectOne(queryWrapper);
        if (StringUtils.isNull(dept)) {
            redisCache.setCacheObject(CacheConstants.DEPT_NOT_EXIST_KEY + deptId, null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, deptId + CommonMessageConstants.DEPT_NOT_EXIST);
        }
        redisCache.setCacheObject(CacheConstants.DEPT_DETAILS_BY_DEPTID_KEY + deptId, dept);
        BeanUtils.copyProperties(dept, deptVo);
        return deptVo;
    }

    /**
     * 新增部门
     *
     * @param deptDto
     */
    @Override
    public void addDept(DeptDto deptDto) {
        Long parentId = StringUtils.isNull(deptDto.getParentId()) ? 0L : deptDto.getParentId();
        // 检查缓存中是否有标记同个分支下名称有被使用
        if (redisCache.hasKey(CacheConstants.DEPT_NAME_USED_KEY + parentId + deptDto.getDeptName())) {
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.DEPT_NAME_USED);
        }
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getParentId, parentId)
                .eq(SysDept::getDeptName, deptDto.getDeptName());
        SysDept dept = deptMapper.selectOne(queryWrapper);
        // 检查同个分支下名称是否被使用
        if (StringUtils.isNotNull(dept)) {
            redisCache.setCacheObject(CacheConstants.DEPT_NAME_USED_KEY + parentId + deptDto.getDeptName(), null);
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.DEPT_NAME_USED);
        }
        deptMapper.insertDept(deptDto);
        redisCache.removeCacheObject(CacheConstants.DEPT_KEY_PREFIX);
    }

    /**
     * 更新部门
     *
     * @param deptDto
     */
    @Override
    public void updateDept(DeptDto deptDto) {
        // 检查缓存中是否有标记空id
        if (redisCache.hasKey(CacheConstants.DEPT_NOT_EXIST_KEY + deptDto.getDeptId())) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, deptDto.getDeptId() + CommonMessageConstants.DEPT_NOT_EXIST);
        }

        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        Long parentId = StringUtils.isNull(deptDto.getParentId()) ? 0L : deptDto.getParentId();
        queryWrapper.eq(parentId != 0L, SysDept::getDeptId, parentId);
        List<SysDept> deptList = deptMapper.selectList(queryWrapper);
        // 检查上级部门是否存在
        if (deptList.size() == 0) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, CommonMessageConstants.DEPT_PARENT_NOT_EXIST);
        }

        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getParentId, parentId)
                .eq(SysDept::getDeptName, deptDto.getDeptName());
        SysDept dept = deptMapper.selectOne(queryWrapper);
        // 检查同个分支下名称是否被使用
        if (StringUtils.isNotNull(dept)) {
            redisCache.setCacheObject(CacheConstants.DEPT_NAME_USED_KEY + parentId + deptDto.getDeptName(), null);
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.DEPT_NAME_USED);
        }

        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getDeptId, deptDto.getDeptId());
        dept = deptMapper.selectOne(queryWrapper);
        // 检查是否存在
        if (StringUtils.isNull(dept)) {
            redisCache.setCacheObject(CacheConstants.DEPT_NOT_EXIST_KEY + deptDto.getDeptId(), null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, deptDto.getDeptId() + CommonMessageConstants.DEPT_NOT_EXIST);
        }
        deptMapper.updateDept(deptDto);
        redisCache.removeCacheObject(CacheConstants.DEPT_KEY_PREFIX);
    }

    /**
     * 删除部门
     *
     * @param deptId
     */
    @Override
    public void removeDeptById(Long deptId) {
        // 检查缓存中是否标记着空id
        if (redisCache.hasKey(CacheConstants.DEPT_NOT_EXIST_KEY + deptId)) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, deptId + CommonMessageConstants.DEPT_NOT_EXIST);
        }
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getDeptId, deptId);
        SysDept dept = deptMapper.selectOne(queryWrapper);
        if (StringUtils.isNull(dept)) {
            redisCache.setCacheObject(CacheConstants.DEPT_NOT_EXIST_KEY + deptId, null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, deptId + CommonMessageConstants.DEPT_NOT_EXIST);
        }
        deptMapper.deleteById(deptId);
        redisCache.removeCacheObject(CacheConstants.DEPT_KEY_PREFIX);
    }


    /**
     * 批量删除部门
     *
     * @param ids
     */
    @Override
    public void removeDeptByIds(Set<Long> ids) {
        if (ids.size() > 0) {
            List<SysDept> deptList;
            deptList = redisCache.getCacheList(CacheConstants.DEPT_LIST_KEY, SysDept.class);
            if (StringUtils.isNull(deptList) || deptList.size() == 0) {
                LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
                deptList = deptMapper.selectList(queryWrapper);
                redisCache.setCacheList(CacheConstants.DEPT_LIST_KEY, deptList);
            }
            for (Long deptId : ids) {
                // 检查缓存中是否标记着空id
                if (redisCache.hasKey(CacheConstants.DEPT_NOT_EXIST_KEY + deptId)) {
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, deptId + CommonMessageConstants.DEPT_NOT_EXIST);
                }
                boolean exist = deptList.stream().anyMatch(dept -> deptId.equals(dept.getDeptId()));
                if (!exist) {
                    redisCache.setCacheObject(CacheConstants.DEPT_NOT_EXIST_KEY + deptId, null);
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, deptId + CommonMessageConstants.DEPT_NOT_EXIST);
                }
            }
            deptMapper.deleteBatchIds(ids);
            redisCache.removeCacheObject(CacheConstants.DEPT_KEY_PREFIX);
        } else {
            throw new CommonException(StateCode.ERROR_REQUEST_PARAMS, CommonMessageConstants.SYS_REQUEST_ILLEGAL);
        }
    }

    /**
     * 更新状态
     * @param deptDto
     */
    @Override
    public void updateStatus(DeptDto deptDto) {
        if (StringUtils.isNull(deptDto.getDeptId())) {
            throw new CommonException(StateCode.ERROR_PARAMS, CommonMessageConstants.DEPT_ID_NOT_FOUND);
        }
        // 状态参数是否合法
        if (!("0".equals(deptDto.getStatus()) || "1".equals(deptDto.getStatus()))) {
            throw new CommonException(StateCode.ERROR_PARAMS, CommonMessageConstants.DEPT_STATUS_ILLEGAL);
        }
        // 检查缓存中是否标记着空id
        if (redisCache.hasKey(CacheConstants.DEPT_NOT_EXIST_KEY + deptDto.getDeptId())) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, deptDto.getDeptId() + CommonMessageConstants.DEPT_NOT_EXIST);
        }
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getDeptId, deptDto.getDeptId());
        SysDept dept = deptMapper.selectOne(queryWrapper);
        if (StringUtils.isNull(dept)) {
            redisCache.setCacheObject(CacheConstants.DEPT_NOT_EXIST_KEY + deptDto.getDeptId(), null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, deptDto.getDeptId() + CommonMessageConstants.DEPT_NOT_EXIST);
        }
        deptMapper.updateDeptStatus(deptDto);
    }

    /**
     * 部门列表转换成部门树的结构
     *
     * @param dept
     * @param list
     * @return
     */
    public SysDept convertToDeptTree(SysDept dept, List<SysDept> list) {
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(dept, sysDept);
        List<SysDept> children = list.stream()
                .filter(subDept -> dept.getDeptId().equals(subDept.getParentId()))
                .map(subDept -> convertToDeptTree(subDept, list)).collect(Collectors.toList());
        sysDept.setChildren(children);
        return sysDept;
    }
}
