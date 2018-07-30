# EmoticonsBoard
基于XhsEmoticonsKeyboard，添加了动态更新表情包的功能。使用EmoticonPacksAdapter，可以轻松的增减表情。

![Art](https://github.com/llwl1982/EmoticonsBoard/blob/master/doc/overlook.gif)

# Gradle Dependency

```xml  
allprojects {
    repositories {
        jcenter()
    }
}
```
and:

```xml
dependencies { 
    compile 'im.ll:emoticonsboard:1.0.0'
}
```
# Samples Usage
## XML

```xml
<github.ll.emotionboard.EmoticonsBoard xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <ListView
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
    </LinearLayout>

</github.ll.emotionboard.EmoticonsBoard>

```
<mark>EmoticonsBoard can only has one child view</mark>

## Set Adapter

Java code:

```
EmoticonsBoard ekBar;

List<EmoticonPack> packs = new ArrayList<EmoticonPack>;
init packs...

EmoticonPacksAdapter adapter = new EmoticonPacksAdapter(packs);
ekBar.setAdapter(adapter);
```
For details refer to the demo source



## Update data

![Art](https://github.com/llwl1982/EmoticonsBoard/blob/master/doc/change_data.gif)

Kotlin code:

```
private var adapter: EmoticonPacksAdapter? = null
private lateinit var mEmojiPack: EmoticonPack<Emoticon>
...
val emoticon = Emoticon()
emoticon.code = "new emoji"
emoticon.uri = "xxx"

mEmojiPack.emoticons.add(emoticon)
mEmojiPack.emoticons.add(emoticon)
mEmojiPack.isDataChanged = true
adapter?.notifyDataSetChanged()
```
<mark>Make modified emoticonPack's isDataChanged to true is very important </mark>