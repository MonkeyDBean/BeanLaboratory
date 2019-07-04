package com.monkeybean.labo.component.config;

import com.monkeybean.labo.util.FtpUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Created by MonkeyBean on 2019/6/26.
 */
@Component
public class FtpPoolConfig {
    private static Logger logger = LoggerFactory.getLogger(FtpPoolConfig.class);

    private ObjectPool<FTPClient> pool;

    @Autowired
    @SuppressWarnings("unchecked")
    public FtpPoolConfig(FtpConfig ftpConfig) {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

        //在从对象池获取对象时是否检测对象是否有效
        poolConfig.setTestOnBorrow(true);

        //在向对象池中归还对象时是否检测对象是否有效
        poolConfig.setTestOnReturn(true);

        ///在检测空闲对象线程检测到对象不需要移除时，是否检测对象的有效性
        poolConfig.setTestWhileIdle(true);

        //对象最小的空闲时间, 不会保留最小的空闲对象数量
        poolConfig.setMinEvictableIdleTimeMillis(60000);

        //对象最小的空间时间,会保留最小的空闲对象数量
        poolConfig.setSoftMinEvictableIdleTimeMillis(50000);

        //空闲对象检测线程的执行周期，即多长时间执行一次空闲对象检测
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
        pool = new GenericObjectPool<>(new FtpClientPooledObjectFactory(ftpConfig), poolConfig);
        preLoadingFtpClient(ftpConfig.getInitSize(), poolConfig.getMaxIdle());

        //初始化ftp工具类中的ftpClientPool
        FtpUtil.init(pool, null);
    }

    /**
     * 预先加载FTPClient连接到对象池中
     *
     * @param initialSize 初始化连接数
     * @param maxIdle     最大空闲连接数
     */
    private void preLoadingFtpClient(int initialSize, int maxIdle) {
        if (initialSize <= 0) {
            return;
        }
        int size = Math.min(initialSize, maxIdle);
        for (int i = 0; i < size; i++) {
            try {
                pool.addObject();
            } catch (Exception e) {
                logger.error("preLoadingFtpClient error: {}", e);
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (pool != null) {
            pool.close();
            logger.info("ftpClientPool destroyed");
        }
    }

    /**
     * FtpClient对象工厂类
     */
    static class FtpClientPooledObjectFactory implements PooledObjectFactory<FTPClient> {
        private FtpConfig props;

        @Autowired
        public FtpClientPooledObjectFactory(FtpConfig props) {
            this.props = props;
        }

        @Override
        public PooledObject<FTPClient> makeObject() throws IOException {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(props.getAddress(), FTP.DEFAULT_PORT);
            ftpClient.login(props.getUser(), props.getPassword());
            logger.info("ftp connect reply code: {}", ftpClient.getReplyCode());
            ftpClient.setBufferSize(props.getBufferSize());
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setRemoteVerificationEnabled(false);
            ftpClient.enterLocalPassiveMode();
            return new DefaultPooledObject<>(ftpClient);
        }

        @Override
        public void destroyObject(PooledObject<FTPClient> p) throws Exception {
            FTPClient ftpClient = getObject(p);
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        }

        @Override
        public boolean validateObject(PooledObject<FTPClient> p) {
            FTPClient ftpClient = getObject(p);
            if (ftpClient == null || !ftpClient.isConnected()) {
                return false;
            }
            try {
                ftpClient.changeWorkingDirectory("/");
                return true;
            } catch (Exception e) {
                logger.error("ftp validate Exception:{}", e);
                return false;
            }
        }

        @Override
        public void activateObject(PooledObject<FTPClient> p) throws Exception {
            //not implemented, currently using default
        }

        @Override
        public void passivateObject(PooledObject<FTPClient> p) throws Exception {
            //not implemented, currently using default
        }

        private FTPClient getObject(PooledObject<FTPClient> p) {
            if (p == null || p.getObject() == null) {
                return null;
            }
            return p.getObject();
        }

    }

}
