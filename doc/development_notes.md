#开发笔记
1. 运行java -jar zhenr.jar 即可以正常运行Zhenr服务器.
2. 整体与局部有相同的一致性。
3. Zhenr的主目录
	1. 如果指定了zhenr.home的系统属性，则用此属性做Zhenr的主目录。否则(2)
	2. Zhenr的主目录=Zhenr的jar所在的目录.
4. 获取zhenr的配置信息.
	1. 缺省配置=Zhenr的jar中的META-INF/conf/zhenr.default.xml
	2. 用户的配置信息=Zhenr主目录/conf/zhenr.xml。如果存在则覆盖缺省配置.
5. awaitTermination
	* 使用synchronized(lock){lock.wait();}	
6. 本地语言支持:NLS.
	1. 需求：
		1. key值也是静态的编译时确定的。
	2. 实现：
		1. TranslationBundle:(Locale,(ResourceBundle:PropertyFile))
			1. 关键方法：#load(): TranslationBundle的每个String类型的Field=对应的PropertyFile的key，根据key从PropertyFile中加载value。
		2. NLS.get(subclassOfTranslationBundle)
			1. GlobalBundleCache缓存(Locale:(Class:TranslationBundle))
7. logging实现
	1. specified class > slf4j > stdErr