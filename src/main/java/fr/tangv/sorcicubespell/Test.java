package fr.tangv.sorcicubespell;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.World.Spigot;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class Test {

	public static void main(String[] args) {
		String text = "caca proute !";
		System.out.println(text);
		String mix = "{textures:{SKIN:{url:\""+text+"\"}}}";
		System.out.println(mix);
		System.out.println(mix.substring(22, mix.length()-4));
		YamlConfiguration config = new YamlConfiguration();
		config.set("location_spawn", new Location(new World() {
			
			@Override
			public void setMetadata(String arg0, MetadataValue arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void removeMetadata(String arg0, Plugin arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean hasMetadata(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public List<MetadataValue> getMetadata(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void sendPluginMessage(Plugin arg0, String arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Set<String> getListeningPluginChannels() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean unloadChunkRequest(int arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean unloadChunkRequest(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean unloadChunk(int arg0, int arg1, boolean arg2, boolean arg3) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean unloadChunk(int arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean unloadChunk(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean unloadChunk(Chunk arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public LightningStrike strikeLightningEffect(Location arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public LightningStrike strikeLightning(Location arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Spigot spigot() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <T> void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5,
					double arg6, double arg7, double arg8, T arg9) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6,
					double arg7, double arg8) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <T> void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5,
					double arg6, double arg7, T arg8) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <T> void spawnParticle(Particle arg0, Location arg1, int arg2, double arg3, double arg4, double arg5,
					double arg6, T arg7) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6,
					double arg7) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void spawnParticle(Particle arg0, Location arg1, int arg2, double arg3, double arg4, double arg5,
					double arg6) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <T> void spawnParticle(Particle arg0, Location arg1, int arg2, double arg3, double arg4, double arg5,
					T arg6) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void spawnParticle(Particle arg0, Location arg1, int arg2, double arg3, double arg4, double arg5) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <T> void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, T arg5) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <T> void spawnParticle(Particle arg0, Location arg1, int arg2, T arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void spawnParticle(Particle arg0, Location arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public FallingBlock spawnFallingBlock(Location arg0, int arg1, byte arg2) throws IllegalArgumentException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public FallingBlock spawnFallingBlock(Location arg0, Material arg1, byte arg2) throws IllegalArgumentException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Entity spawnEntity(Location arg0, EntityType arg1) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <T extends Arrow> T spawnArrow(Location arg0, Vector arg1, float arg2, float arg3, Class<T> arg4) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Arrow spawnArrow(Location arg0, Vector arg1, float arg2, float arg3) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <T extends Entity> T spawn(Location arg0, Class<T> arg1) throws IllegalArgumentException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void setWeatherDuration(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setWaterAnimalSpawnLimit(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setTime(long arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setTicksPerMonsterSpawns(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setTicksPerAnimalSpawns(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setThundering(boolean arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setThunderDuration(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setStorm(boolean arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean setSpawnLocation(int arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void setSpawnFlags(boolean arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setPVP(boolean arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setMonsterSpawnLimit(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setKeepSpawnInMemory(boolean arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean setGameRuleValue(String arg0, String arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void setFullTime(long arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setDifficulty(Difficulty arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setBiome(int arg0, int arg1, Biome arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setAutoSave(boolean arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setAnimalSpawnLimit(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setAmbientSpawnLimit(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void save() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean regenerateChunk(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean refreshChunk(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void playSound(Location arg0, String arg1, float arg2, float arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void playSound(Location arg0, Sound arg1, float arg2, float arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <T> void playEffect(Location arg0, Effect arg1, T arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void playEffect(Location arg0, Effect arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <T> void playEffect(Location arg0, Effect arg1, T arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void playEffect(Location arg0, Effect arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean loadChunk(int arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void loadChunk(int arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void loadChunk(Chunk arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isThundering() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isGameRule(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isChunkLoaded(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isChunkLoaded(Chunk arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isChunkInUse(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isAutoSave() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasStorm() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public WorldType getWorldType() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public File getWorldFolder() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public WorldBorder getWorldBorder() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getWeatherDuration() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getWaterAnimalSpawnLimit() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public UUID getUID() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public long getTime() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public long getTicksPerMonsterSpawns() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public long getTicksPerAnimalSpawns() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getThunderDuration() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public double getTemperature(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Location getSpawnLocation() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public long getSeed() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getSeaLevel() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public List<BlockPopulator> getPopulators() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<Player> getPlayers() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean getPVP() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Collection<Entity> getNearbyEntities(Location arg0, double arg1, double arg2, double arg3) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return "test world";
			}
			
			@Override
			public int getMonsterSpawnLimit() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getMaxHeight() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Chunk[] getLoadedChunks() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<LivingEntity> getLivingEntities() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean getKeepSpawnInMemory() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public double getHumidity(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getHighestBlockYAt(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getHighestBlockYAt(Location arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Block getHighestBlockAt(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Block getHighestBlockAt(Location arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ChunkGenerator getGenerator() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String[] getGameRules() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getGameRuleValue(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public long getFullTime() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Environment getEnvironment() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Collection<Entity> getEntitiesByClasses(Class<?>... arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<Entity> getEntities() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ChunkSnapshot getEmptyChunkSnapshot(int arg0, int arg1, boolean arg2, boolean arg3) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Difficulty getDifficulty() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Chunk getChunkAt(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Chunk getChunkAt(Block arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Chunk getChunkAt(Location arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getBlockTypeIdAt(int arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getBlockTypeIdAt(Location arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Block getBlockAt(int arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Block getBlockAt(Location arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Biome getBiome(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getAnimalSpawnLimit() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getAmbientSpawnLimit() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public boolean getAllowMonsters() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean getAllowAnimals() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean generateTree(Location arg0, TreeType arg1, BlockChangeDelegate arg2) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean generateTree(Location arg0, TreeType arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Item dropItemNaturally(Location arg0, ItemStack arg1) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Item dropItem(Location arg0, ItemStack arg1) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean createExplosion(double arg0, double arg1, double arg2, float arg3, boolean arg4, boolean arg5) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean createExplosion(double arg0, double arg1, double arg2, float arg3, boolean arg4) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean createExplosion(double arg0, double arg1, double arg2, float arg3) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean createExplosion(Location arg0, float arg1, boolean arg2) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean createExplosion(Location arg0, float arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean canGenerateStructures() {
				// TODO Auto-generated method stub
				return false;
			}
		}, 0.0, 0.0, 0.0));
		System.out.println(config.saveToString());
	}
	
}
