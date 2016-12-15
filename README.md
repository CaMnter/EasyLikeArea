EasyLikeArea
==

**Easy like area in the circle of friends or QQ qzone**

![Language](https://img.shields.io/badge/language-Java-EE0000.svg) [![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/CaMnter/EasyLikeArea/blob/master/LICENSE) 
![SDK](https://img.shields.io/badge/SDK-9%2B-orange.svg)
 [ ![Download](https://api.bintray.com/packages/camnter/maven/EasyLikeArea/images/download.svg) ](https://bintray.com/camnter/maven/EasyLikeArea/_latestVersion)
    
<br>    
   
##Introduction

EasyViewProxy is cache manager of EasyLikeArea . The number of the View the default cache is 17.

Suggestion: Use Glide rendering images, then add into EaseLikeArea .
    
<br>    
   
## Gradle

```groovy
dependencies {
	compile 'com.camnter.easylikearea:easylikearea:1.5'
}
```
    
<br>    
   
## Attributes

```xml
<declare-styleable name="EasyLikeArea">
    <attr name="easyLayoutDirection">
        <enum name="left"
            value="2601"/>
        <enum name="right"
            value="2602"/>
    </attr>
    <attr name="easyLikeAreaLikeSpacing"
        format="dimension"/>
    <attr name="easyLikeAreaOmitSpacing"
        format="dimension"/>
    <attr name="easyLikeAreaOmitCenter"
        format="boolean"/>
</declare-styleable>
```
    
<br>    
   
## Easy to use

More details, we can see the demo.

```xml
<com.camnter.easylikearea.EasyLikeArea
    android:id="@+id/topic_ela"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_below="@id/topic_content_bottom_v"
    android:background="@color/white"
    android:paddingBottom="10dp"
    android:paddingLeft="12.5dp"
    app:easyLikeAreaOmitCenter="true"
    app:easyLikeAreaLikeSpacing="5dp"
    app:easyLikeAreaOmitSpacing="8dp"
    android:paddingRight="12.5dp"
    android:paddingTop="10dp"
    app:easyLayoutDirection="right"/>
```
   
**Attention:** You must addView(...)  after EasyLikeArea.setOmitView(View view) .
```java
private void initLikeArea() {
    this.setOmitView(Constant.AVATARS.length);
    for (int idRes : Constant.AVATARS) {
        EasyLikeImageView iv = this.createEasyLikeImageView();
        GlideUtils.displayNative(iv, idRes);
        this.topicEla.addView(iv);
    }
}


public void setOmitView(int count) {
    View omitView = LayoutInflater.from(this).inflate(R.layout.view_omit_style_topic, null);
    this.omitTv = (TextView) omitView.findViewById(R.id.topic_omit_tv);
    this.omitTv.setText(this.getString(this.getOmitVieStringFormatId(), count));
    this.topicEla.setOmitView(omitView);
}
```
  
And you can use the **EasyLikeImageView**
```java
private EasyLikeImageView createEasyLikeImageView() {
    EasyLikeImageView iv = new EasyLikeImageView(this);
    iv.setLayoutParams(new ViewGroup.LayoutParams(this.dp2px(36), this.dp2px(36)));
    return iv;
}
```
  
**EasyLikeImageView Attributes**
```xml
<declare-styleable name="EasyLikeImageView">
    <attr name="easyLikeImageType">
        <enum name="round" value="2601" />
        <enum name="circle" value="2602" />
    </attr>
    <attr name="easyLikeImageBorderRadius" format="dimension" />
</declare-styleable>
```
    
<br>    
   
## Screenshots

<img src="http://ww2.sinaimg.cn/large/006lPEc9jw1f2gdeeubxjg30fw0sg7i3.gif" width="320x">   

|    Scene   |     Qzone    |      Style     |
| :--------: | :-----------:| :------------: |
| | <img src="http://ww2.sinaimg.cn/large/006lPEc9gw1f30vuhhd0zj31ay298aj8.jpg" width="420x">             |  <img src="http://ww3.sinaimg.cn/large/006lPEc9gw1f30vus8l05j31ay2987f1.jpg" width="420x">             |  

  
## Compare resolution 

| Resolution |    480x800   |    720x1280   |
| :--------: | :-----------:| :-----------: |
| | <img src="http://ww2.sinaimg.cn/large/006lPEc9jw1f2gd4fx6ypj30qk186adm.jpg" width="320x">             |  <img src="http://ww3.sinaimg.cn/large/006lPEc9jw1f2gd4u3eipj30qq1baq69.jpg" width="320x">             |


## License

      Copyright (C) 2016 CaMnter yuanyu.camnter@gmail.com

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.







