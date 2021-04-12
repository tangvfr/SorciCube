package fr.tangv.sorcicubecore.configs;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class LevelConfig extends AbstractConfig {

	public IntegerConfig moneyStart;
	public IntegerConfig experienceWin;
	public IntegerConfig moneyWin;
	public IntegerConfig experienceLoss;
	public IntegerConfig moneyLoss;
	public IntegerConfig experienceEquality;
	public IntegerConfig moneyEquality;
	public IntegerConfig maxLevel;
	public StringConfig experienceCalc;
	public StringConfig rewardCalc;

	/*	
	  	this.experienceFunction.value = "40*Math.pow(1.1007, {lvl}-2)";
		this.rewardFunction.value = "10*Math.pow(1.107, {lvl}-2)";
    	+png 300piece
    */
	
	private int[] experiences;
	private int[] rewards;
	
	public LevelConfig(Document doc) throws ConfigParseException {
		super(doc);
		try {
			calculate();
		} catch (NumberFormatException | ScriptException e) {
			throw new ConfigParseException(e);
		}
	}
	
	public void calculate() throws NumberFormatException, ScriptException {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine script = manager.getEngineByName("JavaScript");
		this.experiences = new int[maxLevel.value-1];
		this.rewards = new int[maxLevel.value-1];
		for (int i = 2; i <= maxLevel.value; i++) {
			experiences[i-2] = (int) Double.parseDouble(script.eval(experienceCalc.value.replace("{lvl}", Integer.toString(i))).toString());
			rewards[i-2] = (int) Double.parseDouble(script.eval(rewardCalc.value.replace("{lvl}", Integer.toString(i))).toString());
		}
	}
	
	public int getExperience(int lvl) {
		return experiences[lvl-2];
	}
	
	public int getReward(int lvl) {
		return rewards[lvl-2];
	}
	
}