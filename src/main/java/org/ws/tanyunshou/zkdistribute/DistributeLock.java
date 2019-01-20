package org.ws.tanyunshou.zkdistribute;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ws.tanyunshou.util.CommonConstant;

import java.util.concurrent.CountDownLatch;

/**
 * @author yinan
 * @date 19-1-20
 */
@Component
public class DistributeLock implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(DistributeLock.class);

    private CountDownLatch downLatch = new CountDownLatch(1);

    @Autowired
    private CuratorFramework framework;


    /**
     * 获取锁
     * @param path
     */
    public void lock(String path) {
        String keyPath = CommonConstant.ROOT_PATH_LOCK + path;
        for (;;) {
            try {
                framework
                        .create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(keyPath);
                logger.info("success to acquire lock for path: {}", keyPath);
                break;
            } catch (Exception e) {
                logger.info("failed to acquired lock for path: {}", keyPath);
                logger.info("while try again ......");
                try {
                    if (downLatch.getCount() <= 0) {
                        downLatch = new CountDownLatch(1);
                    }
                    downLatch.await();
                } catch (InterruptedException e1) {
                    logger.error("failed to try, caused by {}", e1.toString());
                }
            }
        }

    }

    public boolean unlock(String path) {
        try {
            String keyPath = CommonConstant.ROOT_PATH_LOCK + path;
            if (framework.checkExists().forPath(keyPath) != null) {
                framework.delete().forPath(keyPath);
            }
        } catch (Exception e) {
            logger.error("failed to release lock: {}", e.getMessage());
            return false;
        }
        return true;
    }

    private void addWatcher(String path) throws Exception {
        String keyPath = "";
        if (path.equals(CommonConstant.ROOT_PATH_LOCK)) {
            keyPath += path;
        } else {
            keyPath = CommonConstant.ROOT_PATH_LOCK + path;
        }

        final PathChildrenCache cache = new PathChildrenCache(framework, keyPath, false);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener((client, event) -> {
            if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                String oldPath = event.getData().getPath();
                logger.info("last node {} has been unlocked", oldPath);
                if (oldPath.contains(path)) {
                    //释放计数器，让当前请求获取锁
                    downLatch.countDown();
                }
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        framework = framework.usingNamespace(CommonConstant.LOCK_NAMESPACE);
        String keyPath = CommonConstant.ROOT_PATH_LOCK;
        try {
            if (framework.checkExists().forPath(keyPath) == null) {
                framework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(keyPath);
            }
            addWatcher(CommonConstant.ROOT_PATH_LOCK);
            logger.info("root path's watcher create success!");
        } catch (Exception e) {
            logger.error("connect zookeeper failed, please check the log: {}", e.getMessage());
        }
    }
}