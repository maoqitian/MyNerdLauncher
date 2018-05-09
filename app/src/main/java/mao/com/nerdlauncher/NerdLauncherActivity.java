package mao.com.nerdlauncher;

import android.support.v4.app.Fragment;

import mao.com.nerdlauncher.fragment.NerdLauncherFragment;


public class NerdLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }
}
