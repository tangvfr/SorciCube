package fr.tangv.sorcicubecore.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.bson.Document;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import fr.tangv.sorcicubecore.configs.*;

@SuppressWarnings("unused")
public class Generate {

	public static void main(String[] args) throws FileNotFoundException, IOException, InvalidConfigurationException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, ConfigParseException {
		boolean gClass = true;
		String name = "Npc";
		String file = "D:\\Data\\ShareFolder\\Bureau\\config\\others\\npc.yml";
		FeatureGenerate feature = new FeatureGenerate(name, new File(file));
		if (gClass)
			generateClass(feature);
		else
			generateJSON(feature);
	}
	
	public static class FeatureGenerate {
		private static final String DEFAULT_CLASS = "package fr.tangv.sorcicubecore.configs;\r\n"
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
		private final String prefix;
		private final String folder;
		private final File file;
		
		public FeatureGenerate(String prefix, File file) {
			this.prefix = prefix;
			this.folder = file.getParent()+"\\";
			this.file = file;
		}
	}
	
	private static String generateClass(ConfigurationSection section, String key, String prefix, String folder) throws IOException {
		String typ = null;
		if (section.isInt(key))
			typ = "IntegerConfig";
		else if (section.isBoolean(key))
			typ = "BooleanConfig";
		else if (section.isString(key))
			typ = "StringConfig";
		else if (section.isVector(key))
			typ = "VectorConfig";
		else if (section.isSet(key) && section.getBoolean(key+".location", false)) {
			typ = "LocationConfig";
		} else if (section.isConfigurationSection(key)) {
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
			cl += prefix;
			File file = new File(folder+cl+".java");
			if (!file.exists())
				file.createNewFile();
			String lines = "";
			ConfigurationSection sec = section.getConfigurationSection(key);
			for (String k : sec.getKeys(false))
				lines += generateClass(sec, k, cl, folder);
			FileWriter fw = new FileWriter(file);
			fw.append(FeatureGenerate.DEFAULT_CLASS.replace("{className}", cl).replace("{classContent}", lines));
			fw.close();
			return "	public "+cl+" "+key.toUpperCase()+";\r\n";
		}
		if (typ != null)
			return "	public "+typ+" "+key.toUpperCase()+";\r\n";
		else {
			System.err.println("Key "+key+" has invalid type !");
			return "";
		}
	}
			
	public static void generateClass(FeatureGenerate feature) throws IOException, InvalidConfigurationException {
		YamlConfiguration config = new YamlConfiguration();
		config.load(feature.file);
		String lineMains = "";
		String cl = feature.prefix+"Config";
		for (String key : config.getKeys(false))
			lineMains += generateClass(config, key, cl, feature.folder);
		File file = new File(feature.folder+cl+".java");
		if (!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file);
		fw.append(FeatureGenerate.DEFAULT_CLASS.replace("{className}", cl).replace("{classContent}", lineMains));
		fw.close();
	}
	
	private static void generateJSON(ConfigurationSection section, String key, Object parent) throws ConfigParseException, NoSuchFieldException, SecurityException {
		Field field = parent.getClass().getField(key.toUpperCase());
		Class<?> type = field.getType();
		if (!ElementConfig.class.isAssignableFrom(type))
			throw new ConfigParseException("ErrorField "+type.getName()+" don't has interface "+ElementConfig.class.getSimpleName()+" !");
		try {
			try {
				if (type == StringConfig.class) {
					StringConfig inst = new StringConfig(null);
					inst.value = section.getString(key, "");
					field.set(parent, inst);
				} else if (type == IntegerConfig.class) {
					IntegerConfig inst = new IntegerConfig(null);
					inst.value = section.getInt(key, 0);
					field.set(parent, inst);
				} else if (type == BooleanConfig.class) {
					BooleanConfig inst = new BooleanConfig(null);
					inst.value = section.getBoolean(key, false);
					field.set(parent, inst);
				} else if (type == VectorConfig.class) {
					VectorConfig inst = new VectorConfig(null);
					inst.x = section.getDouble(key+".x", 0.0);
					inst.y = section.getDouble(key+".y", 0.0);
					inst.z = section.getDouble(key+".z", 0.0);
					field.set(parent, inst);
				} else if (type == LocationConfig.class) {
					LocationConfig inst = new LocationConfig(null);
					inst.world = section.getString(key+".world", "world");
					inst.x = section.getDouble(key+".x", 0.0);
					inst.y = section.getDouble(key+".y", 0.0);
					inst.z = section.getDouble(key+".z", 0.0);
					inst.pitch = (float) section.getDouble(key+".pitch", 0.0);
					inst.yaw = (float) section.getDouble(key+".yaw", 0.0);
					field.set(parent, inst);
				} else {
					Object inst = type.getConstructor(Document.class).newInstance(new Document());
					ConfigurationSection sec = section.getConfigurationSection(key);
					for (String k : sec.getKeys(false))
						generateJSON(sec, k, inst);
					field.set(parent, inst);
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new ConfigParseException(e.getClass().getSimpleName()+": "+e.getMessage(), e);
			}
		} catch (NoSuchMethodException e) {
			throw new ConfigParseException(type.getName()+" don't has constructor "+type.getSimpleName()+"(Document doc) !", e);
		} catch (SecurityException e) {
			throw new ConfigParseException(type.getName()+" don't has permission for constructor "+type.getSimpleName()+"(Document doc) !", e);
		}
	}	
	
	public static void generateJSON(FeatureGenerate feature) throws IOException, InvalidConfigurationException, ClassNotFoundException, ConfigParseException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		YamlConfiguration config = new YamlConfiguration();
		config.load(feature.file);
		Class<?> classs = Class.forName("fr.tangv.sorcicubecore.configs."+feature.prefix+"Config");
		AbstractConfig main = (AbstractConfig) classs.getConstructor(Document.class).newInstance(new Document());
		for (String key : config.getKeys(false))
			generateJSON(config, key, main);
		String cl = feature.prefix+"Config";
		File file = new File(feature.folder+cl+".json");
		if (!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file);
		fw.append(main.toDocument().toJson());
		fw.close();
	}

}
