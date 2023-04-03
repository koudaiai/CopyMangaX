// Generated by view binder compiler. Do not edit!
package com.crow.module_discover.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.crow.module_discover.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DiscoverFragmentNovelBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final DiscoverFragmentAppbarBinding discoverNovelAppbar;

  @NonNull
  public final SmartRefreshLayout discoverNovelRefresh;

  @NonNull
  public final RecyclerView discoverNovelRv;

  @NonNull
  public final TextView discoverNovelTipsError;

  private DiscoverFragmentNovelBinding(@NonNull CoordinatorLayout rootView,
      @NonNull DiscoverFragmentAppbarBinding discoverNovelAppbar,
      @NonNull SmartRefreshLayout discoverNovelRefresh, @NonNull RecyclerView discoverNovelRv,
      @NonNull TextView discoverNovelTipsError) {
    this.rootView = rootView;
    this.discoverNovelAppbar = discoverNovelAppbar;
    this.discoverNovelRefresh = discoverNovelRefresh;
    this.discoverNovelRv = discoverNovelRv;
    this.discoverNovelTipsError = discoverNovelTipsError;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DiscoverFragmentNovelBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DiscoverFragmentNovelBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.discover_fragment_novel, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DiscoverFragmentNovelBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.discover_novel_appbar;
      View discoverNovelAppbar = ViewBindings.findChildViewById(rootView, id);
      if (discoverNovelAppbar == null) {
        break missingId;
      }
      DiscoverFragmentAppbarBinding binding_discoverNovelAppbar = DiscoverFragmentAppbarBinding.bind(discoverNovelAppbar);

      id = R.id.discover_novel_refresh;
      SmartRefreshLayout discoverNovelRefresh = ViewBindings.findChildViewById(rootView, id);
      if (discoverNovelRefresh == null) {
        break missingId;
      }

      id = R.id.discover_novel_rv;
      RecyclerView discoverNovelRv = ViewBindings.findChildViewById(rootView, id);
      if (discoverNovelRv == null) {
        break missingId;
      }

      id = R.id.discover_novel_tips_error;
      TextView discoverNovelTipsError = ViewBindings.findChildViewById(rootView, id);
      if (discoverNovelTipsError == null) {
        break missingId;
      }

      return new DiscoverFragmentNovelBinding((CoordinatorLayout) rootView,
          binding_discoverNovelAppbar, discoverNovelRefresh, discoverNovelRv,
          discoverNovelTipsError);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}