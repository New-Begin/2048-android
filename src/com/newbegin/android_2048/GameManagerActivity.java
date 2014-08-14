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
}
