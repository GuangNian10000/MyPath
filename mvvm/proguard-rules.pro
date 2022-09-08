
-optimizationpasses 5

-dontusemixedcaseclassnames

-dontskipnonpubliclibraryclasses

-dontskipnonpubliclibraryclassmembers

-dontpreverify

-verbose
-printmapping priguardMapping.txt

-optimizations !code/simplification/artithmetic,!field/*,!class/merging/*


-keep class com.sdt.dlxk.data.**{*;}


################阿里登录###############
-keep class org.json.** {
   *;
}

#"R.drawable.authsdk*",
#"R.layout.authsdk*",
#"R.anim.authsdk*",
#"R.id.authsdk*",
#"R.string.authsdk*",
#"R.style.authsdk*",



################华为推送###############
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.huawei.hianalytics.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

################xiaomi推送###############
#这里com.xiaomi.mipushdemo.DemoMessageRreceiver改成app中定义的完整类名
-keep class com.sdt.dlxk.app.push.xiaomi.MiBroadcastReceiver {*;}


################oppo推送###############
-keep public class * extends android.app.Service
-keep class com.heytap.msp.** { *;}

################common###############

 #实体类不参与混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepnames class * implements java.io.Serializable
-keepattributes Signature
-keep class **.R$* {*;}
-ignorewarnings
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclasseswithmembernames class * { # 保持native方法不被混淆
    native <methods>;
}

-keepclassmembers enum * {  # 使用enum类型时需要注意避免以下两个方法混淆，因为enum类的特殊性，以下两个方法会被反射调用，
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# com.github.ittianyu:BottomNavigationViewEx:2.0.4
-keep public class com.google.android.material.bottomnavigation.BottomNavigationView { *; }
-keep public class com.google.android.material.bottomnavigation.BottomNavigationMenuView { *; }
-keep public class com.google.android.material.bottomnavigation.BottomNavigationPresenter { *; }
-keep public class com.google.android.material.bottomnavigation.BottomNavigationItemView { *; }

################support###############
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-dontwarn android.support.**

-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**
################glide###############
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

################retrofit###############
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
################RxPermissions#################
-keep class com.tbruyelle.rxpermissions2.** { *; }
-keep interface com.tbruyelle.rxpermissions2.** { *; }

-keep class com.sdt.dlxk.data.model.bean.**{ *; }

# 保留自定义控件(继承自View)不能被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(***);
    *** get* ();
}
-dontwarn com.kingja.loadsir.**
-keep class com.kingja.loadsir.** {*;}

-keep class com.just.agentweb.** {
    *;
}

-dontwarn com.just.agentweb.**

-keepattributes *Annotation*
-keep class **.*_SnakeProxy
#-keep com.youngfeng.snake.annotations.EnableDragToClose public class *

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# SearchView
-keep class androidx.appcompat.widget.SearchView {
    ImageView mGoButton;
}

-keep class com.tbruyelle.rxpermissions2.** { *; }
-keep interface com.tbruyelle.rxpermissions2.** { *; }

################ ViewBinding & DataBinding ###############
-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
  public static * inflate(android.view.LayoutInflater);
  public static * inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
  public static * bind(android.view.View);
}










#glide加载框架
    -keep public class * implements com.bumptech.glide.module.GlideModule
    -keep class * extends com.bumptech.glide.module.AppGlideModule {
     <init>(...);
    }
    -keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
    }
    -keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
      *** rewind();
    }

#WebView
    -keep class com.just.agentweb.** {
        *;
    }
    -dontwarn com.just.agentweb.**
    #Java 注入类不要混淆 ， 例如 sample 里面的 AndroidInterface 类 ， 需要 Keep 。
    #-keepclassmembers class com.just.agentweb.sample.common.AndroidInterface{ *; }

#文件下载
    -dontwarn com.arialyy.aria.**
    -keep class com.arialyy.aria.**{*;}
    -keep class **$$DownloadListenerProxy{ *; }
    -keep class **$$UploadListenerProxy{ *; }
    -keep class **$$DownloadGroupListenerProxy{ *; }
    -keep class **$$DGSubListenerProxy{ *; }
    -keepclasseswithmembernames class * {
        @Download.* <methods>;
        @Upload.* <methods>;
        @DownloadGroup.* <methods>;
    }

#轮播图
-keep class androidx.recyclerview.widget.**{*;}
-keep class androidx.viewpager2.widget.**{*;}

#easyphotos
-keep class com.huantansheng.easyphotos.models.** { *; }


##########################################分割线######################################################################################################
#指定代码的压缩级别
-optimizationpasses 5

#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

#混淆时是否记录日志
-verbose

#优化  不优化输入的类文件
-dontoptimize

#预校验
-dontpreverify

# 保留sdk系统自带的一些内容 【例如：-keepattributes *Annotation* 会保留Activity的被@override注释的onCreate、onDestroy方法等】
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# 记录生成的日志数据,gradle build时在本项根目录输出
# apk 包内所有 class 的内部结构
#-dump proguard/class_files.txt
# 未混淆的类和成员
-printseeds proguard/seeds.txt
# 列出从 apk 中删除的代码
-printusage proguard/unused.txt
# 混淆前后的映射
-printmapping proguard/mapping.txt


# 避免混淆泛型
-keepattributes Signature
# 抛出异常时保留代码行号,保持源文件以及行号
-keepattributes SourceFile,LineNumberTable

#-----------------------------6.默认保留区-----------------------
# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(***);
}

#保持 Serializable 不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于带有回调函数onXXEvent的，不能混淆
-keepclassmembers class * {
    void *(**On*Event);
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}
# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}
# 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# 不混淆R文件中的所有静态字段，我们都知道R文件是通过字段来记录每个资源的id的，字段名要是被混淆了，id也就找不着了。
-keepclassmembers class **.R$* {
    public static <fields>;
}

#如果引用了v4或者v7包
-dontwarn android.support.**

# 保持哪些类不被混淆
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference

-keep class com.zhy.http.okhttp.**{*;}
-keep class com.wiwide.util.** {*;}

#一键登录
#"R.drawable.authsdk*",
#"R.layout.authsdk*",
#"R.anim.authsdk*",
#"R.id.authsdk*",
#"R.string.authsdk*",
#"R.style.authsdk*",

# LocalePlugin 混淆规则
-keep class com.mallotec.reb.localeplugin.** { *; }
-dontwarn com.mallotec.reb.localeplugin.**

#   //弹窗
-dontwarn com.lxj.xpopup.widget.**
-keep class com.lxj.xpopup.widget.**{*;}

# ============忽略警告，否则打包可能会不成功=============
-ignorewarnings