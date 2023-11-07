package top.whiteleaf03.api.modal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContainerInfoVO {
    /**
     * 节点名称
     */
    private String name;

    /**
     * 容器内存用量
     */
    private String memoryUsed;

    /**
     * 容器最大可用内存
     */
    private String memoryLimit;

    /**
     * 容器内存用量百分比
     */
    private String memoryPercent;

    /**
     * CPU使用量
     */
    private String cpuUsage;

    /**
     * 获取时间
     */
    private String date;
}
