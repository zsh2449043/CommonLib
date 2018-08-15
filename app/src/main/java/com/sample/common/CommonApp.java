package com.sample.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.Nullable;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.github.moduth.blockcanary.BlockCanary;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okserver.OkDownload;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.sample.common.block.AppBlockCanaryContext;
import com.sample.common.database.DaoMaster;
import com.sample.common.database.DaoSession;
import com.sample.common.database.DatabaseContext;
import com.sample.common.database.EffectInfoDao;
import com.sample.common.database.MySQLiteOpenHelper;
import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class CommonApp extends Application {

    private static final String LOG_TAG = "zhshh";
    private static final String DB_NAME = "commonDB.db";
    private static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getPath() + "/test/";
    private static RxSharedPreferences sRxPreferences;
    private static EffectInfoDao sEffectInfoDao;
    private static CommonApp sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        // Normal app init code...
        sApp = this;
        Logger.d("onCreate() called start init ");
        LeakCanary.install(this);
        // Do it on main process
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        initLogger();
        initRxPreference();
        initStrictMode();
        initHttp();
        initDownload();
        Logger.d("onCreate() called init complete");
    }

    public static CommonApp getApp() {
        return sApp;
    }

    private void initStrictMode() {
        if (BuildConfig.OPEN_STRICT_MODE) {
            openStrictMode();
        }
    }

    public static EffectInfoDao getEffectInfoDao() {
        return sEffectInfoDao;
    }

    public static void initGreenDao(Context context) {

        try {
            //可能没有权限,因为保存在外部存储下，需要读写权限
            DatabaseContext databaseContext = new DatabaseContext(context);
            MySQLiteOpenHelper devOpenHelper = new MySQLiteOpenHelper(databaseContext, DB_NAME, null);
            DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
            DaoSession daoSession = daoMaster.newSession();
            sEffectInfoDao = daoSession.getEffectInfoDao();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("initDb error: ", e);
        }
    }

    private void initDownload() {
        OkDownload okDownload = OkDownload.getInstance();
        okDownload.setFolder(DOWNLOAD_PATH);
        okDownload.getThreadPool().setCorePoolSize(1);//同时下载
    }

    private void initHttp() {
        //构建OkHttpClient.Builder
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //*****************初始化log拦截器************************
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(LOG_TAG);
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.NONE);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        //*****************配置超时时间************************
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //*****************配置cookie持久化************************
        //使用sp保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
        //使用内存保持cookie，app退出后，cookie消失
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
        //*****************配置信任规则************************
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
        //HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        //builder.hostnameVerifier(new SafeHostnameVerifier());
        //*****************header/param************************
//        HttpHeaders headers = new HttpHeaders();
//        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
//        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1"); //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数");
        //*****************初始化okGo************************
        OkGo.getInstance().init(this)                             //必须调用初始化
                .setOkHttpClient(builder.build())                 //建议设置OkHttpClient，不设置将使用默认的
                //FIRST_CACHE_THEN_REQUEST   即使数据相同也会回调两遍，不太智能
                .setCacheMode(CacheMode.IF_NONE_CACHE_REQUEST) //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(1000 * 60 * 2)     //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(1);                             //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                        //全局公共头
//                .addCommonParams(params);                         //全局公共参数
    }

    public static RxSharedPreferences getRxPreferences() {
        return sRxPreferences;
    }

    private void initRxPreference() {
        SharedPreferences preferences = getDefaultSharedPreferences(this);
        sRxPreferences = RxSharedPreferences.create(preferences);
    }

    private void initLogger() {

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                //.methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                //.logStrategy(logStrategy) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag(LOG_TAG)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.OPEN_STRICT_MODE;
            }
        });
        Logger.d("initLogger: init Logger successful !");
    }

    private void openStrictMode() {
        StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder()
                        .detectAll()
                        .penaltyLog()
                        .build());

        StrictMode.VmPolicy.Builder vmPolicyBuilder =
                new StrictMode.VmPolicy.Builder()
                        .detectActivityLeaks()
                        .detectLeakedSqlLiteObjects()
                        .detectLeakedClosableObjects();
        vmPolicyBuilder.detectLeakedRegistrationObjects();
        vmPolicyBuilder.penaltyLog();
        StrictMode.setVmPolicy(vmPolicyBuilder.build());
    }
}
