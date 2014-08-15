package com.newbegin.android_2048;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GameManagerActivity extends Activity {
	
	private GameLayout gameView; //contain a double dimensional array gameView numArray;
	private ControlLayout controlPanel;
	
	private HistoryStack historyRecord = new HistoryStack();//a stack that remains past gameView.numArray;
	
	private SharedPreferences gameHistory;
	private SharedPreferences.Editor historyEditor;
	
	private int currentScore;//record current score
	private int highScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//refer gameView and controlPanel
		gameView = (GameLayout)findViewById(R.layout.game_gird_layout);
		controlPanel = (ControlPanel)findViewById(R.layout.game_control_layout);
		
		//register the listener
		gameView.setOnTouchListener(new onTouchListener(){
			public void onTouch()
			{
				//merge equal numbers
				/*
				 * 
				 *
				 * 	historyRecord.push();
				 * 	boolean win = gameView.merge();
				 * 	gameView.refreshView();
				 * 	currentScore = gameView.getScore();
				 * 	controlPanel.refreshScore();
				 * 
				 *
				 * if(!win)
				 * {
				 * //pop a dialog  which prompts that game is over. modify the highscore.
				 * 	if(currentScore > highScore)
				 * 	{
				 * 		historyEditor.putInt("highScore",currentScore);
				 * 		historyEditor.commit();
				 * 	}
				 * }
				 * 
				 * 	
				 * 	
				 */
			}
		});
	}	
	
	/**
	 * initialize the background double dimensional array, the foreground GameView
	 */
	private void init()
	{
		//1.load the history high score;
		gameHistory = this.getSharedPreferences("gameHistory",Context.MODE_PRIVATE);
		historyEditor = gameHistory.edit();
		highScore = gameHistory.getInt("highScore", 0);
		controlPanel.setScore(highScore);
		/*2.initialize data.  
			gameView.initAarry();
		*/
		/*3.refresh UI.
		 	gameView.refreshView();
		 */
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
