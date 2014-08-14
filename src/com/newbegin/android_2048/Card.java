import android.content.Context;
import android.widget.TextView;
public class Card extends TextView{
	
	private int value;
	private int[] color = {0x00ff0000,0x10ff0000,0x20ff0000,0x30ff0000,0x40ff0000,0x50ff0000,0x60ff0000,
			0x70ff0000,0x80ff0000,0x90ff0000,0xa0ff0000,0xb0ff0000,0xc0ff0000,0xd0ff0000,0xe0ff0000,0xf0ff0000};
	
	public Card(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		/*初始化value、number值以及初始背景色*/
		value = 0;
		setBackgroundColor(color[value]);
	}
	
	//比较是否相等
	public boolean isEqual(Card other){
		if(value == other.getValue())
			return true;
		else
			return false;
	}
	
	//value值增加
	public void plus(){
		value++;
		return;
	}

	//改变value
	public void valueChange(int task){
		value = task;
		return;
	}
	
	//刷新card代表的值以及背景色
	public void refresh(){
		if(value == 0){
			return;
		}
		else{
			int number = (int) Math.pow(2, value);
			setText(number);
			setBackgroundColor(color[value]);
		}
		
	}
	
	//获取card的value
	public int getValue(){
		return value;
	}
	
	
	
	//左滑动合并填充函数
	boolean gameLeft(){		
		if(!canMove[2])
			return true;
		haveBlank = false;
		merged = false;
		int fir,sec;
		//合并
		for(int i = 0;i < 4;i++){
			for(fir = 0;fir < 4;fir++){
				if(card[i][fir].getValue() != 0){
					sec = fir+1;
					while(sec<4){
						if(card[i][sec].getValue() != 0){
							if(card[i][fir].isEqual(card[i][sec])){
								card[i][fir].plus();
								card[i][sec].valueChange(0);
								fir = sec+1;
								merged = true;
							}
							else fir = sec;
							break;
						}
						sec++;
					}
				}
			}
		}
			
		//填充
		for(int i = 0;i < 4;i++){
			for(fir = 0;fir < 4;fir++){
				if(card[i][fir].getValue() == 0){
					haveBlank = true;
					sec = fir+1;
					while(sec <4){
						if(card[i][sec].getValue() != 0){
							card[i][fir].valueChange(card[i][sec].getValue());
							card[i][sec].valueChange(0);
							fir += 1;
						}
						sec++;
					}
				}
			}
		}
		
		//判断游戏是否结束以及各个方向滑动有没有效果
		if(merged == false){
			if(haveBlank == false){
				canMove[2] = false;
				canMove[3] = false;
				if(!vJudge()){
					return false;
				}
				canMove[0] = canMove[1] = true;
				return true;
			}
			canMove[0] = canMove[1] = canMove[3] = true;
		}
		canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
		return true;
	}
	
	//右滑动合并填充函数
	boolean gameRight(){		
		if(!canMove[3])
			return true;
		haveBlank = false;
		merged = false;
		int fir,sec;
		//合并
		for(int i = 0;i < 4;i++){
			for(fir = 3;fir >= 0;fir--){
				if(card[i][fir].getValue() != 0){
					sec = fir-1;
					while(sec >= 0){
						if(card[i][sec].getValue() != 0){
							if(card[i][fir].isEqual(card[i][sec])){
								card[i][fir].plus();
								card[i][sec].valueChange(0);
								fir = sec-1;
								merged = true;
							}
							else fir = sec;
							break;
						}
						sec--;
					}
				}
			}
		}
			
		//填充
		for(int i = 0;i < 4;i++){
			for(fir = 3;fir >= 0;fir--){
				if(card[i][fir].getValue() == 0){
					haveBlank = true;
					sec = fir-1;
					while(sec >= 0){
						if(card[i][sec].getValue() != 0){
							card[i][fir].valueChange(card[i][sec].getValue());
							card[i][sec].valueChange(0);
							fir -= 1;
						}
						sec--;
					}
				}
			}
		}
		
		//判断游戏是否结束以及各个方向滑动有没有效果
		if(merged == false){
			canMove[2] = false;
			if(haveBlank == false){
				canMove[3] = false;
				if(!vJudge()){
					return false;
				}
				canMove[0] = canMove[1] = true;
				return true;
			}
			canMove[0] = canMove[1] = canMove[3] = true;
		}
		canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
		return true;
	}
	
