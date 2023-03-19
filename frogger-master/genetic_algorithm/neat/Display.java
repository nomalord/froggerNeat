package genetic_algorithm.neat;

import core.vector.Vector2d;
import genetic_algorithm.neat.neat.Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Display extends JPanel implements Runnable{
	public static int GRID=50;
	public static int ERRORY=10;
	public static int ERRORX=16;
	public static int WIDTH=600+ERRORX;
	public static int HEIGHT=500-ERRORY;
	public enum STATE{
		MENU,
		GAME,
		HELP
	};
	public final int EASIER=-1;
	public final int HARDER=1;
	public static STATE state=STATE.MENU;
	private boolean neat=false;
	
	private Menu menu;
	private int neatScore;
	private short MapMatrix[][]; //0 = empty, 1 = obstacle, -1 = frog
	private BufferedImage image;
	private Frog frog;
	private Cars cars1[];
	private Cars cars2[];
	private Logs logs1[];
	private Logs logs2[];
	private Logs logs3[];
	private int deaths=0;
	private int score=0;

	private int previousX = 250;
	private int previousY = 400;
	private int currentY = 400;
	private int maxStartingY;

	private int numberOfSections = 4; // You can adjust this number based on your preference
	private int[] sectionYCoordinates;

	private int direction;  //0 = north, 1=west, 2=south, 3=east
	private boolean gameOver = false;
	Display(){
		frog= new Frog(250,HEIGHT-90,50,50);
		menu= new Menu();
		cars1= new Cars[2];
		cars2= new Cars[3];
		logs1= new Logs[2];
		logs2= new Logs[2];
		logs3= new Logs[2];

		MapMatrix = new short[8][12];
		maxStartingY = getCurrentY();

		sectionYCoordinates = new int[numberOfSections];
		int sectionHeight = maxStartingY / numberOfSections;
		for (int i = 0; i < numberOfSections; i++) {
			sectionYCoordinates[i] = maxStartingY - (sectionHeight * (i + 1));
		}
		
		loadMap();
		initializeGame();
		start();
		
		this.addKeyListener(menu);
		this.addMouseListener(menu);
		this.addMouseMotionListener(menu);
		this.addKeyListener(frog);
		setFocusable(true);
	}
	public void initializeGame(){
		for(int i=0;i<cars1.length;i++){
			cars1[i]= new Cars(0+i*290,HEIGHT-140,100,50,3);
		}
		for(int i=0;i<cars2.length;i++){
			cars2[i]= new Cars(0+i*270,HEIGHT-190,100,50,-2);
		}
		for(int i=0;i<logs1.length;i++){
			logs1[i]= new Logs(0+i*250,HEIGHT-290,170,50,+2);
		}
		for(int i=0;i<logs2.length;i++){
			logs2[i]= new Logs(0+i*300,HEIGHT-340,170,50,-2);
		}
		for(int i=0;i<logs3.length;i++){
		logs3[i]= new Logs(0+i*350,HEIGHT-390,170,50,+3);
		}
	}
	public void didIntersectCar(){
		for(Cars car:cars1){
			if(frog.getFrog().getBounds().intersects(car.getCar().getBounds())){
				changeDifficulty(EASIER);
				reset();
			}
		}
		for(Cars car:cars2){
			if(frog.getFrog().getBounds().intersects(car.getCar().getBounds())){
				changeDifficulty(EASIER);
				reset();
			}
		}
	}
	public void isInsideLog(){
		Logs logarray[][]=new Logs[][] {logs1,logs2,logs3};

		for (int i = 0; i < logarray.length; i++) {

			if(frog.getFrog().getCenterY()<HEIGHT-240-i*50&&frog.getFrog().getCenterY()>HEIGHT-290-i*50){
				if(!((frog.getFrog().getMinX()>logarray[i][0].getLog().getMinX()&&frog.getFrog().getMaxX()<logarray[i][0].getLog().getMaxX())||
						(frog.getFrog().getMinX()>logarray[i][1].getLog().getMinX()&&frog.getFrog().getMaxX()<logarray[i][1].getLog().getMaxX()))){
					changeDifficulty(EASIER);
					reset();
				}
				else{
					frog.mover(logarray[i][1].getSpeed());
				}
			}
		}
	}
	
	public void loadMap(){
		try {
			image= ImageIO.read(getClass().getResourceAsStream("img/map.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void score(){
		if(frog.getFrog().getCenterY()<HEIGHT-390){
			score++;
			deaths--;
			neatScore = score-deaths;
			changeDifficulty(HARDER);
			reset();
		}
	}
	public void showInfo(Graphics g){
		Graphics2D g2d= (Graphics2D)g;
		g.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.PLAIN, 18));
		g2d.drawString("Deaths: "+Integer.toString(deaths), 15, 20);
		g2d.drawString("Score: "+Integer.toString(score), 105, 20);
	}
	public void reset(){
		deaths++;
		frog.getFrog().x=250;
		frog.getFrog().y=HEIGHT-90;
		frog.setLocation(frog.getFrog().x, frog.getFrog().y);
	}
	public void AntiAliasing(Graphics g){
		Graphics2D g2d= (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	public void renderGame(Graphics g){
		g.drawImage(image, 0, 0, null);
		for(Logs log: logs1)
			log.render(g);
		for(Logs log: logs2)
			log.render(g);
		for(Logs log: logs3)
			log.render(g);
		frog.render(g);
		for(Cars car: cars1)
			car.render(g);
		for(Cars car: cars2)
			car.render(g);
	}
	@Override
	protected void paintComponent(Graphics g) {
		if(neat){
			renderGame(g);
			score();
			showInfo(g);
			didIntersectCar();
			isInsideLog();
			return;
		}
		super.paintComponent(g);
		AntiAliasing(g);
		if(state==STATE.MENU||state==STATE.HELP){
			menu.render(g);
		}else if(state==STATE.GAME){
		renderGame(g);
		score();
		showInfo(g);
		didIntersectCar();
		isInsideLog();
		}
	}


    public void start() {   			
        Thread thread = new Thread(this);
        thread.start();
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
            repaint();
            try {
                Thread.sleep(17);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}
	public boolean didIntersectCarNear(Frog frog){
		for(Cars car:cars1){
			if(frog.getFrog().getBounds().intersects(car.getCar().getBounds())){
				return true;
			}
		}
		for(Cars car:cars2){
			if(frog.getFrog().getBounds().intersects(car.getCar().getBounds())){
				return true;
			}
		}
		return false;
	}
	public boolean isInsideLogNear(Frog frog){
		Logs logarray[][]=new Logs[][] {logs1,logs2,logs3};

		for (int i = 0; i < logarray.length; i++) {

			if(frog.getFrog().getCenterY()<HEIGHT-240-i*50&&frog.getFrog().getCenterY()>HEIGHT-290-i*50){
				if(!((frog.getFrog().getMinX()>logarray[i][0].getLog().getMinX()&&frog.getFrog().getMaxX()<logarray[i][0].getLog().getMaxX())||
						(frog.getFrog().getMinX()>logarray[i][1].getLog().getMinX()&&frog.getFrog().getMaxX()<logarray[i][1].getLog().getMaxX()))){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return false;
	}

	public void changeDifficulty(int difficulty){
		if(true) return;
		if(difficulty == EASIER &&
			logs1[0].getSpeed() > 0)
			return;
		for (Cars car : cars1) {
			car.changeSpeed(car.getSpeed() + difficulty);
		}
		for (Cars cars : cars2) {
			cars.changeSpeed(cars.getSpeed() - difficulty);
		}
		for (Logs logs : logs1) {
			logs.changeSpeed(logs.getSpeed() + difficulty);
		}
		for (Logs logs : logs2) {
			logs.changeSpeed(logs.getSpeed() - difficulty);
		}
		for (Logs logs : logs3) {
			logs.changeSpeed(logs.getSpeed() + difficulty);
		}
	}

	public int sendDeaths(){
		return deaths;
	}

	public void move(Client client, boolean debug){

		if (debug)
			System.out.println("Calculating");
		double[] out = client.calculate(this.extractNetworkInput());
		if (debug)
			System.out.println("Calculating stop");
		int dir = directionFromOutput(out);

		if (debug)
			System.out.println("Moving");
		this.move(dir);
		if (debug)
			System.out.println("Moving stop");
	}
	public double[] extractNetworkInput() {
		//checkMap();
		int inputSize = 4 + cars1.length * 2 + cars2.length * 2 + logs1.length * 2 + logs2.length * 2 + logs3.length * 2;
		double[] out = new double[30];
		Frog newFrog = this.frog;
//		for(int i = 0; i < 4; i++){
//			for(int j = 0; j<13;j++){
//
//			}
//			out[i] = isObstacle(traverse(newFrog, i)) ? 1:0;
//			newFrog = this.frog;
//		}

		out[0] = isObstacle(traverse(newFrog, 1)) ? 1:0;
		newFrog = this.frog;
		out[1] = isObstacle(traverse(newFrog, 0)) ? 1:0;
		newFrog = this.frog;
		out[2] = isObstacle(traverse(newFrog, -1)) ? 1:0;
		newFrog = this.frog;
		out[3] = isObstacle(traverse(newFrog, 2)) ? 1:0;

		int index = 4;
		for (Cars car : cars1) {
			out[index++] = car.getCar().getX();
			out[index++] = car.getCar().getY();
		}
		for (Cars car : cars2) {
			out[index++] = car.getCar().getX();
			out[index++] = car.getCar().getY();
		}
		for (Logs log : logs1) {
			out[index++] = log.getLog().getX();
			out[index++] = log.getLog().getY();
		}
		for (Logs log : logs2) {
			out[index++] = log.getLog().getX();
			out[index++] = log.getLog().getY();
		}
		for (Logs log : logs3) {
			out[index++] = log.getLog().getX();
			out[index++] = log.getLog().getY();
		}


		Vector2d location = new Vector2d(frog.getFrogX(), frog.getFrogY());

		Vector2d dir = new Vector2d(traverse(newFrog).getFrogX(),traverse(newFrog).getFrogY()).sub(location);
		Vector2d target = new Vector2d(frog.getFrogX(), HEIGHT-390);
		dir.self_normalise();
		target.self_normalise();

		double angle = Math.atan2(target.getY(), target.getX()) - Math.atan2(dir.getY(), dir.getX());

		if (angle > Math.PI)        { angle -= 2 * Math.PI; }
		else if (angle <= -Math.PI) { angle += 2 * Math.PI; }

		out[29] = angle;


		return out;
	}
	public boolean isObstacle(Frog frog){
		return didIntersectCarNear(frog) || isInsideLogNear(frog);
	}

	public Frog traverse(Frog root, int control){
		switch (transformDirection(this.direction, control)){
			case 0:
				root.setFrogX(root.getFrogX());
				root.setFrogY(root.getFrogY()+50);
				return root;
			case 1:
				root.setFrogX(root.getFrogX()-50);
				root.setFrogY(root.getFrogY());
				return root;
			case 2:
				root.setFrogX(root.getFrogX());
				root.setFrogY(root.getFrogY()-50);
				return root;
			case 3:
				root.setFrogX(root.getFrogX()+50);
				root.setFrogY(root.getFrogY());
				return root;
			case 4:
				root.setFrogX(root.getFrogX());
				root.setFrogY(root.getFrogY());
				return root;
		}
		return null;
	}
	public Frog traverse(Frog root){
		switch (direction){
			case 0:
				root.setFrogX(root.getFrogX());
				root.setFrogY(root.getFrogY()+50);
				return root;
			case 1:
				root.setFrogX(root.getFrogX()-50);
				root.setFrogY(root.getFrogY());
				return root;
			case 2:
				root.setFrogX(root.getFrogX());
				root.setFrogY(root.getFrogY()-50);
				return root;
			case 3:
				root.setFrogX(root.getFrogX()+50);
				root.setFrogY(root.getFrogY());
				return root;
			case 4:
				root.setFrogX(root.getFrogX());
				root.setFrogY(root.getFrogY());
				return root;
		}
		return null;
	}
	public int transformDirection(int direction, int control){

		return (direction + control + 4) % 4;
	}
	public static int directionFromOutput(double[] out) {
		int index = 0;
		for(int i = 1; i < out.length; i++){
			if(out[i] > out[index]){
				index = i;
			}
		}
		return index -1;
	}
	public void move(int direction){
		if(gameOver) return;

		synchronized (frog){
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			previousY = frog.getFrogY();
			previousX = frog.getFrogX();
			this.direction = (this.direction + direction + 4) % 4;
			frog = traverse(frog);
			currentY = frog.getFrogY();
			frog.setLocation(frog.getFrogX(), frog.getFrogY());
			if(frog.getFrogY() > HEIGHT - 90 || frog.getFrogX() > WIDTH - 90 || frog.getFrogX() < 0){
				deaths++;
				reset();
				if(isObstacle(frog)){
					gameOver = isGameOver();
				}
			}
//			frog.changeSprite("img/frog.png");
			score();

			if(isObstacle(frog)){
				gameOver = isGameOver();
			}
		}



	}
	public void move(int direction, boolean neat){
		if(gameOver) return;

		synchronized (frog){
			previousY = frog.getFrogY();
			previousX = frog.getFrogX();
			this.direction = (this.direction + direction + 4) % 4;
			frog.setLocation(frog.getFrogX(), frog.getFrogY());
			currentY = frog.getFrogY();
			frog = traverse(frog);
			frog.setLocation(frog.getFrogX(), frog.getFrogY());
			if(frog.getFrogY() > HEIGHT - 90 || frog.getFrogX() > WIDTH - 90 || frog.getFrogX() < 0){
				deaths++;
				reset();
				if(isObstacle(frog)){
					gameOver = isGameOver();
				}
			}
			frog.changeSprite("img/frog.png");
			score();

			if(isObstacle(frog)){
				gameOver = isGameOver();
			}
		}



	}
	public boolean isGameOver() {
		return deaths > 10;
	}

	public int getScore(){
		return score;
	}

	public int getNeatScore(){
		return neatScore;
	}

	public void SetNeat(boolean neat){
		this.neat = neat;
	}

	public void move(Client client, boolean debug,boolean neat){
		if (debug)
			System.out.println("Calculating");
		double[] out = client.calculate(this.extractNetworkInput());
		if (debug)
			System.out.println("Calculating stop");
		int dir = directionFromOutput(out);

		if (debug)
			System.out.println("Moving");
		this.move(dir, true);
		if (debug)
			System.out.println("Moving stop");
	}

	public void setDeaths(int deaths){
		this.deaths = deaths;
	}
	public void setScore(int score){
		this.score = score;
	}

	public int getCurrentY() {
		return currentY;
	}

	public int getPreviousY() {
		return previousY;
	}
	public int getCurrentX() {
		return frog.getFrogX();
	}
	public int getPreviousX() {
		return previousX;
	}


	public int getDistanceScore() {
		// Calculate the distance traveled towards the destination
		int currentCheck = getCurrentY();
		int distanceTraveled = maxStartingY - getCurrentY();

		// You can use a constant multiplier to adjust the score impact of the distance traveled
		int distanceScore = distanceTraveled * 10;

		return distanceScore < 0 ? 0 : distanceScore;
	}

	public int getCheckpointReward() {
		int reachedSection = -1;
		for (int i = 0; i < numberOfSections; i++) {
			if (getCurrentY() <= sectionYCoordinates[i]) {
				reachedSection = i;
				break;
			}
		}

		// Reward the AI for reaching each section
		int rewardPerSection = 100; // You can adjust this value based on your preference
		int checkpointReward = (reachedSection + 1) * rewardPerSection;
		return checkpointReward;
	}
	public int getTimeBasedReward(int iteration) {
		int maxIterations = 5000;
		int maxReward = 500; // You can adjust this value based on your preference

		// Calculate the time-based reward
		int reward = (int) ((1.0 - ((double) iteration / (double) maxIterations)) * maxReward);
		return reward;
	}


	public int GetDeaths() {
		return deaths;
	}



	public void checkMap(){
		//check car placement
		for (Cars car : cars1) {
			MapMatrix[(int)car.getCar().getY()/50 - 1][(int)car.getCar().getX()/50] = 1;
			if(((int)car.getCar().getX()/50 + 50) < 600){
				MapMatrix[(int)car.getCar().getY()/50 - 1][((int)car.getCar().getX()+ 50)/50] = 1;
			}
		}
		for (Cars car : cars2) {
			if((int)car.getCar().getX()/50 > 0)
				MapMatrix[(int)car.getCar().getY()/50 - 1][(int)car.getCar().getX()/50] = 1;
			if(((int)car.getCar().getX()/50 + 50) < 600){
				MapMatrix[(int)car.getCar().getY()/50 - 1][((int)car.getCar().getX() + 50)/50] = 1;
			}
		}
		//check log placement and mark with 5s
		for (Logs log : logs1) {
			if(log.getLog().getX()/50 % 1 == 0 && log.getLog().getX()/50 > 0)
				MapMatrix[(int)log.getLog().getY()/50 - 1][(int)log.getLog().getX()/50] = 5;

			if((log.getLog().getX()/50 + 100) % 1 == 0 && log.getLog().getX()/50 < 600) {
				MapMatrix[(int) log.getLog().getY() / 50 - 1][((int) log.getLog().getX() + 50) / 50] = 5;
				MapMatrix[(int) log.getLog().getY() / 50 - 1][((int) log.getLog().getX() + 100) / 50] = 5;
			}

		}
		for (Logs log : logs2) {
			if(log.getLog().getX()/50 % 1 == 0 && log.getLog().getX()/50 < 600)
				MapMatrix[(int)log.getLog().getY()/50 - 1][(int)log.getLog().getX()/50] = 5;

			if((log.getLog().getX()/50 + 100) % 1 == 0 && log.getLog().getX()/50 > 0) {
				MapMatrix[(int) log.getLog().getY() / 50 - 1][((int) log.getLog().getX() - 50) / 50] = 5;
				MapMatrix[(int) log.getLog().getY() / 50 - 1][((int) log.getLog().getX() - 100) / 50] = 5;
			}

		}
		for (Logs log : logs3) {
			if(log.getLog().getX()/50 % 1 == 0 && log.getLog().getX()/50 > 0)
				MapMatrix[(int)log.getLog().getY()/50 - 1][(int)log.getLog().getX()/50] = 5;

			if((log.getLog().getX()/50 + 100) % 1 == 0 && log.getLog().getX()/50 < 600) {
				MapMatrix[(int) log.getLog().getY() / 50 - 1][((int) log.getLog().getX() + 50) / 50] = 5;
				MapMatrix[(int) log.getLog().getY() / 50 - 1][((int) log.getLog().getX() + 100) / 50] = 5;
			}

		}
		//reverse log placement (logs are not obstacles)
		for(int i = 1; i <= 3; i++){
			for(int j = 0; j < 12; j++){
				if(MapMatrix[i][j] == 5){
					MapMatrix[i][j] = 0;
				}
				else
					MapMatrix[i][j] = 1;
			}
		}
		//check turtle placement and mark with -1
		MapMatrix[frog.getFrogY()/50 - 1][frog.getFrogX()/50] = -1;
	}


}



