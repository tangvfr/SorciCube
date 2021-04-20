package fr.tangv.sorcicubecore.configs;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.IntegerConfig;
import fr.tangv.sorcicubecore.config.StringConfig;

public class LevelConfig extends AbstractConfig {
	
	public IntegerConfig numberDeckStart;
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
	
	private int[] experiences;
	private int[] rewards;
	private boolean calculatingError;
	
	public LevelConfig(Document doc) throws ConfigParseException {
		super(doc);
		calculate();
	}
	
	public void calculate() {
		try {
			ScriptEngine script = new ScriptEngineManager().getEngineByName("JavaScript");
			if (script == null)
				throw new NumberFormatException("ScriptEngine is null !");
			if (maxLevel.value < 1)
				throw new NumberFormatException("MaxLevel < 1");
			this.experiences = new int[maxLevel.value-1];
			this.rewards = new int[maxLevel.value-1];
			for (int i = 2; i <= maxLevel.value; i++) {
				experiences[i-2] = (int) Double.parseDouble(script.eval(experienceCalc.value.replace("{lvl}", Integer.toString(i))).toString());
				rewards[i-2] = (int) Double.parseDouble(script.eval(rewardCalc.value.replace("{lvl}", Integer.toString(i))).toString());
			}
			this.calculatingError = false;
		} catch (Exception e) {
			e.printStackTrace();
			this.calculatingError = true;
		}
	}
	
	public int getExperience(int lvl) {
		return experiences[lvl-2];
	}
	
	public int getReward(int lvl) {
		return rewards[lvl-2];
	}
	
	public boolean hasCalculatingError() {
		return calculatingError;
	}
	
}