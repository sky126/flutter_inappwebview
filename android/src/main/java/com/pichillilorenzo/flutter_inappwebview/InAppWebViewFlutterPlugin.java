package com.pichillilorenzo.flutter_inappwebview;

import android.net.Uri;
import android.os.Build;
import android.webkit.ValueCallback;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.shim.ShimPluginRegistry;

public class InAppWebViewFlutterPlugin implements FlutterPlugin {
  public PluginRegistry.Registrar registrar;
  public MethodChannel channel;

  protected static final String LOG_TAG = "InAppWebViewFlutterPlugin";

  public static InAppBrowser inAppBrowser;
  public static InAppWebViewStatic inAppWebViewStatic;
  public static MyCookieManager myCookieManager;
  public static CredentialDatabaseHandler credentialDatabaseHandler;
  public static ValueCallback<Uri[]> uploadMessageArray;

  public InAppWebViewFlutterPlugin() {}

  public static void registerWith(PluginRegistry.Registrar registrar) {
    inAppBrowser = new InAppBrowser(registrar);

    registrar
            .platformViewRegistry()
            .registerViewFactory(
                    "com.pichillilorenzo/flutter_inappwebview", new FlutterWebViewFactory(registrar, registrar.view()));
    new InAppWebViewStatic(registrar);
    new MyCookieManager(registrar);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      new CredentialDatabaseHandler(registrar);
    }
  }

  @Override
  public void onAttachedToEngine(FlutterPluginBinding binding) {
    //BinaryMessenger messenger = binding.getFlutterEngine().getDartExecutor();
    ShimPluginRegistry shimPluginRegistry = new ShimPluginRegistry(binding.getFlutterEngine());
    registrar = shimPluginRegistry.registrarFor("com.pichillilorenzo/flutter_inappwebview");
    inAppBrowser = new InAppBrowser(registrar);
    binding
            .getFlutterEngine()
            .getPlatformViewsController()
            .getRegistry()
            .registerViewFactory(
                    "com.pichillilorenzo/flutter_inappwebview", new FlutterWebViewFactory(registrar,null));
    inAppWebViewStatic = new InAppWebViewStatic(registrar);
    myCookieManager = new MyCookieManager(registrar);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      credentialDatabaseHandler = new CredentialDatabaseHandler(registrar);
    }
  }

  @Override
  public void onDetachedFromEngine(FlutterPluginBinding binding) {
    if (inAppBrowser != null) {
      inAppBrowser.dispose();
      inAppBrowser = null;
    }
    if (myCookieManager != null) {
      myCookieManager.dispose();
      myCookieManager = null;
    }
    if (credentialDatabaseHandler != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      credentialDatabaseHandler.dispose();
      credentialDatabaseHandler = null;
    }
    if (inAppWebViewStatic != null) {
      inAppWebViewStatic.dispose();
      inAppWebViewStatic = null;
    }
    uploadMessageArray = null;
  }
}
