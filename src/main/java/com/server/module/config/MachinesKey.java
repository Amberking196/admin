package com.server.module.config;

public class MachinesKey extends BasePrefix {

	public MachinesKey(String prefix) {
		super(prefix);
	}
	
	public MachinesKey(String prefix,int expireSeconds) {
		super(prefix,expireSeconds);
	}


	public static MachinesKey currentMachinesUser=new MachinesKey("user",60*60*12);
	
	public static MachinesKey improperMachinesUser=new MachinesKey("improperUser");
	
	public static MachinesKey getVmCodeByFactoryNum=new MachinesKey("numToCode");

	public static MachinesKey currentMachinesOpenState=new MachinesKey("open",100);
	
	public static MachinesKey orderFlag = new MachinesKey("order",60*60*5);
	
	public static MachinesKey game=new MachinesKey("game");
	
	public static MachinesKey gameUser=new MachinesKey("gameUser");
	

}
