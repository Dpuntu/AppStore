debug=platform
debugseuic=seuic
itms.jks=self

debug      sha1
debugseuic sha1     C:\User\user\.android   keytool -list -keystore E:\seuic\ITMS\keystore\debug.keystore
itms       sha1     C:\User\user\.android   keytool -list -v -keystore E:\seuic\ITMS\keystore\itms.jks

系统签名SHA1  b8 75 f6 9f 34 38 de fc 62 ba d8 d4 96 52 51 f2 1f af ed af
b8:75:f6:9f:34:38:de:fc:62:ba:d8:d4:96:52:51:f2:1f:af:ed:af


C:\Users\user>keytool -v -list -keystore E:\seuic\work\TMS\ITMS\keystore\debug.keystore
输入密钥库口令:

密钥库类型: JKS
密钥库提供方: SUN

您的密钥库包含 1 个条目

别名: androiddebugkey
创建日期: 2015-10-15
条目类型: PrivateKeyEntry
证书链长度: 1
证书[1]:
所有者: EMAILADDRESS=ningyanpan@seuic.com, CN=DJ-W790 platform, OU=Finance R&D, O=SEUIC, L=Nanjing, ST=Jiangsu, C=CN
发布者: EMAILADDRESS=ningyanpan@163.com, CN=Smart Pos Root, OU=Finance R&D, O=SEUIC, L=Nanjing, ST=Jiangsu, C=CN
序列号: 974ed21d87769806
有效期开始日期: Mon Jun 29 12:21:13 CST 2015, 截止日期: Thu Jun 26 12:21:13 CST 2025
证书指纹:
         MD5: 3E:E6:BB:B4:8A:06:8E:26:67:9F:67:0C:30:EA:DA:BA
         SHA1: B8:75:F6:9F:34:38:DE:FC:62:BA:D8:D4:96:52:51:F2:1F:AF:ED:AF
         SHA256: FD:80:17:6C:36:51:64:AB:13:D0:37:E9:9F:50:8E:45:3C:02:CC:F6:4F:4A:61:06:10:E4:1E:36:FE:CE:FD:21
         签名算法名称: SHA1withRSA
         版本: 1