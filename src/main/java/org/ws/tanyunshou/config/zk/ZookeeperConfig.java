package org.ws.tanyunshou.config.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yinan
 * @date 19-1-20
 */
@Configuration
public class ZookeeperConfig {
    @Autowired
    private WrapperZookeeper wrapperZookeeper;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.newClient(
                wrapperZookeeper.getConnectString(),
                wrapperZookeeper.getSessionTimeoutMs(),
                wrapperZookeeper.getConnectionTimeoutMs(),
                new RetryNTimes(wrapperZookeeper.getRetryCount(), wrapperZookeeper.getElapsedTimeMs())
        );
    }


}
