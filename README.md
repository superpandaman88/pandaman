# 简介
android app 服务器地址设置工具apk（android）主要功能：在开发环境中修改，新增，切换 连接的服务器的地址。 
该工具简单易用，从而告别之前客户端切换服务端地址的繁琐操作，新工具切换服务器地址只需点击操作即可完成，新增，修改服务器地址
只需简单的在工具中设置即可立即生效。在输入地址的时候可以使用扫码二维码可以方便快捷输入。
该工具所用的原理：使用Android 的contentprovider 数据共享机制的统一访问接口。 数据存储采用Android的sqlite，并使用orm框架访问sqlite，使用Android的PreferenceActivity 显示界面。