	//上滑动合并填充函数
	boolean gameUp(){		
		if(!canMove[2])
			return true;
		haveBlank = false;
		merged = false;
		int fir,sec;
		//合并
		for(int i = 0;i < 4;i++){
			for(fir = 0;fir < 4;fir++){
				if(card[fir][i].getValue() != 0){
					sec = fir+1;
					while(sec<4){
						if(card[sec][i].getValue() != 0){
							if(card[fir][i].isEqual(card[sec][i])){
								card[fir][i].plus();
								card[sec][i].valueChange(0);
								fir = sec+1;
								merged = true;
							}
							else fir = sec;
							break;
						}
						sec++;
					}
				}
			}
		}
			
		//填充
		for(int i = 0;i < 4;i++){
			for(fir = 0;fir < 4;fir++){
				if(card[fir][i].getValue() == 0){
					haveBlank = true;
					sec = fir+1;
					while(sec <4){
						if(card[sec][i].getValue() != 0){
							card[fir][i].valueChange(card[sec][i].getValue());
							card[sec][i].valueChange(0);
							fir += 1;
						}
						sec++;
					}
				}
			}
		}
		
		//判断游戏是否结束以及各个方向滑动有没有效果
		if(merged == false){
			canMove[2] = false;
			if(haveBlank == false){
				canMove[3] = false;
				if(!vJudge()){
					return false;
				}
				canMove[0] = canMove[1] = true;
				return true;
			}
			canMove[0] = canMove[1] = canMove[3] = true;
		}
		canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
		return true;
	}
	
	//下滑动合并填充函数
	boolean gameDown(){		
		if(!canMove[2])
			return true;
		haveBlank = false;
		merged = false;
		int fir,sec;
		//合并
		for(int i = 0;i < 4;i++){
			for(fir = 3;fir >= 0;fir--){
				if(card[fir][i].getValue() != 0){
					sec = fir-1;
					while(sec<4){
						if(card[sec][i].getValue() != 0){
							if(card[fir][i].isEqual(card[sec][i])){
								card[fir][i].plus();
								card[sec][i].valueChange(0);
								fir = sec-1;
								merged = true;
							}
							else fir = sec;
							break;
						}
						sec--;
					}
				}
			}
		}
			
		//填充
		for(int i = 0;i < 4;i++){
			for(fir = 3;fir >= 0;fir--){
				if(card[fir][i].getValue() == 0){
					haveBlank = true;
					sec = fir-1;
					while(sec <4){
						if(card[sec][i].getValue() != 0){
							card[fir][i].valueChange(card[sec][i].getValue());
							card[sec][i].valueChange(0);
							fir -= 1;
						}
						sec--;
					}
				}
			}
		}
		
		//判断游戏是否结束以及各个方向滑动有没有效果
		if(merged == false){
			canMove[2] = false;
			if(haveBlank == false){
				canMove[3] = false;
				if(!vJudge()){
					return false;
				}
				canMove[0] = canMove[1] = true;
				return true;
			}
			canMove[0] = canMove[1] = canMove[3] = true;
		}
		canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
		return true;
	}
	
	//在没有空格的情况下判断垂直方向有没有相等的相邻数
	boolean vJudge(){
		for(int i = 0;i < 4;i++){
			for(int j = 0;j < 3;j++){
				if(card[j][i].isEqual(card[j+1][i])){
					return true;
				}
			}
		}
		return false;
	}
	
	//在没有空格的情况下判断水平方向有没有相等的相邻数
	boolean hJudge(){
		for(int i = 0;i < 4;i++){
			for(int j = 0;j < 3;j++){
				if(card[i][j].isEqual(card[i][j+1])){
					return true;
				}
			}
		}
		return false;
	}
}
