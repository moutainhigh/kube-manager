package com.cgm.kube.client.dto;

import com.cgm.kube.client.constant.KubeConstant;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.models.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.util.Assert;

import java.util.*;


/**
 * 提供给用户的deployment类
 *
 * @author cgm
 */
@Data
@ApiModel("提供给用户的deployment类")
public class UserDeploymentDTO {
    @ApiModelProperty(value = "唯一标识", hidden = true)
    private String uid;

    @ApiModelProperty("名称（必须以字母开头，只能包含字母/数字/中划线）")
    private String name;

    @ApiModelProperty("命名空间")
    private String namespace;

    @ApiModelProperty(value = "副本数量", example = "1")
    private Integer replicas;

    @ApiModelProperty(value = "镜像", example = "centos:latest")
    private String image;

    @ApiModelProperty(value = "资源类型", example = "CPU")
    private String resourceType;

    @ApiModelProperty(value = "cpu要求（单位：m，千分之一核）", example = "500m")
    private String cpuRequests;

    @ApiModelProperty(value = "内存要求（单位：Mi，兆字节）", example = "200Mi")
    private String memRequests;

    @ApiModelProperty(value = "cpu上限（单位：m，千分之一核）", example = "800m")
    private String cpuLimits;

    @ApiModelProperty(value = "内存上限（单位：Mi，兆字节）", example = "256Mi")
    private String memLimits;

    @ApiModelProperty(value = "gpu要求（单位：自然数，一个）", example = "1")
    private String gpuLimits;

    @ApiModelProperty("标签")
    private Map<String, String> labels;

    @ApiModelProperty(value = "创建时间", hidden = true)
    private Long creationTimestamp;

    @ApiModelProperty(value = "可用的副本数量", hidden = true)
    private Integer availableReplicas;

    @ApiModelProperty(value = "状态", hidden = true)
    private String status;

    @ApiModelProperty(value = "可用状态", hidden = true)
    private String availableStatus;

    @ApiModelProperty(value = "执行状态", hidden = true)
    private String progressingStatus;


    public UserDeploymentDTO() {

    }

    /**
     * 解析k8s-client中的deployment
     *
     * @param kubeDeployment kubeDeployment
     */
    public UserDeploymentDTO(V1Deployment kubeDeployment) {
        // 创建时的基本信息
        Assert.notNull(kubeDeployment, KubeConstant.ErrorCode.NO_FIELD);
        Assert.isTrue(kubeDeployment.getMetadata() != null, KubeConstant.ErrorCode.NO_FIELD);
        this.uid = kubeDeployment.getMetadata().getUid();
        this.name = kubeDeployment.getMetadata().getName();
        this.namespace = kubeDeployment.getMetadata().getNamespace();
        this.labels = kubeDeployment.getMetadata().getLabels();

        Assert.isTrue(kubeDeployment.getSpec() != null, KubeConstant.ErrorCode.NO_FIELD);
        V1DeploymentSpec spec = kubeDeployment.getSpec();
        this.replicas = spec.getReplicas();

        Assert.isTrue(spec.getTemplate().getSpec() != null, KubeConstant.ErrorCode.NO_FIELD);
        V1PodSpec templateSpec = spec.getTemplate().getSpec();
        V1Container container = templateSpec.getContainers().get(0);
        this.image = container.getImage();

        // 资源信息
        V1ResourceRequirements resource = container.getResources();
        Assert.isTrue(resource != null, KubeConstant.ErrorCode.NO_FIELD);
        if (resource.getRequests() != null) {
            this.cpuRequests = resource.getRequests().get(KubeConstant.RESOURCE_CPU).toSuffixedString();
            this.memRequests = resource.getRequests().get(KubeConstant.RESOURCE_MEM).toSuffixedString();

        } else {
            this.cpuRequests = KubeConstant.RESOURCE_NOT_SET;
            this.memRequests = KubeConstant.RESOURCE_NOT_SET;
        }
        if (resource.getLimits() != null) {
            this.cpuLimits = resource.getLimits().get(KubeConstant.RESOURCE_CPU).toSuffixedString();
            this.memLimits = resource.getLimits().get(KubeConstant.RESOURCE_MEM).toSuffixedString();
            Quantity gpuLimitsQuantity = resource.getLimits().get(KubeConstant.RESOURCE_GPU);
            this.gpuLimits = gpuLimitsQuantity == null ?
                    KubeConstant.RESOURCE_GPU_DEFAULT : gpuLimitsQuantity.toSuffixedString();
        } else {
            this.cpuLimits = KubeConstant.RESOURCE_NOT_SET;
            this.memLimits = KubeConstant.RESOURCE_NOT_SET;
            this.gpuLimits = KubeConstant.RESOURCE_GPU_DEFAULT;
        }
        this.resourceType = KubeConstant.RESOURCE_GPU_DEFAULT.equals(this.gpuLimits) ? "CPU" : "GPU";


        // 各状态指标
        this.creationTimestamp = Objects.requireNonNull(kubeDeployment.getMetadata().getCreationTimestamp()).getMillis();
        V1DeploymentStatus deploymentStatus = kubeDeployment.getStatus();
        Assert.isTrue(deploymentStatus != null, KubeConstant.ErrorCode.NO_FIELD);
        this.availableReplicas = deploymentStatus.getAvailableReplicas() == null ?
                0 : deploymentStatus.getAvailableReplicas();
        this.status = this.availableReplicas < this.replicas ? KubeConstant.STATUS_FAILED : KubeConstant.STATUS_READY;

        List<V1DeploymentCondition> conditionList = deploymentStatus.getConditions();
        Assert.isTrue(conditionList != null, KubeConstant.ErrorCode.NO_FIELD);
        for (V1DeploymentCondition condition : conditionList) {
            if ("Available".equals(condition.getType())) {
                this.availableStatus = condition.getStatus();
            }
            if ("Progressing".equals(condition.getType())) {
                this.progressingStatus = condition.getStatus();
            }
        }
    }


