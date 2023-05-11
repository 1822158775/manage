package com.example.manage.util.machine;

/**
 * @avthor 潘小章
 * @date 2023/5/10
 */
import java.util.ArrayList;
import java.util.List;

class Item {
    private String name;
    private int position;  // 物品在输送带上的位置

    public Item(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

class ConveyorBelt {
    private List<Item> items;  // 输送带上的物品
    private int length;        // 输送带长度
    private boolean isRunning; // 输送带是否正在运行

    public ConveyorBelt(int length) {
        this.length = length;
        this.items = new ArrayList<>();
        this.isRunning = false;
    }

    public synchronized boolean addItem(Item item) {
        // 将物品放在输送带上
        if (items.size() >= length) {
            // 输送带已满，无法添加物品
            return false;
        }
        items.add(item);
        return true;
    }

    public synchronized boolean removeItem(Item item) {
        // 从输送带上移除物品
        return items.remove(item);
    }

    public synchronized void start() {
        // 启动输送带运行
        if (!isRunning) {
            isRunning = true;
            new Thread(new ConveyorBeltRunner()).start();
        }
    }

    public synchronized void stop() {
        // 停止输送带运行
        isRunning = false;
    }

    public synchronized boolean isRunning() {
        // 检查输送带是否正在运行
        return isRunning;
    }

    private class ConveyorBeltRunner implements Runnable {
        public void run() {
            while (isRunning) {
                for (Item item : items) {
                    // 将物品向前移动一格
                    item.setPosition(item.getPosition() + 1);
                    System.out.println(item.getName() + " 在输送带上向前移动到了位置 " + item.getPosition());
                    if (item.getPosition() == length) {
                        // 物品到达终点
                        System.out.println(item.getName() + " 到达了终点");
                        removeItem(item);
                    }
                }
                try {
                    Thread.sleep(1000); // 等待1秒钟
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

public class ConveyorBeltSystem {
    public static void main(String[] args) {
        // 创建两个输送带对象
        ConveyorBelt belt1 = new ConveyorBelt(5);
        ConveyorBelt belt2 = new ConveyorBelt(7);

        // 在输送带上放置若干个物品
        Item item1 = new Item("A", 0);
        Item item2 = new Item("B", 2);
        Item item3 = new Item("C", 4);
        Item item4 = new Item("D", 1);
        Item item5 = new Item("E", 3);

        belt1.addItem(item1);
        belt1.addItem(item2);
        belt1.addItem(item3);
        belt2.addItem(item4);
        belt2.addItem(item5);

        // 启动输送带运行
        belt1.start();
        belt2.start();

        // 等待一段时间后停止输送带运行
        try {
            Thread.sleep(5000); // 等待5秒钟
            belt1.stop();
            belt2.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

