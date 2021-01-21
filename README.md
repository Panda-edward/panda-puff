使用说明
1.引入依赖 com.ed:nuts:1.0-SNAPSHOT
2.在db执行resource目录下的DDL
3.添加注解@Nuts，拦截需保证最终成功的方法
  ps:被拦截的方法，要在事务中被调用才生效
4.标识业务主键
  4.1 参数注解@NutsBizNo指定
  4.2 参数实现NutsVO接口,重写getBizNo方法

5.扩展点
  5.1 通过实现INutsRepository（托管spring）,实现本地记录的扩展;
  5.2 指定重试策略