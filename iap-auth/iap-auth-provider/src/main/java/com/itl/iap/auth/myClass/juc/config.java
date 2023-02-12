package com.itl.iap.auth.myClass.juc;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * @author cch
 * @date 2021/8/17$
 * @since JDK1.8
 */
public class config implements Partitioner {

    @Override
    public int partition(String topic, Object k, byte[] bytes, Object v, byte[] bytes1, Cluster cluster) {
        if (v.toString().contains("aaa")) {
            return 0;
        }
        //返回partition
        return 1;
    }

    @Override
    public void close() {

    }


    @Override
    public void configure(Map<String, ?> map) {

    }
}
