package com.example.manage.util.machine;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * @avthor 潘小章
 * @date 2023/5/10
 */

public class ConveyorBeltSimulation {
    private static final int BELT_LENGTH = 10; // 输送带长度
    private static final int BELT_SPEED = 1; // 输送带速度
    private static final int ITEM_WEIGHT = 1; // 物品重量
    private static final int ITEM_VOLUME = 1; // 物品体积
    private static final int MAX_ITEMS = 5; // 输送带上最多可同时运输的物品数
    private static final int PRODUCER_DELAY = 1000; // 生产者生产物品的时间间隔
    private static final int CONSUMER_DELAY = 2000; // 消费者取走物品的时间间隔

    private static BlockingQueue<Item> belt = new ArrayBlockingQueue<>(BELT_LENGTH); // 使用阻塞队列来模拟输送带
    private static Semaphore spaceOnBelt = new Semaphore(BELT_LENGTH - MAX_ITEMS, true); // 用信号量来控制输送带上的物品数
    private static Semaphore itemsOnBelt = new Semaphore(0, true); // 用信号量来控制输送带上的物品数

    public static void main(String[] args) {
        new Thread(new Producer()).start(); // 启动生产者线程
        new Thread(new Consumer()).start(); // 启动消费者线程
    }

    // 生产者线程
    private static class Producer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    spaceOnBelt.acquire(); // 获取一个空位
                    Item item = new Item(ITEM_WEIGHT, ITEM_VOLUME);
                    belt.put(item); // 将物品放到输送带上
                    System.out.println("Producer produced item: " + item);
                    itemsOnBelt.release(); // 释放一个物品信号量
                    Thread.sleep(PRODUCER_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 消费者线程
    private static class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    itemsOnBelt.acquire(); // 获取一个物品
                    Item item = belt.take(); // 从输送带上取走物品
                    System.out.println("Consumer consumed item: " + item);
                    spaceOnBelt.release(); // 释放一个空位信号量
                    Thread.sleep(CONSUMER_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 物品类
    private static class Item {
        private int weight;
        private int volume;

        public Item(int weight, int volume) {
            this.weight = weight;
            this.volume = volume;
        }

        public int getWeight() {
            return weight;
        }

        public int getVolume() {
            return volume;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "weight=" + weight +
                    ", volume=" + volume +
                    '}';
        }
    }
}
