package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class FeatureEnumConfig extends AbstractConfig {

	public StringConfig SKIN;
	public StringConfig HEALTH;
	public StringConfig DAMAGE;
	public StringConfig DESTRUCT;
	public StringConfig TAKE_NEW_CARD;
	public StringConfig COPY_CARD_ARENA_POSE;
	public StringConfig COPY_CARD_ARENA;
	public StringConfig HEAL;
	public StringConfig BOOST_DAMAGE;
	public StringConfig BOOST_DAMAGE_NEG;
	public StringConfig BOOST_HEALTH;
	public StringConfig BOOST_HEALTH_NEG;
	public StringConfig BOOST_MANA;
	public StringConfig BOOST_MANA_NEG;
	public StringConfig REMOVE_MANA_HERO;
	public StringConfig REMOVE_MANA_HERO_NEG;
	public StringConfig INCITEMENT;
	public StringConfig EXCITED;
	public StringConfig INVULNERABILITY;
	public StringConfig IMMOBILIZATION;
	public StringConfig STUNNED;
	public StringConfig GIVE_CARD;
	public StringConfig IF_ATTACKED_EXEC_ONE;
	public StringConfig IF_ATTACKED_EXEC;
	public StringConfig IF_ATTACKED_GIVE_ONE;
	public StringConfig IF_ATTACKED_GIVE;
	public StringConfig INVOCATION;
	public StringConfig ACTION_SPAWN;
	public StringConfig ACTION_DEAD;
	public StringConfig EXECUTE;
	public StringConfig APPLY_EXCITED;
	public StringConfig GIVE_FEATURE_CARD;
	public StringConfig METAMORPH_TO;
	public StringConfig REMOVE_CARD;
	public StringConfig HIDE_CARD;

	public FeatureEnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}