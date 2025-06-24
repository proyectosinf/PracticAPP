-keepattributes SourceFile,LineNumberTable

#### -- Support Library --
# Allow obfuscation of android.support.v7.internal.view.menu.**
# to avoid problem on Samsung 4.2.2 devices with appcompat v21
# see https://code.google.com/p/android/issues/detail?id=78377
-keep class !android.support.v7.internal.view.menu.**,android.support.** {*;}
-keep class * extends androidx.fragment.app.Fragment{}

-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable

### OKHTTP3 ###
-dontwarn com.squareup.okhttp3.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keep class okhttp3.**{ *; }

### TIMBER ###
-dontwarn org.jetbrains.annotations.NonNls

### Google Apis ###
-dontwarn com.google.android.**
-keep class com.google.android.**{ *; }
-keep interface com.google.android.** { *; }

## Kotlin Coroutines
-keepclassmembers class kotlinx.coroutines.DispatchedCoroutine {
    private <fields>;
}


### GSON ###
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**

# Project seems to run now even when encrypting data model classes, so these lines seems not to be necessary and will leave them commented for now
# Application classes that will be serialized/deserialized over Gson
#-keep class com.mobivery.fct25.data.network.model.** { *; }
#-keepclassmembers class com.mobivery.fct25.data.network.model.** { *; }
#-keep class com.mobivery.fct25.data.model.** { *; }
#-keepclassmembers class com.mobivery.fct25.data.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken



### RETROFIT 2 ###
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Kotlin serialization looks up the generated serializer classes through a function on companion
# objects. The companions are looked up reflectively so we need to explicitly keep these functions.
-keepclasseswithmembers class **.*$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}
# If a companion has the serializer function, keep the companion field on the original type so that
# the reflective lookup succeeds.
-if class **.*$Companion {
  kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers class <1>.<2> {
  <1>.<2>$Companion Companion;
}