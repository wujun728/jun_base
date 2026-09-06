package ginyi.framework.security.service;

import ginyi.common.constant.CommonMessageConstants;
import ginyi.common.exception.CommonException;
import ginyi.common.redis.cache.RedisCache;
import ginyi.common.result.StateCode;
import ginyi.common.utils.DateUtils;
import ginyi.common.utils.NumberUtils;
import ginyi.common.utils.StringUtils;
import ginyi.common.utils.ip.IpUtils;
import ginyi.system.domain.*;
import ginyi.system.domain.model.dto.CacheDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.CacheKeyVo;
import ginyi.system.domain.model.vo.CacheVo;
import ginyi.system.service.ISysMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.util.*;

@Slf4j
@Service
public class SysMonitorServiceImpl implements ISysMonitorService {

    @Resource
    private RedisCache redisCache;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 获取系统服务信息
     */
    @Override
    public SysServer getServerInfo() throws InterruptedException {
        SysServer server = new SysServer();

        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();

        // CPU信息
        long[] prevTicks = hardware.getProcessor().getSystemCpuLoadTicks();
        Thread.sleep(1000);
        long[] ticks = hardware.getProcessor().getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long SysRate = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long userRate = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long waitRate = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long IdleRate = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = userRate + nice + SysRate + IdleRate + waitRate + irq + softIrq + steal;
        SysCpu sysCpu = new SysCpu();
        sysCpu.setCpuNum(hardware.getProcessor().getLogicalProcessorCount());
        sysCpu.setTotal(totalCpu);
        sysCpu.setSys(NumberUtils.round(((double) SysRate / totalCpu) * 100, 2));
        sysCpu.setUsed(NumberUtils.round(((double) userRate / totalCpu) * 100, 2));
        sysCpu.setWait(NumberUtils.round(((double) waitRate / totalCpu) * 100, 2));
        sysCpu.setFree(NumberUtils.round(((double) IdleRate / totalCpu) * 100, 2));


        // 内存
        SysMemory sysMemory = new SysMemory();
        sysMemory.setTotal(NumberUtils.round((double) hardware.getMemory().getTotal() / (1024 * 1024 * 1024), 2));
        sysMemory.setUsed(NumberUtils.round((double) (hardware.getMemory().getTotal() - hardware.getMemory().getAvailable()) / (1024 * 1024 * 1024), 2));
        sysMemory.setFree(NumberUtils.round((double) hardware.getMemory().getAvailable() / (1024 * 1024 * 1024), 2));
        sysMemory.setUsage(NumberUtils.round((sysMemory.getUsed() / sysMemory.getTotal()) * 100, 2));


        // Java虚拟机
        Properties properties = System.getProperties();
        SysJvm sysJvm = new SysJvm();
        sysJvm.setTotal(NumberUtils.round((double) Runtime.getRuntime().totalMemory() / (1024 * 1024), 2));
        sysJvm.setMax(NumberUtils.round((double) Runtime.getRuntime().maxMemory() / (1024 * 1024), 2));
        sysJvm.setFree(NumberUtils.round((double) Runtime.getRuntime().freeMemory() / (1024 * 1024), 2));
        sysJvm.setUsed(NumberUtils.round(sysJvm.getTotal() - sysJvm.getFree(), 2));
        sysJvm.setUsage(NumberUtils.round(((sysJvm.getTotal() - sysJvm.getFree()) / sysJvm.getTotal()) * 100, 2));
        sysJvm.setInputArgs(ManagementFactory.getRuntimeMXBean().getInputArguments().toString());
        sysJvm.setName(ManagementFactory.getRuntimeMXBean().getVmName());
        sysJvm.setRunTime(DateUtils.getDatePoor(DateUtils.getNowDate(), DateUtils.getServerStartDate()));
        sysJvm.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.getServerStartDate()));
        sysJvm.setVersion(properties.getProperty("java.version"));
        sysJvm.setHome(properties.getProperty("java.home"));


        // 服务器信息
        Sys sys = new Sys();
        sys.setComputerName(IpUtils.getHostName());
        sys.setComputerIp(IpUtils.getHostIp());
        sys.setOsName(properties.getProperty("os.name"));
        sys.setOsArch(properties.getProperty("os.arch"));
        sys.setUserDir(properties.getProperty("user.dir"));


        // 磁盘
        FileSystem fileSystem = systemInfo.getOperatingSystem().getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        List<SysFile> sysFiles = new LinkedList<>();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(NumberUtils.round((double) total / (1024 * 1024 * 1024), 2));
            sysFile.setFree(NumberUtils.round((double) free / (1024 * 1024 * 1024), 2));
            sysFile.setUsed(NumberUtils.round((double) used / (1024 * 1024 * 1024), 2));
            sysFile.setUsage(NumberUtils.round(((double) used / total) * 100, 2));
            sysFiles.add(sysFile);
        }

        server.setCpu(sysCpu);
        server.setMemory(sysMemory);
        server.setJvm(sysJvm);
        server.setSys(sys);
        server.setFile(sysFiles);
        return server;
    }

    /**
     * 获取缓存列表
     */
    @Override
    public BaseVo<CacheKeyVo> getCacheList() {
        BaseVo<CacheKeyVo> baseVo = new BaseVo<>();
        // 获取所有key
        Set<String> keys = redisCache.getKeys("*");
        ArrayList<CacheKeyVo> list = new ArrayList<>();

        for (String key : keys) {
            CacheKeyVo keyVo = new CacheKeyVo();
            DataType type = redisCache.getType(key);
            keyVo.setKey(key);
            keyVo.setType(type.code());
            list.add(keyVo);
        }

        // 排序
        list.sort(Comparator.comparing(CacheKeyVo::getKey));
        baseVo.setList(list);
        baseVo.setCount(keys.size());
        return baseVo;
    }

    /**
     * 获取缓存详情
     */
    @Override
    public CacheVo getCacheDetails(CacheDto cacheDto) {

        if (!redisCache.hasKey(cacheDto.getKey())) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, CommonMessageConstants.REDIS_KEY_NOT_EXIST);
        }

        Object object = null;
        try {
            if ("string".equalsIgnoreCase(cacheDto.getType())) {
                object = redisCache.getCacheObject(cacheDto.getKey(), Object.class);
            }
            if ("list".equalsIgnoreCase(cacheDto.getType())) {
                object = redisCache.getCacheList(cacheDto.getKey(), Object.class);
            }
        } catch (Exception e) {
            throw new CommonException(StateCode.ERROR_BUSINESS, CommonMessageConstants.REDIS_VALUE_TYPE_NOT_MATCH);
        }

        if (StringUtils.isNull(object)) {
            throw new CommonException(StateCode.ERROR_BUSINESS, CommonMessageConstants.REDIS_VALUE_TYPE_NOT_MATCH);
        }

        CacheVo cacheVo = new CacheVo();
        cacheVo.setKey(cacheDto.getKey());
        cacheVo.setExpire(redisCache.getExpire(cacheDto.getKey()));
        cacheVo.setValue(object);
        return cacheVo;
    }

    /**
     * 删除缓存
     */
    @Override
    public void removeCache(String key) {
        if (!redisCache.hasKey(key)) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, CommonMessageConstants.REDIS_KEY_NOT_EXIST);
        }
        redisCache.removeCacheObject(key);
    }
}
