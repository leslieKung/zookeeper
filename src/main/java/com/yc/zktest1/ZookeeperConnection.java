package com.yc.zktest1;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperConnection {

	private static ZooKeeper zk;
	final CountDownLatch connectSignal = new CountDownLatch(1);

	public ZooKeeper connect(String host) throws IOException, InterruptedException {

		zk = new ZooKeeper(host, 5000, new Watcher() {
			// 回调方法 //event ：事件
			public void process(WatchedEvent event) {
				System.out.println("事件发生了，详情如下：");
				System.out.println(event.getType());
				System.out.println(event.getState());
				System.out.println(event.getPath());
				if (event.getState() == KeeperState.SyncConnected) {
					System.out.println("客户机与zk连接成功。。。。");
					connectSignal.countDown();
					System.out.println("递减后的值" + connectSignal.getCount());
				}
			}

		});
		connectSignal.await();
		System.out.println("主程序创建zk对象成功");
		return zk;

	}

	public static void main(String[] args) throws IOException, InterruptedException {
		ZookeeperConnection zc = new ZookeeperConnection();
		ZooKeeper zk = zc.connect("192.168.176.132");
		System.out.println(zk);
		zc.close();
	}

	private void close() throws InterruptedException {
		zk.close();
	}

}
