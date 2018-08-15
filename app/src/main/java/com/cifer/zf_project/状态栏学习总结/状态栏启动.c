a)Application类:
Application和Activity,Service一样是Android框架的一个系统组件，当Android程序启动时系统会创建一个Application对象，用来存储系统的一些信息。
Android系统自动会为每个程序运行时创建一个Application类的对象且只创建一个，所以Application可以说是单例（singleton）模式的一个类。
通常我们是不需要指定一个Application的，系统会自动帮我们创建，如果需要创建自己的Application，那也很简单！创建一个类继承Application并在AndroidManifest.xml文件中的application标签中进行注册（只需要给application标签增加name属性，并添加自己的 Application的名字即可）。
启动Application时，系统会创建一个PID，即进程ID，所有的Activity都会在此进程上运行。那么我们在Application创建的时候初始化全局变量，同一个应用的所有Activity都可以取到这些全局变量的值，换句话说，我们在某一个Activity中改变了这些全局变量的值，那么在同一个应用的其他Activity中值就会改变。
Application对象的生命周期是整个程序中最长的，它的生命周期就等于这个程序的生命周期。因为它是全局的单例的，所以在不同的Activity,Service中获得的对象都是同一个对象。所以可以通过Application来进行一些，如：数据传递、数据共享和数据缓存等操作。
应用场景：
在Android中，可以通过继承Application类来实现应用程序级的全局变量，这种全局变量方法相对静态类更有保障，直到应用的所有Activity全部被destory掉之后才会被释放掉。

b)style标签中的parent属性:通过继承机制，可以利用已有的style来定义新的style。所定义的新的style型不仅拥有新定义的item，而且还同时拥有旧的item.(在Android的frameworks/base/core/res/res/values目录下有一下几个文件,分别定义了各种系统Theme，Style)
c)#ff263238----->5.0状态栏默认颜色
d)Intent对象的setComponent(ComponentName comp)方法用于设置Intent的Component属性；除了使用setComponent() 之外, 还可以使用setClass(), setClassName()来显式指定目标组件, 还可以调用getComponent()方法获得Intent中封装的ComponentName对象.
IntentFilter类表示Intent过滤器, 大部分情况下, 每一个component都会定义一个或多个IntentFilter, 用于表明其可处理的Intent.
一般来说, component的IntentFilter应该在AndroidManifest.xml文件中定义. 

e)开机==>http://blog.jobbole.com/67931/
//https://blog.csdn.net/yangzhihuiguming/article/details/51697801
//https://www.cnblogs.com/samchen2009/p/3294713.html
第五步——>加载Zygote虚拟器进程——>startSystemServer函数来启动SystemServer组件——>调用startSystemUi方法——>启动SystemUIService——>
getApplication()).startServicesIfNeeded()启动(/frameworks/base/packages/SystemUI/src/com/android/systemui/SystemUIApplication)
			com.android.systemui.keyguard.KeyguardViewMediator.class,
            com.android.systemui.recent.Recents.class,
            com.android.systemui.volume.VolumeUI.class,
            com.android.systemui.statusbar.SystemBars.class,
            com.android.systemui.usb.StorageNotification.class,
            com.android.systemui.power.PowerUI.class,
            com.android.systemui.media.RingtonePlayer.class

d）SystemBars.class extends SystemUI implements ServiceMonitor.Callbacks(frameworks/base/packages/SystemUI/src/com/android/systemui/statusbar)
e)Uri代表了要操作的数据，Uri主要包含了两部分信息：1》需要操作的ContentProvider ，2》对ContentProvider中的什么数据进行操作，一个Uri由以下几部分组成：          
ContentProvider（内容提供者）的scheme已经由Android所规定， scheme为：content://
主机名（或叫Authority）用于唯一标识这个ContentProvider，外部调用者可以根据这个标识来找到它。
路径（path）可以用来表示我们要操作的数据，路径的构建应根据业务而定，如下:
要操作person表中id为10的记录，可以构建这样的路径:/person/10
要操作person表中id为10的记录的name字段， person/10/name
要操作person表中的所有记录，可以构建这样的路径:/person
要操作xxx表中的记录，可以构建这样的路径:/xxx
当然要操作的数据不一定来自数据库，也可以是文件、xml或网络等其他存储方式，如下:
要操作xml文件中person节点下的name节点，可以构建这样的路径：/person/name
如果要把一个字符串转换成Uri，可以使用Uri类中的parse()方法，如下：
Uri uri = Uri.parse("content://com.ljq.provider.personprovider/person")

f)android:descendantFocusability:
该属性是当一个为view获取焦点时，定义viewGroup和其子控件两者之间的关系。
属性的值有三种：
        beforeDescendants：viewgroup会优先其子类控件而获取到焦点
        afterDescendants：viewgroup只有当其子类控件不需要获取焦点时才获取焦点
        blocksDescendants：viewgroup会覆盖子类控件而直接获得焦点


g)a|=b的意思就是把a和b按位或然后赋值给a   按位或的意思就是先把a和b都换成2进制，然后用或操作，相当于a=a|b


h)ServiceMonitor.Callbacks(接d)

public interface Callbacks {
        /** The service does not exist or failed to bind */
        void onNoService();
        /** The service is about to start, this is a chance to perform cleanup and
         * delay the start if necessary */
        long onServiceStartAttempt();
    }
——>SystemBars实现回调方法

public void onNoService() {
        if (DEBUG) Log.d(TAG, "onNoService");
        createStatusBarFromConfig();  // fallback to using an in-process implementation
    }
——>createStatusBarFromConfig()中新建PhoneStatusBar
mStatusBar = (BaseStatusBar) cls.newInstance();//PhoneStatusBar(./res/values/config.xml)

——>mStatusBar.start(); 调用PhoneStatusBar的start方法

——>super.start(); // calls createAndAddWindows()

——>@Override  PhoneStatusBar中覆写BaseStatusBar中的createAndAddWindows()方法
	public void createAndAddWindows() {
			addStatusBarWindow();
	}

——>addStatusBarWindow()中调用：
	makeStatusBarView();

——>makeStatusBarView()中加载布局文件
if (FeatureOption.MTK_GEMINI_SUPPORT) {//true
		mStatusBarWindow = (StatusBarWindowView)View.inflate(context, R.layout.gemini_super_status_bar, null);
} else {
		mStatusBarWindow = (StatusBarWindowView) View.inflate(context, R.layout.super_status_bar, null);
}