    /**
     * 转换成k8s-client中的deployment
     * 基本结构示意：
     * #metadata:
     * #  name:
     * #  labels:
     * #spec:                    #V1DeploymentSpec
     * #  replicas:
     * #  selector:
     * #    matchLabels:
     * #  template:              #V1PodTemplateSpec
     * #    metadata:
     * #      name:
     * #      labels:
     * #    spec:                #V1PodSpec
     * #      containers:
     * #      - name:
     * #        image:
     * #        resources:
     * #          requests:
     * #          limits:
     * #      volumes:
     *
     * @return deployment
     */
    public V1Deployment toKube() {
        this.labels = this.labels == null ? new HashMap<>(1) : this.labels;
        this.labels.put("app", this.image.split(":")[0]);
        V1ObjectMeta metaData = new V1ObjectMeta()
                .name(this.getName())
                .namespace(this.getNamespace())
                .labels(this.getLabels());

        // 资源需求，spec->template->spec->containers->resources
        Map<String, Quantity> requestsMap = new HashMap<>(3);
        requestsMap.put("cpu", new Quantity(this.getCpuRequests()));
        requestsMap.put("memory", new Quantity(this.getMemRequests()));
        Map<String, Quantity> limitsMap = new HashMap<>(3);
        limitsMap.put("cpu", new Quantity(this.getCpuLimits()));
        limitsMap.put("memory", new Quantity(this.getMemLimits()));
        if (!KubeConstant.RESOURCE_GPU_DEFAULT.equals(this.getGpuLimits())) {
            limitsMap.put("nvidia.com/gpu", new Quantity(this.getGpuLimits()));
        }
        V1ResourceRequirements resource = new V1ResourceRequirements()
                .requests(requestsMap)
                .limits(limitsMap);

        // pod模板的spec配置，spec->template->spec
        String[] commands = {"/bin/bash", "-ce", "tail -f /dev/null"};
        V1Container podContainer = new V1Container()
                .name(this.getName())
                .image(this.getImage())
                .resources(resource)
                .command(Arrays.asList(commands));
        V1Volume volume = new V1Volume()
                .name(this.getName() + "volume")
                .emptyDir(new V1EmptyDirVolumeSource());
        V1PodSpec podSpec = new V1PodSpec()
                .containers(Collections.singletonList(podContainer))
                .volumes(Collections.singletonList(volume));

        // spec配置
        V1LabelSelector selector = new V1LabelSelector()
                .matchLabels(this.getLabels());
        V1PodTemplateSpec template = new V1PodTemplateSpec()
                .metadata(metaData)
                .spec(podSpec);
        V1DeploymentSpec spec = new V1DeploymentSpec()
                .replicas(this.getReplicas())
                .selector(selector)
                .template(template);

        return new V1DeploymentBuilder()
                .withApiVersion("apps/v1")
                .withKind("Deployment")
                .withMetadata(metaData)
                .withSpec(spec)
                .build();
    }

}
