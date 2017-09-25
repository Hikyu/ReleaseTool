# ReleaseTool

> 发布单自动填写工具

1. 备份发布单
2. 扫描指定发布单.xlsx，读取指定负责人需要填写的组件
3. 到config.josn中读取组件配置信息，包括Starteam分支与Starteam路径
4. 初始化指定组件，获取Starteam最新Label和revision信息
5. 比较ViewLabel与revisionLabel，如果revisionLabel比ViewLabel新，新建ViewLabel，否则维持不变
6. 将最新的ViewLabel填入发布单.xlsx

java -jar ReleaseTool.jar -h -s -a cluster

```
usage: ReleaseTool [-options]
 -a <activeComponentCenter>   激活的组件列表
 -h                           打印帮助信息
 -l <suffix>                  默认的Label后缀，Label格式为
                              "yyyyMMddHHmmss_suffix"，仅在自动模式下生效
 -r <releasePath>             发布单路径
 -s                           启动自动模式，自动新建Build Label
 -u <personInCharge>          组件负责人
************************************
发布单: D:/code/产品版本发布记录/神通xCluster集群件/20170921_王洋洋_神通并行数据库2017年9月改款_01.xlsx
负责人: 杜怀宇
注册组件: cluster
************************************
>>备份发布单...
>>读取发布单...
>>初始化组件信息...
>>更新发布单Label...
====================================
版本发布单（xCluster） : oscarJDBC驱动
label: KJDBCV1.0_20170901
====================================
版本发布单（xCluster） : ClusterJDBC驱动
label: 20170921_for_xCluster3.5
====================================
版本发布单（xCluster） : 集群逻辑备份恢复工具
label: 2012.4.25_cluster_2.1
====================================
版本发布单（xCluster） : 集群维护工具
label: 20150126_for_cluster3.5
====================================
版本发布单（xCluster） : 集群WEB管理工具
label: 20150925_xCluster3.5_for_sw
====================================
版本发布单（xCluster） : anywhere
label: 20170921_mainline
====================================
版本发布单（KSTORE） : JDBC驱动
label: 20170921_mainline
====================================
版本发布单（KSTORE） : DBA管理工具
label: 20161107_kstore4.5
====================================
版本发布单（KSTORE） : SQL交互工具
label: 20161107_kstore4.5
====================================
版本发布单（KSTORE） : 参数配置工具
label: 20170113_mainline
====================================
版本发布单（KSTORE） : 升级工具
label: 20160415_mainline
====================================
版本发布单（KSTORE） : 数据库维护工具
label: 20170921_mainline
====================================
版本发布单（KSTORE） : 数据迁移工具
label: 20170921_kstore4.5.5
====================================
版本发布单（KSTORE） : 数据库配置工具
label: 20170921_kstore4.5
====================================
版本发布单（KSTORE） : anywhere
label: 20170921_mainline
====================================
>>刷新磁盘...
>>DONE
```

注: 

- 配置信息从config.json中读取
- 非自动模式下，需要依次手动填写新建Label名称
- 对于相同的组件，集群发布单与oscar发布单中的分支并不相同，因此需要指定激活的组件列表，读取正确的组件信息