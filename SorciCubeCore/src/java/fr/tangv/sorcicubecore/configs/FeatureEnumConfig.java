package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class FeatureEnumConfig extends AbstractConfig {

	public StringConfig skin;
	public StringConfig health;
	public StringConfig damage;
	public StringConfig destruct;
	public StringConfig takeNewCard;
	public StringConfig copyCardArenaPose;
	public StringConfig copyCardArena;
	public StringConfig heal;
	public StringConfig boostDamage;
	public StringConfig boostDamageNeg;
	public StringConfig boostHealth;
	public StringConfig boostHealthNeg;
	public StringConfig boostMana;
	public StringConfig boostManaNeg;
	public StringConfig removeManaHero;
	public StringConfig removeManaHeroNeg;
	public StringConfig incitement;
	public StringConfig excited;
	public StringConfig invulnerability;
	public StringConfig immobilization;
	public StringConfig stunned;
	public StringConfig giveCard;
	public StringConfig ifAttackedExecOne;
	public StringConfig ifAttackedExec;
	public StringConfig ifAttackedGiveOne;
	public StringConfig ifAttackedGive;
	public StringConfig invocation;
	public StringConfig actionSpawn;
	public StringConfig actionDead;
	public StringConfig execute;
	public StringConfig applyExcited;
	public StringConfig giveFeatureCard;
	public StringConfig metamorphTo;
	public StringConfig removeCard;
	public StringConfig hideCard;

	public FeatureEnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}