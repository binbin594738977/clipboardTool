# 说明
android的剪贴板界面操作工具,非常方便,可以通过电脑设备,得到手机的剪贴板内容,设置剪贴板内容
 
![img](https://github.com/binbin594738977/clipboardTool/blob/master/resources/aa.png)

# 工具下载地址
https://github.com/binbin594738977/clipboardTool/releases/tag/v1.2

# 注意
- 目前只支持android9.0以下包括9.0
- 手机打开adb权限

# 问题
- mac双击jar运行使用有问题,需要将adb的路径软连接到/usr/bin/adb,但是/usr/bin/adb是只读的,所以需要系统权限
sudo ln -s 用户目录/Library/Android/sdk/platform-tools/adb /usr/bin/adb
```shell script
# 去掉系统的保护模式
# 重启MAC，按住command+R直到屏幕上出现苹果的标志和进度条，进入Recovery模式；
# 在屏幕最上方的工具栏找到实用工具（左数第3个），打开终端，输入：
csrutil disable
# 或者
csrutil enable
# 关掉终端，重启mac；
# 重启以后可以在终端中查看状态确认。
csrutil status
# 开始不检查模式 (每次重启都会失效)
sudo mount -uw /
```

# 运行jar乱码解决
```shell script
chcp 65001
java -Dfile.encoding=UTF-8 -jar xxx.jar
```

# 弹框
https://blog.csdn.net/zhao50632/article/details/20999173