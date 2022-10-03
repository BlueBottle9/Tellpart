# bukkit-Tellpart
A bukkit teleport plugin. - 一个传送插件

commands:

```
/home

Use /home set to set here as "home". - 使用/home set 来设置"家"

Use /home to teleport "home". - 使用/home 来传送回家

/tpr

Use /tpr to random teleport in the world. - 使用/tpr 在世界内随机传送

/tpt

Use /tpt to [Player ID] to requset to teleport to others. - 使用/tpt to [玩家ID] 来请求传送到他人处

Use /tpt accept/refuse to accept/refuse someone's TPT teleporting request. - 使用 /tpt accept/refuse 来接受/拒绝 他人的TPT请求

/tph

Use /tph to [Player ID] to requset someone to teleport to you. - 使用/tph to [玩家ID] 来请求别人传送到你处

Use /tph accept/refuse to accept/refuse someone's TPH teleporting request. - 使用 /tph accept/refuse 来接受/拒绝 他人的TPH请求

/tpp

Use /tpp set [name] to set a teleport point. - 使用/tpp set [名字] 来设定传送点

Use /tpp to [name] to teleport to a point. - 使用/tpp to [名字] 传送到指定传送点

Use /tpp remove [name] to remove a teleport point. - 使用/tpp remove [名字] 来移除传送点

/back

Use /back to teleport the lastest teleporting/death location. - 传送至上一个传送/死亡点
```

config.yml:

```
move-to-cancel: true #Will it cancel player's teleporting when they move. - 是否在玩家移动时取消传送

home:

  enable: true
  
  delay: 3 #How much time do a player need to teleport.(seconds) - 一个玩家需要多少时间传送回家(单位：秒)
  
  could-down: 30
  
tpr:

  enable: true
  
  delay: 5 #How much time do a player need to teleport.(seconds) - 一个玩家需要多少时间随机传送(单位：秒)
  
  could-down: 30
  
tpt:

  enable: true
  
  delay: 5 #How much time do a player need to teleport.(seconds) - 一个玩家需要多少时间"tpr"(单位：秒)
  
  could-down: 30
  
  valid-time: 300
  
tph:

  enable: true
  
  delay: 5 #How much time do a player need to teleport.(seconds) - 一个玩家需要多少时间"tph"(单位：秒)
  
  could-down: 30
  
  valid-time: 5
  
tpp:

  enable: true
  
  delay: 5 #How much time do a player need to teleport.(seconds) - 一个玩家需要多少时间传送到传送点(单位：秒)
  
  could-down: 30
  
bar: #Teleport bar style - 传送进度条样式

  color: 'GREEN' #颜色
  
  style: 'SOLID' #样式
  
```
