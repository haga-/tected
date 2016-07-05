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
import android.view.View;
import android.widget.Toast;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
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

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);

        usuarios = new ArrayList<>();
        SmartUser su = new SmartUser();
        su.setEmail("admin_pet@gmail.com");
        su.setFirstName("Administrador do sistema");
        su.setPassword("admin12345");
        su.setUsername("admin");
        final IProfile p1= new ProfileDrawerItem().withName(su.getFirstName() + " " + su.getLastName()).withEmail(su.getEmail());

        usuarios.add(su);

        su = new SmartUser();
        su.setEmail("usuario@gmail.com");
        su.setFirstName("Joao");
        su.setLastName("Silva");
        su.setPassword("123456789");
        su.setUsername("joaosilva");
        final IProfile p2 = new ProfileDrawerItem().withName(su.getFirstName() + " " + su.getLastName()).withEmail(su.getEmail());

        usuarios.add(su);

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).deleteRealmIfMigrationNeeded().build();

        // Get a Realm instance for this thread
        Realm realm = Realm.getInstance(realmConfig);

        final RealmResults<Cadastro> cadastros = realm.where(Cadastro.class).findAll();
        for (Cadastro c : cadastros) {
            Log.i("Main", c.getNomeDono() + " " + c.getTelefone());
        }
        realm.close();

        /*
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        */

        final SmartLoginBuilder loginBuilder = new SmartLoginBuilder();

        final SmartCustomLoginListener loginListener = new SmartCustomLoginListener() {
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
                UserSessionManager.logout(MainActivity.this, smartUser);
                //acho que vai dar ruim
                Log.d("Main", "sign out");
                return true;
            }
        };

        currentUser = UserSessionManager.getCurrentUser(getApplicationContext());
        if(currentUser == null) {
            Intent intent = loginBuilder.with(getApplicationContext())
                    .isCustomLoginEnabled(true)
                    .setSmartCustomLoginHelper(loginListener)
                    .build();
            startActivityForResult(intent, SmartLoginConfig.LOGIN_REQUEST);
            Log.d("Main", "usuario NOPE esta logado");
            currentUser = UserSessionManager.getCurrentUser(getApplicationContext());
            if(currentUser == null){
                Log.d("Main", "não logou msm");
            }
            else{
                Log.d("Main", "usuario \"" + currentUser.getUsername() + "\" esta logado na segunda tentativa");
            }

        }
        else{
            Log.d("Main", "usuario \"" + currentUser.getUsername() + "\" esta logado");
        }



        if(currentUser != null) {

            IProfile profile = currentUser.getUsername().equals("admin") ? p1 : p2;

            //cabeçalho do drawer
            headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(R.mipmap.background_material)
                    .withSelectionListEnabledForSingleProfile(false)
                    .addProfiles(profile)
                    .build();
            //if(currentUser.getUsername().equals("Administrador do sistema")) {
            if(currentUser.getUsername().equals("admin")) {
                result = new DrawerBuilder()
                        .withActivity(this)
                        .withToolbar(toolbar)
                        .withAccountHeader(headerResult)
                        .withTranslucentStatusBar(true)
                        .withCloseOnClick(true)
                        .withSelectedItem(-1)
                        .addDrawerItems(
                                new SecondaryDrawerItem().withName("Home").withIdentifier(1).withSelectable(false),
                                new SecondaryDrawerItem().withName("Cadastrar animal").withIdentifier(2).withSelectable(false),
                                new SecondaryDrawerItem().withName("Informar animal perdido").withIdentifier(3).withSelectable(false),
                                new SecondaryDrawerItem().withName("Informar animal encontrado").withIdentifier(4).withSelectable(false),
                                new SecondaryDrawerItem().withName("Deletar Banco").withIdentifier(5).withSelectable(false),
                                new SecondaryDrawerItem().withName("Sobre").withIdentifier(6).withSelectable(false),
                                new SecondaryDrawerItem().withName("Logout").withIdentifier(7).withSelectable(false)
                        )
                        .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                if(drawerItem != null){
                                    long id = drawerItem.getIdentifier();
                                    if(id == 7) {
                                        Log.d("Main", "retorno de logout " +  UserSessionManager.logout(MainActivity.this, currentUser));

                                    }
                                    else if(id == 5){
                                        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).deleteRealmIfMigrationNeeded().build();
                                        Realm realm = Realm.getInstance(realmConfig);
                                        realm.beginTransaction();
                                        realm.deleteAll();
                                        realm.commitTransaction();
                                        realm.close();
                                        Log.d("Main", "click on delete banco");
                                    }
                                }
                                return false;
                            }
                        })
                        .withSavedInstance(savedInstanceState)
                        .build();
            }
            else{
                result = new DrawerBuilder()
                        .withActivity(this)
                        .withToolbar(toolbar)
                        .withAccountHeader(headerResult)
                        .withTranslucentStatusBar(true)
                        .withCloseOnClick(true)
                        .withSelectedItem(-1)
                        .addDrawerItems(
                                new SecondaryDrawerItem().withName("Home").withIdentifier(1).withSelectable(false),
                                new SecondaryDrawerItem().withName("Cadastrar animal").withIdentifier(2).withSelectable(false),
                                new SecondaryDrawerItem().withName("Informar animal perdido").withIdentifier(3).withSelectable(false),
                                new SecondaryDrawerItem().withName("Informar animal encontrado").withIdentifier(4).withSelectable(false),
                                new SecondaryDrawerItem().withName("Sobre").withIdentifier(5).withSelectable(false),
                                new SecondaryDrawerItem().withName("Logout").withIdentifier(6).withSelectable(false)
                        )
                        .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                if (drawerItem != null) {
                                    long id = drawerItem.getIdentifier();
                                    if(id == 6) {
                                        Log.d("Main", "retorno de logout " +  UserSessionManager.logout(MainActivity.this, currentUser));

                                    }
                                }
                                return false;
                            }
                        })
                        .withSavedInstance(savedInstanceState)
                        .build();
            }
        }
        else{

            //cabeçalho do drawer
            headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(R.mipmap.background_material)
                    .withSelectionListEnabledForSingleProfile(false)
                    .build();

            result = new DrawerBuilder()
                    .withActivity(this)
                    .withToolbar(toolbar)
                    .withAccountHeader(headerResult)
                    .withTranslucentStatusBar(true)
                    .withCloseOnClick(true)
                    .withSelectedItem(-1)
                    .addDrawerItems(
                            new SecondaryDrawerItem().withName("Home").withIdentifier(1).withSelectable(false),
                            //new SecondaryDrawerItem().withName("Cadastrar animal").withIdentifier(1).withSelectable(false),
                            new SecondaryDrawerItem().withName("Informar animal perdido").withIdentifier(2).withSelectable(false),
                            new SecondaryDrawerItem().withName("Informar animal encontrado").withIdentifier(3).withSelectable(false),
                            new SecondaryDrawerItem().withName("Sobre").withIdentifier(4).withSelectable(false),
                            new SecondaryDrawerItem().withName("Login").withIdentifier(5).withSelectable(false)
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            if (drawerItem != null) {
                                long id = drawerItem.getIdentifier();
                                if(id == 5) {
                                    Intent intent = loginBuilder.with(getApplicationContext())
                                            .isCustomLoginEnabled(true)
                                            .setSmartCustomLoginHelper(loginListener)
                                            .build();
                                    startActivityForResult(intent, SmartLoginConfig.LOGIN_REQUEST);
                                    SmartUser user = UserSessionManager.getCurrentUser(getApplicationContext());
                                    if(user != null){
                                        Log.d("Main", "Usuario logado: \"" + user.getUsername() + "\"");
                                    }
                                    else{
                                        Log.d("Main", "Usuario nao logou");
                                    }
                                }
                            }
                            return false;
                        }
                    })
                    .withSavedInstance(savedInstanceState)
                    .build();
        }


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
            Log.d("Main", user.getUsername() + " on activityresult");
        }else if(resultCode == RESULT_CANCELED){
            //Login Failed
        }
    }

}
