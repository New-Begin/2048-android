import android.content.Context;
import android.widget.TextView;
public class Card extends TextView{
	
	private int value;
	private int[] color = {0x00ff0000,0x10ff0000,0x20ff0000,0x30ff0000,0x40ff0000,0x50ff0000,0x60ff0000,
			0x70ff0000,0x80ff0000,0x90ff0000,0xa0ff0000,0xb0ff0000,0xc0ff0000,0xd0ff0000,0xe0ff0000,0xf0ff0000};
	
	public Card(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		/*��ʼ��value��numberֵ�Լ���ʼ����ɫ*/
		value = 0;
		setBackgroundColor(color[value]);
	}
	
	//��ʼ��
	public void init(){
		value = 0;
		setBackgroundColor(color[value]);
	}
	
	//�Ƚ��Ƿ����
	public boolean isEqual(Card other){
		if(value == other.getValue())
			return true;
		else
			return false;
	}
	
	//valueֵ����
	public void plus(){
		value++;
		return;
	}

	//�ı�value
	public void valueChange(int task){
		value = task;
		return;
	}
	
	//ˢ��card�����ֵ�Լ�����ɫ
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
	
	//��ȡcard��value
	public int getValue(){
		return value;
	}
	
	
	
	//�󻬶��ϲ���亯��
	boolean gameLeft(){		
		if(!canMove[2])
			return true;
		haveBlank = false;
		merged = false;
		int fir,sec;
		//�ϲ�
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
			
		//���
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
		
		//�ж���Ϸ�Ƿ�����Լ��������򻬶���û��Ч��
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
	
	//�һ����ϲ���亯��
	boolean gameRight(){		
		if(!canMove[3])
			return true;
		haveBlank = false;
		merged = false;
		int fir,sec;
		//�ϲ�
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
			
		//���
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
		
		//�ж���Ϸ�Ƿ�����Լ��������򻬶���û��Ч��
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
	
	//�ϻ����ϲ���亯��
	boolean gameUp(){		
		if(!canMove[2])
			return true;
		haveBlank = false;
		merged = false;
		int fir,sec;
		//�ϲ�
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
			
		//���
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
		
		//�ж���Ϸ�Ƿ�����Լ��������򻬶���û��Ч��
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
	
	//�»����ϲ���亯��
	boolean gameDown(){		
		if(!canMove[2])
			return true;
		haveBlank = false;
		merged = false;
		int fir,sec;
		//�ϲ�
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
			
		//���
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
		
		//�ж���Ϸ�Ƿ�����Լ��������򻬶���û��Ч��
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
	
	//��û�пո��������жϴ�ֱ������û����ȵ�������
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
	
	//��û�пո��������ж�ˮƽ������û����ȵ�������
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
