package top.whiteleaf03.api.service.monitor;

import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.whiteleaf03.api.modal.vo.ContainerInfoVO;
import top.whiteleaf03.api.util.RedisCache;
import top.whiteleaf03.api.util.ResponseResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author WhiteLeaf03
 */
@Service
public class MonitorServiceImpl implements MonitorService {
    private final String[] containerList = {"windyapi-backend", "windyapi-gateway", "windyapi-interface"};
    private final SimpleDateFormat nodeInfoDateFormat = new SimpleDateFormat("mm:ss");
    private final RedisCache redisCache;

    @Autowired
    public MonitorServiceImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 获取容器信息
     *
     * @return 返回容器信息
     */
    @Override
    public ResponseResult getDockerInfo() {
        List<String> containerInfoVOList = redisCache.getCacheObject("DockerContainerInfo");
        return ResponseResult.success(containerInfoVOList);
    }

    @Scheduled(fixedRate = 5000)
    public void saveContainerInfo() {
        try {
            String date = nodeInfoDateFormat.format(new Date());
            List<ContainerInfoVO> containerInfoVOList = new ArrayList<>();
            for (String containerName : containerList) {
                Process process = Runtime.getRuntime().exec("docker stats --no-stream " + containerName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                int index = 0;
                while ((line = reader.readLine()) != null) {
                    if (index == 0) {
                        index = 1;
                        continue;
                    }
                    String[] parts = line.split("\\s+");
                    String cpuUsage = parts[2].replace("%", "");
                    String memoryUsage = parts[3].replace("MiB", "");
                    String memoryLimit = parts[5].replace("MiB", "");
                    String memoryPercent = parts[6].replace("%", "");
                    containerInfoVOList.add(new ContainerInfoVO(containerName, memoryUsage, memoryLimit, memoryPercent, cpuUsage, date));
                }
            }
            putInfoToRedis(JSONUtil.toJsonStr(containerInfoVOList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void putInfoToRedis(String containerInfoListJsonStr) {
        List<String> containerInfoVOList = redisCache.getCacheObject("DockerContainerInfo");
        if (Objects.isNull(containerInfoVOList) || containerInfoVOList.isEmpty()) {
            containerInfoVOList = new ArrayList<>();
        } else if (containerInfoVOList.size() >= 10) {
            containerInfoVOList.remove(9);
        }
        containerInfoVOList.add(0, containerInfoListJsonStr);
        redisCache.setObject("DockerContainerInfo", containerInfoVOList);
    }
}