-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.Application
-keepclassmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclassmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class com.google.gson.** { *; }
-keep class org.json.** { *; }
-keep class com.example.douglasdev.models.** { *; }
-keepattributes Signature, Exception, *Annotation*, EnclosingMethod, InnerClasses