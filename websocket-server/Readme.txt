1. 配置 <context:component-scan .../> 务必注意， 在 ***-servlet.xml 中只配置扫描 web controller 对应的 package
   同时在 applicationContext.xml 中配置扫描时需要排除掉 web controller, 
   反之若只在 ***-servlet.xml 中配置针对所有 package 的扫描， spring 会初始化两个 application context 实例，
   导致在自定义的 ContextLoaderListener 中初始化 BeanLocator 后无法从中取到对应的 bean 实例 ！！！ 
   (因为 BeanLocator 实际上设置成了另一个 application context 实例)