package com.yc.zkProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Client implements Watcher{
	ZooKeeper zk ;
	String hostPort;
	
	Client(String hostPort){
		this.hostPort = hostPort;
	}
	
	public void startZk() {
		try {
			zk = new ZooKeeper(hostPort,15000,this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listWorkersWatcher(Notifier notifier) throws KeeperException, InterruptedException {
		boolean flag = true;
		while(flag) {
			List<String> list = new ArrayList<String>();
			for(String w : zk.getChildren("/workers", false)) {
				byte data[] = zk.getData("/workers/" + w, false, null);
				String state = new String(data);
				list.add(state);
			}
			System.gc();
			notifier.notify(list);
			Thread.sleep(10000);
		}
	}
	
	public static void main(String[] args) throws KeeperException, InterruptedException {
		Client c = new Client("192.168.176.129");
		c.startZk();
		c.listWorkersWatcher(new Notifier() {
			public void notify(List<String> onLineWorkers) {
				System.out.println(onLineWorkers);
			}	
		});
		System.out.println("程序结束运行");
	}
	
	public void process(WatchedEvent event) {
		if(event.getType() == Watcher.Event.EventType.None) {
			System.out.println("连接服务器成功！");
		}else if(event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
			System.out.println("子节点发生改变。。。");
		}else if(event.getType() == Watcher.Event.EventType.NodeCreated) {
			System.out.println("节点创建成功！");
		}else if(event.getType() == Watcher.Event.EventType.NodeDeleted) {
			System.out.println("节点删除成功！");
		}else if(event.getType() == Watcher.Event.EventType.NodeDataChanged) {
			System.out.println("更新节点数据成功");
		}
	}
	
	public interface Notifier {
		public void notify(List<String> onLineWorkers);
	}


}
