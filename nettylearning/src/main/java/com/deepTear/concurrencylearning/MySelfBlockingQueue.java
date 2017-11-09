package com.deepTear.concurrencylearning;

import java.util.concurrent.CountDownLatch;

/**
 * 简单的阻塞队列
 * @author Administrator
 *
 */
public class MySelfBlockingQueue {

	int capacity = 10;
	int[] container;
	int headIndex = 0;//队列头部位置
	int tailIndex = 0;//队列尾部位置
	int size = 0;

	MySelfBlockingQueue(){
		container = new int[capacity];
	}

	MySelfBlockingQueue(int capacity){
		this.capacity = capacity;
		container = new int[capacity];
	}

	public synchronized int getSize(){
		return size;
	}

	public synchronized void putData(int data){
		while(getSize() == capacity){
			try {
				System.out.println("阻塞中-----------------");
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		container[tailIndex] = data;
		size++;
		tailIndex = ++tailIndex == capacity ? 0 : tailIndex;
		System.out.println("添加成功");
		this.notifyAll();
	}

	public synchronized int getData(){
		while(getSize() == 0){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int rdata = container[headIndex];
		container[headIndex] = 0;
		size--;
		headIndex = ++headIndex == capacity ? 0 : headIndex;
		this.notifyAll();
		return rdata;
	}

	public void clearQueue(){
		if(getSize() > 0){
			container = new int[capacity];
			headIndex = 0;
			tailIndex = 0;
			size = 0;
		}
	}

	public String getContent() {
		String str = "[";
		for(int i = 0 ; i < container.length ; i++){
			if(i < container.length - 1){
				str += container[i] + ",";
			}else{
				str += container[i];
			}
		}
		str += "]";
		return str;
	}

	public static void main(String[] args) {
		final MySelfBlockingQueue queue = new MySelfBlockingQueue(6);
		final CountDownLatch cdl = new CountDownLatch(2);
		new Thread(new Runnable() {
			public void run() {
				for(int i = 1 ; i < 8 ; i++){
					queue.putData(i);
					System.out.println(queue.getContent());
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				cdl.countDown();
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(7000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(queue.getData());
				System.out.println(queue.getContent());
				cdl.countDown();
			}
		}).start();
		try {
			cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("------------------------");
	}
}
