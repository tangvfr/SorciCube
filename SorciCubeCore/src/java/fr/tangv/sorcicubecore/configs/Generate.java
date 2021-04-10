package fr.tangv.sorcicubecore.configs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Generate {

	public static void main(String[] args) throws FileNotFoundException, IOException, InvalidConfigurationException {
		String prefix = "Enum";
		String folder = "D:\\Data\\ShareFolder\\Bureau\\";
		String text = "#cible\r\n"
				+ "cible:\r\n"
				+ "  none: \"§f§lpersonne\"\r\n"
				+ "  one_entity_ally_and_one_entity_enemie: \"§fun serviteur allié et un serviteur adverse\"\r\n"
				+ "  one_hero: \"§fun héros\"\r\n"
				+ "  all_hero: \"§ftous les héros\"\r\n"
				+ "  one: \"§fun serviteur ou héros\"\r\n"
				+ "  one_enemie: \"§fun adversaire\"\r\n"
				+ "  one_entity_enemie: \"§fun serviteur adverse\"\r\n"
				+ "  hero_enemie: \"§fl'héros adverse\"\r\n"
				+ "  all_enemie: \"§ftous les adversaires\"\r\n"
				+ "  all_entity_enemie: \"§ftous les serviteurs adverses\"\r\n"
				+ "  one_ally: \"§fun allié\"\r\n"
				+ "  one_entity_ally: \"§fun serviteur allié\"\r\n"
				+ "  hero_ally: \"§fnotre héros\"\r\n"
				+ "  all_ally: \"§ftous les alliés\"\r\n"
				+ "  all_entity_ally: \"§ftous les serviteurs alliés\"\r\n"
				+ "  one_entity: \"§fun serviteur\"\r\n"
				+ "  all_entity: \"§ftous les serviteurs\"\r\n"
				+ "  all: \"§ftous les serviteurs et héros\"\r\n"
				+ "#faction color\r\n"
				+ "faction_color:\r\n"
				+ "  basic: \"7\"\r\n"
				+ "  dark: \"8\"\r\n"
				+ "  light: \"6\"\r\n"
				+ "  nature: \"2\"\r\n"
				+ "  toxic: \"5\"\r\n"
				+ "#faction\r\n"
				+ "faction:\r\n"
				+ "  basic: \"§l§nBasique\"\r\n"
				+ "  dark: \"§l§nObscure\"\r\n"
				+ "  light: \"§l§nLumière\"\r\n"
				+ "  nature: \"§l§nNature\"\r\n"
				+ "  toxic: \"§l§nToxique\"\r\n"
				+ "#rarity\r\n"
				+ "rarity:\r\n"
				+ "  commun: \"§lCommune\"\r\n"
				+ "  rare: \"§lRare\"\r\n"
				+ "  epic: \"§lEpique\"\r\n"
				+ "  legendary: \"§lLégendaire\"\r\n"
				+ "#type\r\n"
				+ "type:\r\n"
				+ "  spell: \"§f[§cSort§f]\"\r\n"
				+ "  entity: \"§f[§9Serviteur§f]\"\r\n"
				+ "  hero: \"§f[§6Héros§f]\"\r\n"
				+ "#sort\r\n"
				+ "sort:\r\n"
				+ "  by_id: \"§7Trié par id\"\r\n"
				+ "  by_faction: \"§aTrié par faction\"\r\n"
				+ "  by_rarity: \"§eTrié par rareté\"\r\n"
				+ "  by_type: \"§dTrié par type\"\r\n"
				+ "  by_low_mana: \"§9Trié par mana croissant\"\r\n"
				+ "  by_high_mana: \"§9Trié par mana décroissant\"\r\n"
				+ "  by_name: \"§cTrié par nom\"\r\n"
				+ "#feature\r\n"
				+ "feature:\r\n"
				+ "  skin: \"hide show\"\r\n"
				+ "  health: \"hide show\"\r\n"
				+ "  damage: \"§fInflige {number} à {cible}.\"\r\n"
				+ "  destruct: \"§fDétruit {cible}.\"\r\n"
				+ "  take_new_card: \"§fVous piochez {number} carte(s).\"\r\n"
				+ "  copy_card_arena_pose: \"§fCopie une carte de l'arène en \\n§f{number} exemplaire sur l'arène.\"\r\n"
				+ "  copy_card_arena: \"§fCopie une carte de l'arène en \\n§f{number} exemplaire dans notre main.\"\r\n"
				+ "  heal: \"§fRajoute {number} point(s) de vie \\n§fà {cible}.\"\r\n"
				+ "  boost_damage: \"§fAugmente l'attaque de {number} \\n§fà {cible}.\"\r\n"
				+ "  boost_damage_neg: \"§fRéduit l'attaque de {number} \\n§fà {cible}.\"\r\n"
				+ "  boost_health: \"§fRajoute {number} point(s) de vie \\n§fà {cible}.\"\r\n"
				+ "  boost_health_neg: \"§fRéduit la vie de {number} \\n§fà {cible}.\"\r\n"
				+ "  boost_mana: \"§fAugmente le mana de {number} \\n§fà {cible} au prochain tour.\"\r\n"
				+ "  boost_mana_neg: \"§fRéduit le mana de {number} \\n§fà {cible} au prochain tour.\"\r\n"
				+ "  remove_mana_hero: \"§fEnlève {number} de mana \\n§fà {cible} immédiatement.\"\r\n"
				+ "  remove_mana_hero_neg: \"§fRajoute {number} de mana \\n§fà {cible} immédiatement.\"\r\n"
				+ "  incitement: \"§7§lIncitation\"\r\n"
				+ "  excited: \"§c§lPeut attaquer directement.\"\r\n"
				+ "  invulnerability: \"§d§lInsensible pendant \\n§f{round} tour(s).\"\r\n"
				+ "  immobilization: \"§b§lImmobile pendant \\n§f{round} tour(s).\"\r\n"
				+ "  stunned: \"§6Étourdi pendant \\n§f{round} tour(s).\"\r\n"
				+ "  give_card: \"§fDonne la carte\\n§f{uuid}\"\r\n"
				+ "  if_attacked_exec_one: \"§f{uuid} \\n§fquand il subit des dégats.\"\r\n"
				+ "  if_attacked_exec: \"§f{uuid} \\n§fà chaque fois qu'il subit des dégats.\"\r\n"
				+ "  if_attacked_give_one: \"§f{uuid} \\n§fquand il subit des dégats.\"\r\n"
				+ "  if_attacked_give: \"§f{uuid} \\n§fà chaque fois qu'il subit des dégats.\"\r\n"
				+ "  invocation: \"§fInvoque {uuid}.\"\r\n"
				+ "  action_spawn: \"§fEntrée en jeu : \\n§f{uuid}.\"\r\n"
				+ "  action_dead: \"§fMort amorcée : \\n§f{uuid}.\"\r\n"
				+ "  execute: \"§f{uuid}.\"\r\n"
				+ "  apply_excited: \"§f{cible} attaque une \\n§fseconde fois.\"\r\n"
				+ "  give_feature_card: \"§f{uuid}.\"\r\n"
				+ "  metamorph_to: \"§fTransforme {cible} en {uuid}.\"\r\n"
				+ "  remove_card: \"§fDéfausse {number} carte(s) à {cible}.\"\r\n"
				+ "  hide_card: \"hide show\"";
		YamlConfiguration config = new YamlConfiguration();
		config.load(new StringReader(text));
		String classDefault = "package fr.tangv.sorcicubecore.configs;\r\n"
				+ "\r\n"
				+ "import org.bson.Document;\r\n"
				+ "import fr.tangv.sorcicubecore.config.*;\r\n"
				+ "\r\n"
				+ "public class {className} extends AbstractConfig {\r\n"
				+ "\r\n"
				+ "{classContent}"
				+ "\r\n"
				+ "	public {className}(Document doc) throws ConfigParseException {\r\n"
				+ "		super(doc);\r\n"
				+ "	}\r\n"
				+ "	\r\n"
				+ "}";
		String lineMains = "";
		for (String key : config.getKeys(false)) {
			if (config.isConfigurationSection(key)) {
				char[] cll = key.toLowerCase().toCharArray();
				String cl = "";
				for (int i = 0; i < cll.length; i++) {
					if (cll[i] == '_') {
						i++;
						cl += Character.toUpperCase(cll[i]);
					} else if (i == 0) {
						cl += Character.toUpperCase(cll[i]);
					} else {
						cl += cll[i];
					}
				}
				cl += prefix+"Config";
				File file = new File(folder+cl+".java");
				if (!file.exists())
					file.createNewFile();
				String lines = "";
				ConfigurationSection section = config.getConfigurationSection(key);
				for (String msg : section.getKeys(false)) {
					lines += "	public StringConfig "+msg.toUpperCase()+";\r\n";
				}
				FileWriter fw = new FileWriter(file);
				fw.append(classDefault.replace("{className}", cl).replace("{classContent}", lines));
				fw.close();
				lineMains += "	public "+cl+" "+key.toUpperCase()+";\r\n";
			} else {
				lineMains += "	public StringConfig "+key.toUpperCase()+";\r\n";
			}
		}
		String cl = prefix+"Config";
		File file = new File(folder+cl+".java");
		if (!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file);
		fw.append(classDefault.replace("{className}", cl).replace("{classContent}", lineMains));
		fw.close();
	}

}
