package tected.pet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import studios.codelight.smartloginlibrary.SmartCustomLoginListener;
import studios.codelight.smartloginlibrary.SmartLoginBuilder;
import studios.codelight.smartloginlibrary.SmartLoginConfig;
import studios.codelight.smartloginlibrary.manager.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;

//http://cuidados-cachorro-gatos.webnode.com/rss/
//http://www.procurasecachorro.com.br/blog/category/noticias/feed/

public class MainActivity extends AppCompatActivity { //implements OnMapReadyCallback

    //private GoogleMap mMap;
    private Drawer result = null;
    private AccountHeader headerResult = null;

    public List<SmartUser> usuarios;
    SmartUser currentUser;
    boolean gambiarraEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usuarios = new ArrayList<>();
        SmartUser su = new SmartUser();
        su.setEmail("admin_pet@gmail.com");
        su.setFirstName("Administrador do sistema");
        su.setPassword("admin12345");
        su.setUsername("admin");

        usuarios.add(su);

        su = new SmartUser();
        su.setEmail("usuario@gmail.com");
        su.setFirstName("Joao");
        su.setLastName("Silva");
        su.setPassword("123456789");
        su.setUsername("joaosilva");


        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).deleteRealmIfMigrationNeeded().build();

        // Get a Realm instance for this thread
        Realm realm = Realm.getInstance(realmConfig);

        SmartLoginBuilder loginBuilder = new SmartLoginBuilder();

        SmartCustomLoginListener loginListener = new SmartCustomLoginListener() {
            @Override
            public boolean customSignin(SmartUser smartUser) {
                Log.d("Main", "sign in");
                for(int i = 0; i < usuarios.size(); i++){
                    SmartUser user = usuarios.get(i);
                    if(smartUser.equals(usuarios.get(i)))
                        return true;
                    if(smartUser.getUsername() == user.getUsername() && smartUser.getPassword() == user.getPassword())
                        return true;
                }
                //return false;
                Toast.makeText(getApplicationContext(), "Usuário não cadastrado", Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean customSignup(SmartUser smartUser) {
                //do something with smartUser
                Log.d("Main", "sign up");
                return true;
            }

            @Override
            public boolean customUserSignout(SmartUser smartUser) {
                //do something with smartUser
                Log.d("Main", "sign out");
                return true;
            }
        };

        Intent intent = loginBuilder.with(getApplicationContext())
                .isCustomLoginEnabled(true)
                .setSmartCustomLoginHelper(loginListener)
                .build();
        startActivityForResult(intent, SmartLoginConfig.LOGIN_REQUEST);

        currentUser = UserSessionManager.getCurrentUser(getApplicationContext());
        if(currentUser != null){
            Log.d("Main", "usuario \"" + currentUser.getUsername() + "\" esta logado");
        }
        else{
            Log.d("Main", "usuario NOPE esta logado");
        }



        /*
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        */

        setSupportActionBar(toolbar);
        //final ActionBar ab = getSupportActionBar();

        List<IProfile> teste = makeList();

        //cabeçalho do drawer
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.mipmap.background_material)
                .withSelectionListEnabledForSingleProfile(false)
                .withProfiles(teste)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withTranslucentStatusBar(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName("Home"),
                        new SecondaryDrawerItem().withName("Cadastrar animal"),
                        new SecondaryDrawerItem().withName("Sobre")
                )
                .withSavedInstance(savedInstanceState)
                .build();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);

        final RealmResults<Cadastro> cadastros = realm.where(Cadastro.class).findAll();
        for (Cadastro c : cadastros) {
            Log.i("Main", c.getNomeDono() + " " + c.getTelefone());
        }
        realm.close();


    }

    List<IProfile> makeList(){
        List<IProfile> list = new ArrayList<>();
        if (currentUser != null) {
            list.add(new IProfile() {
                @Override
                public Object withName(String name) {
                    return currentUser.getFirstName();
                }

                @Override
                public StringHolder getName() {
                    return null;
                }

                @Override
                public Object withEmail(String email) {
                    return currentUser.getEmail();
                }

                @Override
                public StringHolder getEmail() {
                    return null;
                }

                @Override
                public Object withIcon(Drawable icon) {
                    return null;
                }

                @Override
                public Object withIcon(Bitmap bitmap) {
                    return null;
                }

                @Override
                public Object withIcon(@DrawableRes int iconRes) {
                    return null;
                }

                @Override
                public Object withIcon(String url) {
                    return null;
                }

                @Override
                public Object withIcon(Uri uri) {
                    return null;
                }

                @Override
                public Object withIcon(IIcon icon) {
                    return null;
                }

                @Override
                public ImageHolder getIcon() {
                    return null;
                }

                @Override
                public Object withSelectable(boolean selectable) {
                    return null;
                }

                @Override
                public boolean isSelectable() {
                    return false;
                }

                @Override
                public Object withIdentifier(long identifier) {
                    return null;
                }

                @Override
                public long getIdentifier() {
                    return 0;
                }
            });
        }
        return list;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getFragmentManager());
        adapter.addFragment(new Timeline_fragment(), "Timeline");
        adapter.addFragment(new Mapa_fragment(), "Região");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Intent "data" contains the user object
        if(resultCode == SmartLoginConfig.CUSTOM_LOGIN_REQUEST){
            SmartUser user = data.getParcelableExtra(SmartLoginConfig.USER);
            //use this user object as per your requirement
        }else if(resultCode == RESULT_CANCELED){
            //Login Failed
        }
    }

}
