package com.deepTear.concurrencylearning;

/**
 * 环形队列
 * @author Administrator
 *
 */
public class MySelfQueue {

	int capacity = 10;
	int[] container;
	int headIndex = 0;//队列头部位置
	int nextTailIndex = 0;//队列尾部下一个位置
	int tailIndex = 0;//队列尾部位置
	int size = 0;

	MySelfQueue(){
		container = new int[capacity];
	}

	MySelfQueue(int capacity){
		this.capacity = capacity;
		container = new int[capacity];
	}

	public synchronized int getSize(){
		return size;
	}

	public synchronized boolean putData(int data){
		if(getSize() < capacity){
			container[nextTailIndex] = data;
			tailIndex = nextTailIndex;
			size++;
			if(nextTailIndex + 1 == capacity){
				nextTailIndex = 0;
			}else{
				nextTailIndex++;
			}
			return true;
		}
		throw new RuntimeException("队列已满");
	}

	public synchronized int getData(){
		if(getSize() > 0){
			int rdata = container[headIndex];
			container[headIndex] = 0;
			size--;

			if(getSize() == 0){
				headIndex = 0;
				nextTailIndex = 0;
				tailIndex = 0;
			}else{
				if(headIndex + 1 == capacity){
					headIndex = 0;
				}else{
					headIndex++;
				}
			}
			return rdata;
		}
		throw new RuntimeException("队列为空");
	}

	public void clearQueue(){
		if(getSize() > 0){
			container = new int[capacity];
			headIndex = 0;
			tailIndex = 0;
			size = 0;
			nextTailIndex = 0;
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
		MySelfQueue queue = new MySelfQueue(5);
		queue.putData(1);
		queue.putData(2);
		queue.putData(3);
		queue.putData(4);
		queue.putData(5);
		System.out.println(queue.getContent());//[1,2,3,4,5]
		System.out.println("head:" + queue.headIndex + "   tail:" + queue.tailIndex);// head:0   tail:4
		System.out.println("出队列的值" + queue.getData() + "  head:" + queue.headIndex + "   tail:" + queue.tailIndex);//出队列的值 1  head:1   tail:4
		System.out.println(queue.getContent());//[0,2,3,4,5]
		System.out.println("出队列的值" + queue.getData() + "  head:" + queue.headIndex + "   tail:" + queue.tailIndex);//出队列的值 2  head:2   tail:4
		System.out.println(queue.getContent());//[0,0,3,4,5]
		queue.putData(6);
		System.out.println("head:" + queue.headIndex + "   tail:" + queue.tailIndex);// head:2   tail:0
		System.out.println(queue.getContent());//[6,0,3,4,5]
		queue.putData(7);
		System.out.println("head:" + queue.headIndex + "   tail:" + queue.tailIndex);// head:2   tail:1
		System.out.println(queue.getContent());//[6,7,3,4,5]


		System.out.println("出队列的值" + queue.getData() + "  head:" + queue.headIndex + "   tail:" + queue.tailIndex);//出队列的值 3  head:3   tail:1
		System.out.println(queue.getContent());//[6,7,0,4,5]
		System.out.println("出队列的值" + queue.getData() + "  head:" + queue.headIndex + "   tail:" + queue.tailIndex);//出队列的值 4  head:4   tail:1
		System.out.println(queue.getContent());//[6,7,0,0,5]
		System.out.println("出队列的值" + queue.getData() + "  head:" + queue.headIndex + "   tail:" + queue.tailIndex);//出队列的值 5  head:0   tail:1
		System.out.println(queue.getContent());//[6,7,0,0,0]
		System.out.println("出队列的值" + queue.getData() + "  head:" + queue.headIndex + "   tail:" + queue.tailIndex);//出队列的值 6  head:1   tail:1
		System.out.println(queue.getContent());//[0,7,0,0,0]
		queue.putData(8);
		System.out.println(queue.getSize());
		System.out.println(queue.getContent());//[0,7,8,0,0]
		System.out.println("出队列的值" + queue.getData() + "  head:" + queue.headIndex + "   tail:" + queue.tailIndex);//出队列的值 7  head:2   tail:2
		System.out.println(queue.getContent());//[0,0,8,0,0]
		System.out.println("出队列的值" + queue.getData() + "  head:" + queue.headIndex + "   tail:" + queue.tailIndex);//出队列的值 8  head:0   tail:0
		System.out.println(queue.getContent());//[0,0,0,0,0]

		System.out.println(queue.getSize());

	}
}
