package com.newbegin.android_2048;

import java.util.Stack;

public class HistoryStack extends Stack<int[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 961640924660228196L;

	@Override
	public synchronized int[] pop() {
		// TODO Auto-generated method stub
			return super.pop();
	}

	@Override
	public int[] push(int[] object) {
		// TODO Auto-generated method stub
		//��ʷջ��Ϊ�գ���������Ԫ�ء�
		System.out.println(empty());
		clearStack();
		return super.push(object);
	}
	
	public void clearStack()
	{
		if(!empty())
		{
			pop();
		}
	}
	

}
