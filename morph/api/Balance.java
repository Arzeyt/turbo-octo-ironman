package morph.api;

import java.util.HashMap;
import java.util.HashSet;

import javax.jws.Oneway;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Balance{

	int tickRate = 300;
	int minimumFoodLevel =2;//has to be greater than 1
	HashMap<EntityPlayer, Integer> players = new HashMap<EntityPlayer, Integer>();
	
	public void handleNewPlayerTick(EntityPlayer player){
		
		if(players.containsKey(player)==false){
			players.put(player, 0);
		}
		if(hasMorph(player)==true){
			addTickToPlayer(player);
		}else if(hasMorph(player)==false){
			return;
		}
	}
	

	private void addTickToPlayer(EntityPlayer player){
		if(players.get(player).intValue() > tickRate){
			handleLimitBreak(player);
			players.put(player, 0);
		}else{
			int tick = players.get(player).intValue();
			tick++;
			players.put(player, tick);
		}
	}
	
	private void handleLimitBreak(EntityPlayer player){
		int foodLevel = player.getFoodStats().getFoodLevel();
		if(foodLevel < minimumFoodLevel){
			sendMessageToPlayer(player, "You're too exhausted to keep your morph!");
			Api.forceDemorph((EntityPlayerMP) player);
		}else{
			player.getFoodStats().addExhaustion(0.100F);
		}
	}
	
	private void sendMessageToPlayer(EntityPlayer player, String msg){
		player.sendChatToPlayer(new ChatMessageComponent().createFromText(msg));
	}
	
	private boolean hasMorph(EntityPlayer player){
		if(Api.hasMorph(player.username, true)==true || Api.hasMorph(player.getDisplayName(), true)==true){
			return true;
		}else if(Api.hasMorph(player.username,  false)==true || Api.hasMorph(player.getDisplayName(), false)==true){
			return true;
		}else{
			return false;
		}
	}
}
