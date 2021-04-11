package fr.tangv.sorcicubecore.configs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

import org.bson.Document;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.BooleanConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.ElementConfig;
import fr.tangv.sorcicubecore.config.IntegerConfig;
import fr.tangv.sorcicubecore.config.StringConfig;

public class Generate {

	public static void main(String[] args) throws FileNotFoundException, IOException, InvalidConfigurationException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, ConfigParseException {
		/*FeatureGenerate feature; = new FeatureGenerate(
						"Enum", 
						"D:\\Data\\ShareFolder\\Bureau\\"
				);
		generateClass(feature);
		generateJSON(feature);*/
	}
	
	public static class FeatureGenerate {
		private final String prefix;
		private final String folder;
		private final String data;
		
		public FeatureGenerate(String prefix, String folder, String data) {
			this.prefix = prefix;
			this.folder = folder;
			this.data = data;
		}
	}
	
	public static void generateClass(FeatureGenerate feature) throws IOException, InvalidConfigurationException {
		YamlConfiguration config = new YamlConfiguration();
		config.load(new StringReader(feature.data));
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
				cl += feature.prefix+"Config";
				File file = new File(feature.folder+cl+".java");
				if (!file.exists())
					file.createNewFile();
				String lines = "";
				ConfigurationSection section = config.getConfigurationSection(key);
				for (String msg : section.getKeys(false)) {
					String typ = "StringConfig";
					if (section.isInt(msg))
						typ = "IntegerConfig";
					else if (section.isBoolean(msg))
						typ = "BooleanConfig";
					lines += "	public "+typ+" "+msg.toUpperCase()+";\r\n";
				}
				FileWriter fw = new FileWriter(file);
				fw.append(classDefault.replace("{className}", cl).replace("{classContent}", lines));
				fw.close();
				lineMains += "	public "+cl+" "+key.toUpperCase()+";\r\n";
			} else {
				String typ = "StringConfig";
				if (config.isInt(key))
					typ = "IntegerConfig";
				else if (config.isBoolean(key))
					typ = "BooleanConfig";
				lineMains += "	public "+typ+" "+key.toUpperCase()+";\r\n";
			}
		}
		String cl = feature.prefix+"Config";
		File file = new File(feature.folder+cl+".java");
		if (!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file);
		fw.append(classDefault.replace("{className}", cl).replace("{classContent}", lineMains));
		fw.close();
	}
	
	public static void generateJSON(ConfigurationSection section) {
		
		
		
	}	
	
	public static void generateJSON(FeatureGenerate feature) throws IOException, InvalidConfigurationException, ClassNotFoundException, ConfigParseException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		YamlConfiguration config = new YamlConfiguration();
		config.load(new StringReader(feature.data));
		Class<?> classs = Class.forName("fr.tangv.sorcicubecore.configs."+feature.prefix+"Config");
		AbstractConfig main = (AbstractConfig) classs.getConstructor(Document.class).newInstance(new Document());
		for (String key : config.getKeys(false)) {
			Field field = main.getClass().getField(key.toUpperCase());
			Class<?> type = field.getType();
			if (!ElementConfig.class.isAssignableFrom(type))
				throw new ConfigParseException("ErrorField "+type.getName()+" don't has interface "+ElementConfig.class.getSimpleName()+" !");
			try {
				try {
					if (type == StringConfig.class) {
						StringConfig inst = new StringConfig(null);
						inst.value = config.getString(key, "");
						field.set(main, inst);
					} else if (type == IntegerConfig.class) {
						IntegerConfig inst = new IntegerConfig(null);
						inst.value = config.getInt(key, 0);
						field.set(main, inst);
					} else if (type == BooleanConfig.class) {
						BooleanConfig inst = new BooleanConfig(null);
						inst.value = config.getBoolean(key, false);
						field.set(main, inst);
					} else {
						Object inst = type.getConstructor(Document.class).newInstance(new Document());
						ConfigurationSection section = config.getConfigurationSection(key);
						for (String msg : section.getKeys(false)) {
							Field f = inst.getClass().getField(msg.toUpperCase());
							StringConfig s = new StringConfig(null);
							s.value = section.getString(msg);
							f.set(inst, s);
						}
						field.set(main, inst);
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
		String cl = feature.prefix+"Config";
		File file = new File(feature.folder+cl+".json");
		if (!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file);
		fw.append(main.toDocument().toJson());
		fw.close();
	}

}
