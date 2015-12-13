package com.swinestudios.growingtwobuttons;

import java.util.ArrayList;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;

public class Gameplay implements GameScreen{

	public static int ID = 2;

	public float score; //Height reached is score
	public static float maxScore = 0;
	public boolean gameOver = false;
	public boolean paused = false;

	public TreeTrunk tree;
	public SpawningSystem spawner;

	public ArrayList<Projectile> projectiles;
	public ArrayList<TreeProjectile> treeProjectiles;

	@Override
	public int getId(){
		return ID;
	}

	@Override
	public void initialise(GameContainer gc){

	}

	@Override
	public void postTransitionIn(Transition t){

	}

	@Override
	public void postTransitionOut(Transition t){
		gameOver = false;
		paused = false;
		score = 0;
	}

	@Override
	public void preTransitionIn(Transition t){
		gameOver = false;
		paused = false;
		score = 0;

		treeProjectiles = new ArrayList<TreeProjectile>();
		projectiles = new ArrayList<Projectile>();
		tree = new TreeTrunk(300, 100, this); //TODO adjust position later
		spawner = new SpawningSystem(this);
		
		//TODO Testing projectiles
		/*treeProjectiles.add(new TreeProjectile(60, 0, this));
		treeProjectiles.add(new TreeProjectile(160, 38, this));
		treeProjectiles.add(new TreeProjectile(250, 16, this));
		treeProjectiles.add(new TreeProjectile(400, 60, this));
		treeProjectiles.add(new TreeProjectile(490, 4, this));
		treeProjectiles.add(new TreeProjectile(587, 27, this));*/
		//projectiles.add(new Projectile(-20, 0, 0.5f, 0.2f, this));

		//Input handling
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(tree);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void preTransitionOut(Transition t){

	}

	@Override
	public void render(GameContainer gc, Graphics g){
		renderTreeProjectiles(g);
		tree.render(g);
		renderProjectiles(g);
		tree.renderSelector(g);
		//TODO Test drawing
		g.setColor(Color.GREEN);
		g.drawRect(0, 16, 640, 40);
		g.drawRect(32, 0, 576, 80);
		//System.out.println(Gdx.input.getX() + ", " + Gdx.input.getY()); //TODO remove later
		
		//TODO adjust UI for each menu
		if(gameOver){
			g.setColor(Color.RED);
			g.drawString("You died! Press Escape to go back to the main menu", 160, 240);
		}
		if(paused){
			g.setColor(Color.RED);
			g.drawString("Are you sure you want to quit? Y or N", 220, 240);
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
		if(!paused && !gameOver){
			updateProjectiles(delta);
			updateTreeProjectiles(delta);
			tree.update(delta);
			tree.updateSelector(delta);
			spawner.update(delta);
			
			//Update score
			score += delta*100;
			if(score > maxScore){
				maxScore = score;
			}
			
			if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
				paused = true;
			}
		}
		else{
			if(gameOver){
				if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
					sm.enterGameScreen(MainMenu.ID, new FadeOutTransition(), new FadeInTransition());
				}
			}
			else if(paused){
				if(Gdx.input.isKeyJustPressed(Keys.Y)){
					sm.enterGameScreen(MainMenu.ID, new FadeOutTransition(), new FadeInTransition());
				}
				if(Gdx.input.isKeyJustPressed(Keys.N)){
					paused = false;
				}
			}
		}
	}

	public void renderProjectiles(Graphics g){
		for(int i = 0; i < projectiles.size(); i++){
			projectiles.get(i).render(g);
		}
	}

	public void updateProjectiles(float delta){
		for(int i = 0; i < projectiles.size(); i++){
			projectiles.get(i).update(delta);
		}
	}
	
	public void renderTreeProjectiles(Graphics g){
		for(int i = 0; i < treeProjectiles.size(); i++){
			treeProjectiles.get(i).render(g);
		}
	}

	public void updateTreeProjectiles(float delta){
		for(int i = 0; i < treeProjectiles.size(); i++){
			treeProjectiles.get(i).update(delta);
		}
	}

	@Override
	public void interpolate(GameContainer gc, float delta){
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResize(int arg0, int arg1) {
	}

	@Override
	public void onResume() {
	}

}
