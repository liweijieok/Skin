# 基于MVVM的换肤方案

Github地址：[SKin](https://github.com/liweijieok/Skin)，本文的思路来自于[Databinding+LiveData轻松实现无重启换肤](https://juejin.cn/post/7061453421231996958)

![Demo](https://i.328888.xyz/img/2022/12/03/VZqRC.md.png)

## 皮肤描述

我们在开发中会有各种个样式的皮肤，比如`白天`(默认皮肤)，`夜间`，`公祭日`，`专属会员`等等的，根据他们的属性，我划分为了2中类型：

1. 互斥皮肤，即展示了某一种之后，另外一种就不能展示，比如白天与夜间，白天与会员，即展示了夜间就不展示白天的，展示了会员的就不展示白天的。
2. 伴生皮肤，即每个皮肤都会拥有的另外一个形态，比如公祭日皮肤，白天，夜间，等级会员等，他们都会有公祭日的皮肤。

以上针对是同一个UI控件的展示来做的区分。同样的，比如对于会员来说，`白天会员`与`夜间会员`，按照这里的区分来说是不同的两套皮肤。


## 换肤方案

本文是基于MVVM中的ViewModel与DataBinding来实现换肤，主要的feature是：
1. 无需重启即可换肤
2. 代码层次明了，结构清晰
3. 无内存泄漏问题，不会hook系统的`api`，没有`if/else`类型的代码块
4. 安装以后如果需要`新增`资源则需要升级app。`修改`资源也是需要升级app，当然我们可以考虑做一套资源更新的系统来实现`不更新App`达到资源换肤。

他的缺点也是比较明显的：
1. 会增大安装包大小
2. 灵活度一般，当然假如我们可以维护一套资源更新系统那就还可以(后面说)。

## 主要实现

由于我们使用的是应用内的换肤，所以我们必须先把我们可以做到的换肤的类型定义出来，比如：
```
enum class AppThemeType {
    DEFAULT,RED, GREEN // 分别是默认类型，红色，绿色
}
```

然后，我们利用`gradle`的`sourceSets`功能，把所有的皮肤定义在业务代码之外，独立开来，比如改成下面这样的构造：

![代码结构](https://i.328888.xyz/img/2022/12/03/iuXd5.md.png)

我们的一套皮肤中会有自身资源与它的伴生资源，比如`skin`中就是有自身的资源与自身对应的伴生皮肤资源，他有多少个伴生皮肤就有多少个伴生资源。

为了让代码层次明了，结构清晰，我们需要做一些代码的约定：
1. 所以涉及到换肤的资源，都需要写在换肤对应的文件中，包括默认的都需要独立一个资源文件，比如截图的`skin`。
2. 我们的所有xml资源，都需要以自己所属的资源包名称作为前缀，其中伴生资源需要增加`companion`关键字，比如`skin`皮肤默认的资源名称开头需要为`skin`，比如`color`，`drawable`，`dimen`等，它的伴生皮肤的资源开头需要以`skin_companion`开头。

最终，通过`sourceSets`把皮肤中的代码和资源合并进去，例如：
```
sourceSets {
        main {
            res.srcDirs = ['src/main/res', 'src/skin-red/main/res', 'src/skin-green/main/res']
            java.srcDirs = ['src/main/java', 'src/skin-red/main/java', 'src/skin-green/main/java']
        }
    }
```

我们是通过`DataBinding`来实现的换肤，所以我们是在xml中插入java代码来实现对资源的使用，假如控件不支持我们也可以使用`@BindingAdapter`改造来达到xml中使用代码的目的。

目前我们的皮肤包中是有自己对应的资源与伴生资源，那么我们就需要读取他们。我们利用的是`ViewModel`，每个页面都有自己的`ViewModel`，我们通过ViewModel中持有`ObservableField<Theme>`的方式，在初始化设置默认的`ObservableField<Theme>` or 后续更换皮肤/切换伴生模式的时候设置`ObservableField<Theme>`，来更新到xml中控件资源。

首先我们定义个`open`类`AppBaseTheme`，来设置一些每个页面都使用的资源，比如通用的字体颜色或者其他icon等，我们的所有`Theme`都继承自改类。

然后我们给每一个需要有换肤的页面(Activity/Fragment)定义一个专属的`AppTheme`，然后在发起换肤的时候就更改该`AppTheme`值。因为都需要，所以定义一个`BaseSkinModel`类，需要换肤的页面的ViewModel都需要继承自改类。

```
abstract class BaseSkinModel<T : AppBaseTheme> : ViewModel() {
    val theme = ObservableField<T>()
}
```

我们在xml中就可以读取`theme`变量来设置xml中的属性，比如字体颜色，大小，背景等的xml属性，假如控件不支持的可以通过扩展`@BindingAdapter`来设置。

那么我们如何获取每个皮肤的对应的`Theme`对象呢？是获取当前皮肤的`Theme`还是伴生的`Theme`？做法就是我们会在每一个皮肤中的java文件夹下，定义对应页面的对应皮肤的`AppTheme`，他们继承自主工程的对应页面`Theme`。比如Demo中`FirstTheme`是`FirstFragment`默认的皮肤设置，`GreenFirstTheme`是`FirstFragment`在`skin_green`时候的配置，后者就需要继承前者。

而关于伴生皮肤呢？我们可以让他继承自默认皮肤的伴生，也可以继承自自己的伴生，看那种可以复用较多用那种即可。比如Demo中的是`red`和`green`的伴生继承自默认的伴生。

那么我们如何给`theme`设置对象呢？如果决定使用的皮肤自身还是他的伴生皮肤呢？首先是设置ViewModel中的`theme`，有三种时机需要设置
1. 初始化的时候，读取上一次的配置
2. 更换皮肤的时候，比如由白天到黑夜的皮肤。
3. 切换伴生的时候，比如我需要展示公祭日，那么无论是那种情况下的皮肤都需要展示他的公祭日样式。

我们定义一个获取伴生还是自身的`Theme`的接口
```
interface IAppBaseTheme<T : AppBaseTheme> {
    /**
     * 当前主题
     * @return
     */
    fun theme(): T

    /**
     * 伴生主题，类似，优先级比theme高，当开启了之后有限使用companionTheme
     * @return
     */
    fun companionTheme(): T
}
```

然后定义一个决定是使用伴生还是自身的抽象类，继承了`IAppBaseTheme`
```
abstract class AppBaseThemeOwner<T : AppBaseTheme> : IAppBaseTheme<T> {
    /**
     * 获取主题
     * @return
     */
    fun getTheme(): T {
        return if (AppThemeController.isShowCompanion) companionTheme() else theme()
    }

}
```
我们通过`AppThemeController`的`isShowCompanion`来决定使用自身还是伴生样式，我们的每个皮肤(包括默认)，都继承`AppBaseThemeOwner`，去实现`IAppBaseTheme`接口，返回对应自身以及伴生的皮肤样式对象。比如Demo中的`FirstThemeOwner`
```
open class FirstThemeOwner : AppBaseThemeOwner<FirstTheme>() {
    override fun theme(): FirstTheme {
        return FirstTheme()
    }

    override fun companionTheme(): FirstThemeCompanionTheme {
        return FirstThemeCompanionTheme()
    }
}

open class FirstTheme : AppBaseTheme() {
    open val btnTextColor = ResUtil.getColor(R.color.skin_btn_text_color)
}

open class FirstThemeCompanionTheme : FirstTheme() {
    override val btnTextColor = ResUtil.getColor(R.color.skin_companion_btn_text_color)
}
```

然后就是我们需要在ViewModel中获取具体的`Theme`了，设置对象我们可以`new`，也可以通过反射的方式，随意。这里的Demo使用了反射，我们在`BaseSkinModel`定义一个抽象方法来返回不同皮肤对应的`AppBaseThemeOwner`的class，然后发射生成`AppBaseThemeOwner`对象，调用他的`getTheme()`方法，
```
abstract class BaseSkinModel<T : AppBaseTheme> : ViewModel(), ISkinChange {
    val theme = ObservableField<T>()

    init {
        AppThemeController.registerSkinChange(this)
        val now = getSkins()[AppThemeController.getCurrent()]
        kotlin.runCatching { now?.newInstance() }
            .onFailure { it.printStackTrace() }
            .getOrNull()?.let {
                theme.set(it.getTheme())
            }
    }

    override fun onCleared() {
        super.onCleared()
        AppThemeController.unregisterSkinChange(this)
    }

    override fun onSkinChange(theme: AppThemeType) {
        val skins = getSkins()
        if (skins.isEmpty()) {
            return
        }
        val target = skins[theme] ?: return
        kotlin.runCatching {
            target.newInstance()
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()?.let {
            this.theme.set(it.getTheme())
        }
    }

    /**
    * 返回对应皮肤的AppBaseThemeOwner的Class集合，通过发射生成AppBaseThemeOwner对象，然后调研getTheme方法获取具体的皮肤Theme对象
    */
    abstract fun getSkins(): Map<AppThemeType, Class<out AppBaseThemeOwner<T>>>

}
```

比如在实现类中，比如`FirstViewModel`
```
class FirstViewModel : BaseSkinModel<FirstTheme>() {

    override fun getSkins(): Map<AppThemeType, Class<out FirstTheme>> {
        return mapOf(
            AppThemeType.DEFAULT to FirstTheme::class.java,
            AppThemeType.RED to RedFirstTheme::class.java,
            AppThemeType.GREEN to GreenFirstTheme::class.java
        )
    }

}
```
这样我们的`FirstFragment`就支持了3中换肤了，其中每种换肤自身又有伴生皮肤。这里看到初始化的时候,`theme`的值是通过反射生成的。

解决完成了初始化之后，我们再解决更新的问题：即换肤的时候如何通知到每个页面的`theme`，以及切换伴生的时候如何通知。
我们可以通过观察着模式来实现，例如
```
object AppThemeController {

    // 当前模式
    private var currentMode = AppThemeType.RED

    var isShowCompanion = false

    val globalTheme by lazy { MutableLiveData<GlobalTheme>() }

    private val mListener = mutableListOf<ISkinChange>()

    fun getCurrent() = currentMode

    @Synchronized
    fun registerSkinChange(listener: ISkinChange) {
        mListener.add(listener)
    }

    @Synchronized
    fun unregisterSkinChange(listener: ISkinChange) {
        mListener.remove(listener)
    }

    /**
     * 切换伴生皮肤
     */
    fun changeCompanion() {
        isShowCompanion = !isShowCompanion
        changeSkin(currentMode)
    }
    @Synchronized
    fun changeSkin(newTheme: AppThemeType) {
        // TODO 如果需要globalTheme，可以在这里设置
        currentMode = newTheme
        if (mListener.isEmpty()) {
            return
        }
        mListener.forEach {
            it.onSkinChange(newTheme)
        }
    }
}
fun interface ISkinChange {
    fun onSkinChange(theme: AppThemeType)
}

open class GlobalTheme : AppBaseTheme() {
    // 可以设置一些不跟随页面变化的主题数
    // 不同的主题可以有不同的GlobalTheme
    // 子主题复写当前类即可
}
```

我们定义了`AppThemeController`，需要感知换肤时间的可以通过注册来得知，通过反注册来避免内存泄露，而我们的每个需要换肤的ViewModel都需要感知，所以最终我们的`BaseSkinModel`的`onCleared`中调用` AppThemeController.unregisterSkinChange(this)`,避免内存泄露。

同样的，伴生皮肤的更新也是通过观察者模式实现。

具体的使用就是ViewModel对象注入到了xml中，然后再xml中调用我们的`theme`来使用资源，比如
```
...
    <data>
        <variable
            name="vm"
            type="com.example.daynightmode.FirstViewModel" />
    </data>
...
        <TextView
            android:id="@+id/textview_first"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/hello_first_fragment"
            app:layout_constraintBottom_toTopOf="@id/button_first"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:tvColor="@{vm.theme.textColor}" />

        <Button
            android:id="@+id/button_first"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/next"
            android:textColor="@{vm.theme.btnTextColor}"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/button_first" />
...
</layout>
```

通过上面的这一套，我们就可以较完美的实现应用内的换肤功能了，一般步骤就是
1. 定义皮肤种类，创建对应的文件夹，通过`sourceSets`加入源码中，需要注意的是资源的名称前缀是最好以当前资源名称开头，好做区分和维护。
3. 定义`BaseSkinModel`，持有当前皮肤`theme`对象，然后再xml中通过引用该对象的属性设置对应的属性值
4. 定义皮肤包中对应页面的`AppThemeController`，然后通过他获取当前皮肤的伴生对象或者是自身的`Theme`，这些Theme类需要继承自对应页面的默认`Theme`，然后复写有需要专属于当前皮肤的配置即可。然后通过对应页面的ViewModel的`getSkins`方法返回`AppThemeController`的Class。
5. 通过观察者模式来注册换肤事件，在不需要的地方清除。
 
## 其他

1. 某些页面或者不好获取到ViewModel对象，比如我们的全局Taost设置的背景，颜色等，那么我们应该如何处理？我推荐的方案是定义一个`GlobalTheme`，在`AppThemeController`中持有该对象，切换资源或者是伴生的时候修改它，然后我们无法方便读取到ViewModel的地方就通过获取该`GlobalTheme`设置资源值，同时注册一个`ISkinChange`来感知换肤，伴生切换事件。
2. 针对与使用Java的方式来获取资源，而不是xml的情况：比如我某个控件的属性就是需要通过代码设置，那么我推荐的处理方案是：
    a. 假如能够获取到他所属页面的ViewModel，就调用该ViewModel对象的`theme`来获取资源，同时注册`ISkinChange`来感知换肤，伴生切换事件
    b. 假如也较难获取或者无法获取到ViewModel对象，则定义一套与`BaseSkinModel`类似的架构来获取`theme`。

## 动态换肤

因为本文使用应用内的换肤，所以一般无法做到资源更新，但是假如是必须的要做资源的随时可更新，那么我有两个建议

1. 使用配置下发的方式实现动态更新，比如我们在皮肤中定义了一些资源名称，我们就可以通过接口下发的时候下这些名称对应的资源。读取的时候优先从数据库中读取，不存在我们再去使用应用内定义的。比如我们定义了一个`color`，名称为`skin_text_color`，那么我们下发的数据里面，就下发一个`skin`皮肤下`color`属性的名称为`skin_text_color`的可转为颜色的资源(比如`#f00`)，然后该数据存入数据库中。去读的时候，通过通过id拿到资源的名称，即`getResourceEntryName`方法，然后再去获取数据库中对应`name`的对应属性的值。图片的也是类似的，假如我们定义了一个`drawable`或者是本地图片，是`skin_red`中的资源，名称为`skin_red_bg`。我们下发数据的时候，就下发一个`skin_red`中`drawable`属性的`名称为`skin_red_bg`,值为`xxx`的数据，然后插入数据库，同时下发一张图片名称也为`xxx`。我们的空间设置资源的时候通过`@BindingAdapter`实现，`Theme`中返回一个`Drawable`对象，有限读取本地对应皮肤的`skin_red_bg`属性的值，这里的话就是`xxx`，然后获取该图片返回一个`Drawable`对象，假如不存在的话就使用应用内的。
2. 使用插件化，比如现在的[Android-Skin-Loader](https://github.com/fengjundev/Android-Skin-Loader)和[Android-skin-support](https://github.com/ximsfei/Android-skin-support)框架。使用他们作为一个兜底策略，通过开关控制。打开的时候，我们所有`Theme`中的资源的获取都通过框架去读取而不是我们直接去获取，然后我们还需要在换肤事件/伴生切换去重新通过框架更新资源。当然他可能还存在一些其他的兼容性问题，毕竟使用了反射去hook系统的api。还有一点就是使用框架的时候不建议hook系统的`setFacotry`方法，我们只用框架的获取资源的方法即可，这样能避免少出一些系统兼容的问题，我们只用框架读取资源即可，设置空间的属性还是可以通过`DataBinding`设置。

本文只是重在讲述换肤的方案，具体的实现大家也可以具体去发挥，大致的思路就是不同的皮肤不同文件去管理，然后获取不同文件中的资源进行设置。