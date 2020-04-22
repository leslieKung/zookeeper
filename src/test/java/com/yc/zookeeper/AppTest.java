package com.yc.zookeeper;

import org.apache.zookeeper.KeeperException;

import com.yc.zktest1.ZookeeperUtil1;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest extends TestCase {

	public AppTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

/*	public void testCreate() throws KeeperException, InterruptedException {
		ZookeeperUtil1.create("/yc5556", "BigDataClass".getBytes());
	}*/
	
/*	public void testRmr() throws KeeperException, InterruptedException {
		ZookeeperUtil1.rmr("/kh/a", 0);
	}*/
	
/*	public void testGet() throws KeeperException, InterruptedException {
		ZookeeperUtil1.get("/kh/a", false);
	}*/
	
/*	public void testSet() throws KeeperException, InterruptedException {
		ZookeeperUtil1.set("/kh/a", "a",-1);
	}*/
		
	/*public void testStat() throws KeeperException, InterruptedException {
		ZookeeperUtil1.stat("/kh/a", false);
	}*/
	
/*	public void testGetAcl() throws KeeperException, InterruptedException {
		ZookeeperUtil1.getAcl("/kh/a");
	}*/
	
/*	public void testSetAcl() throws KeeperException, InterruptedException {
		ZookeeperUtil1.setAcl("/kh/a",2);
	}*/
	
	public void testChildren() throws KeeperException, InterruptedException {
		ZookeeperUtil1.getChildren("/kh",false);
	}
	
}
