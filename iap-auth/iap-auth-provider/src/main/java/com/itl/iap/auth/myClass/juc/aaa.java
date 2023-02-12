package com.itl.iap.auth.myClass.juc;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class aaa {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //配置连接
        final Properties properties = new Properties();
        final Object put = properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092");
        //反序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //配置消费者组id
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");


        //设置分区 分配策略
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
//自动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        kafkaConsumer.commitAsync();
        kafkaConsumer.commitSync();


        //指定位置进行消费
        Set<TopicPartition> assignment = kafkaConsumer.assignment();
//指定消费的offset
        for (TopicPartition topicPartition : assignment) {
            kafkaConsumer.seek(topicPartition, 100);
        }

        //希望把时间转换为对应的offset
        HashMap<TopicPartition, Long> topicPartitionLongHashMap = new HashMap<>();
//封装对应集合
        for (TopicPartition topicPartition : assignment) {
            topicPartitionLongHashMap.put(topicPartition, System.currentTimeMillis() - 1 * 24 * 3600 * 1000);
        }
        Map<TopicPartition, OffsetAndTimestamp> topicPartitionOffsetAndTimestampMap = kafkaConsumer.offsetsForTimes(topicPartitionLongHashMap);
//指定消费的offset
        for (TopicPartition topicPartition : assignment) {
            OffsetAndTimestamp offsetAndTimestamp = topicPartitionOffsetAndTimestampMap.get(topicPartition);
            kafkaConsumer.seek(topicPartition, offsetAndTimestamp.offset());
        }

//提交时间间隔
            properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);

            //创建消费者

            //订阅主题first
            ArrayList<String> topics = new ArrayList<>();
            topics.add("first");
            kafkaConsumer.subscribe(topics);

            // 2订阅主题对应的分区
            ArrayList<TopicPartition> topicPartitions = new ArrayList<>();
            topicPartitions.add(new TopicPartition("first", 0));
            kafkaConsumer.assign(topicPartitions);


            //3消费数据
            while (true) {
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    System.out.println(consumerRecord);
                }
            }


            // 指定k v 序列化类型
//            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//            properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.itl.iap.auth.myClass.juc.config");
//
//            properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);//缓冲区
//            properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);//批次
//            properties.put(ProducerConfig.LINGER_MS_CONFIG, 5);//延迟
//            properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");//压缩 gzip/snappy/lz4/ztsd
//            properties.put(ProducerConfig.ACKS_CONFIG, 1);
//            properties.put(ProducerConfig.RETRIES_CONFIG, 1);//重试次数
//
//            properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transaction");

            //创建连接
//            final KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);

            //发送
//            kafkaProducer.send(new ProducerRecord<>("first", "aaa"), new Callback() {
//                @Override
//                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//
//                    if (e != null) {
//                        System.out.println("主题" + recordMetadata.topic() + "分区" + recordMetadata.partition());
//                    }
//                }
//            }).get();
//            kafkaProducer.send(new ProducerRecord<>("first", 1, "a", "1")).get();
//
//
//            kafkaProducer.initTransactions();
//            kafkaProducer.beginTransaction();
//            kafkaProducer.commitTransaction();
//            kafkaProducer.abortTransaction();//终止事务
//
//
//            //在事务内提交已经消费的偏移量(主要用于消费者)
////        kafkaProducer.sendOffsetsToTransaction();
//
//            kafkaProducer.close();

    }
    }
