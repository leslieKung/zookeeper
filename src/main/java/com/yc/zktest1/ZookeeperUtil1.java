package com.yc.zktest1;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

public class ZookeeperUtil1 {

	private static ZooKeeper zk;
	private static ZookeeperConnection zc = new ZookeeperConnection();

	//静态块  程序一加载到jvm就执行
	static {
		try {
			zk = zc.connect("192.168.176.132");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建节点
	 * @param path
	 * @param data
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static void create(String path, byte[] data) throws KeeperException, InterruptedException {
		zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	
	/**
	 * Return the data and the stat of the node of the given path.
	 * get
	 */
	public static void get(String path, boolean watch) throws KeeperException, InterruptedException {
		Stat stat = zk.exists(path, false);
		byte[] data = zk.getData(path,false, stat );
		String v = new String(data,0,data.length);
		System.out.println("节点   "+path+"  的值是  "+v);
	}
	
	/**
	 * set
	 */
	public static void set( String path,String content , int version) throws KeeperException, InterruptedException {
		byte[] data = content.getBytes();
		zk.setData(path, data,-1);
	}
	
	/**
	 * getAcl
	 * Return the ACL and stat of the node of the given path.
	 */
	public static void getAcl( String path) throws KeeperException, InterruptedException {
		Stat stat = zk.exists(path, false);
		List<ACL> acl = zk.getACL(path, stat );
		System.out.println(acl);
	}
	
	public static void setAcl(String path, int version) throws KeeperException, InterruptedException {
		zk.setACL(path,ZooDefs.Ids.OPEN_ACL_UNSAFE, version);
	}
	
	/**
	 * 递归删除节点    等效于Zookeeper里面的rmr deleteall 
	 * @param path
	 * @param version  = cversion
	 * if the given version is -1, it matches any node's versions
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public static void rmr(String path,int version) throws InterruptedException, KeeperException {
		zk.delete(path, version);
	}
	
	/**
	 * Return the stat of the node of the given path
	 * whether need to watch this node
	 * @param path
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	public static void stat(String path) throws KeeperException, InterruptedException{
		Stat s = zk.exists(path, false);
		System.out.println("==============stat===========");
		System.out.println(s);
	}
	
	public static void getChildren(String path, boolean watch) throws KeeperException, InterruptedException{
		List<String> children = zk.getChildren(path, false);
		System.out.println(path + " 的子节点有 "+ children);
	}
	
	public static Stat znode_exits(String path) throws KeeperException, InterruptedException {
		return zk.exists(path, true);
	}
	
	public static void update(String path,byte[] data) throws KeeperException, InterruptedException {
		zk.setData(path, data, znode_exits(path).getVersion());
	}
	
	public static void delete(String path ) throws InterruptedException, KeeperException {
		zk.delete(path, znode_exits(path).getVersion());
	}
	
	public static String find(String path) throws KeeperException, InterruptedException {
		Stat stat = znode_exits(path);
		if(stat!=null) {
			byte[] data = zk.getData(path, false, stat);
			String s = new String(data);
			return s;
		}else {
		return null;
		}
	}
	
	public static void find2(final String path) throws KeeperException, InterruptedException {
		final CountDownLatch cl = new CountDownLatch(1);
		
		final Stat stat = znode_exits(path);
		if(stat!=null) {
			byte[] data = zk.getData(path, new Watcher() {

				public void process(WatchedEvent event) {
					System.out.println("事件类型:"+event.getType());
					if(event.getType() == EventType.NodeDataChanged) {
						byte[] updateData;
						try {
							updateData = zk.getData(path, false, stat);
							String s = new String(updateData);
							System.out.println("更新后的信息："+s);
							cl.countDown();
						} catch (KeeperException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}		
					}
				}
				
			}, stat);
			String s = new String(data);
			System.out.println("连接后接收到的数据："+ s);
			
			cl.await();
			System.out.println("主程序重新启动，跳过等待，向下运行。。。。");
		}
		
	}
	
}
