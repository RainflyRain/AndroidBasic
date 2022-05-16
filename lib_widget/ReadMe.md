可在项目中依赖此模块，使用此模块公用的资源

### 1. 添加默认颜色资源

```xml
<!--背景颜色-->
<color name="primaryColor">#e0f7fa</color>
<color name="primaryLightColor">#ffffff</color>
<color name="primaryDarkColor">#aec4c7</color>
<color name="secondaryColor">#90a4ae</color>
<color name="secondaryLightColor">#c1d5e0</color>
<color name="secondaryDarkColor">#62757f</color>
<!--文字颜色-->
<color name="primaryTextColor">#ffffff</color>
<color name="secondaryTextColor">#ffffff</color>
```



#### Button样式属性：

### 2. 添加默认Button样式

```
Widgets.Button
```

可继承，可重写

```xml
<item name="android:background">@drawable/selector_button</item>
<item name="android:textAllCaps">false</item>
<item name="android:textColor">@color/primaryTextColor</item>
<item name="android:stateListAnimator">@null</item>
```

项目中依赖使用lib_widget

```xml
 <!-- Base application theme. -->
<style name="AppTheme" parent="Theme.AppCompat.Light">
    <!--Customize your theme here.-->
    <item name="android:textColorPrimary">@color/primaryTextColor</item>
    <item name="android:textColorSecondary">@color/secondaryTextColor</item>
		<!--Button style-->
    <item name="buttonStyle">@style/Widgets.Button</item>
    <item name="android:buttonStyle">@style/Widgets.Button</item>
</style>
```



### 3. 添加ShapeButton、ShapeConstraintLayout、ShapeRelativeLayout
不推荐项目中使用，不支持背景选择器，