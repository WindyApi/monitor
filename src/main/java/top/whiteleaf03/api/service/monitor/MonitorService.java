package top.whiteleaf03.api.service.monitor;

import top.whiteleaf03.api.util.ResponseResult;

/**
 * @author WhiteLeaf03
 */
public interface MonitorService {
    /**
     * 获取容器信息
     *
     * @return 返回容器信息
     */
    ResponseResult getDockerInfo();
}
