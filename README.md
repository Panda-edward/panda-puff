# panda-hunting

把同步调用远程服务的方法->先记录本地消息,再异步请求+重试的模式

使用说明:
1.@enablePuff
2.对方法@hunting,同时需要标注业务主键参数@PuffBizId

ps:被拦截方法需要在事务中被调用,否则抛出异常
