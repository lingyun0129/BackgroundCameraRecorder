package com.sth.camerabackgroundrecorder.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.psaravan.filebrowserview.lib.FileBrowserEngine.AdapterData;
import com.psaravan.filebrowserview.lib.Interfaces.NavigationInterface;
import com.psaravan.filebrowserview.lib.Utils.Utils;
import com.psaravan.filebrowserview.lib.View.AbstractFileBrowserAdapter;
import com.psaravan.filebrowserview.lib.View.FileBrowserView;
import com.sth.camerabackgroundrecorder.R;
import com.sth.camerabackgroundrecorder.util.AppPara;

import java.io.File;
import java.util.ArrayList;


public class SwipeMenuListActivity extends Activity {
    private SwipeMenuListView mListView;
    private FileBrowserView mFileBrowserView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_menu_list);
        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        mFileBrowserView = new FileBrowserView(this);

        ArrayList<String> fileExtension = new ArrayList<String>();
        fileExtension.add(".mp4");
        fileExtension.add(".3gp");
        fileExtension.add(".mov");
        fileExtension.add(".wmv");
        fileExtension.add(".avi");
        fileExtension.add(".sth");
        mFileBrowserView.excludeFileTypes(fileExtension, false);
        //Customize the view.
        mFileBrowserView.setFileBrowserLayoutType(FileBrowserView.FILE_BROWSER_LIST_LAYOUT) //Set the type of view to use.
                .setDefaultDirectory(new File(AppPara.getInstance().getSavePath() + "/")) //Set the default directory to show.
                .setShowHiddenFiles(true) //Set whether or not you want to show hidden files.
                .showItemSizes(true) //Shows the sizes of each item in the list.
                .showOverflowMenus(false) //Shows the overflow menus for each item in the list.
                .showItemIcons(true) //Shows the icons next to each item name in the list.
                .setNavigationInterface(navInterface) //Sets the nav interface instance for this view.
                .init(); //Loads the view. You MUST call this method, or the view will not be displayed.
        mListView.setAdapter(mFileBrowserView.getFileBrowserAdapter());

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
/*                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("播放");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.RED);
                // add to menu
                menu.addMenuItem(openItem);*/

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
/*                        // open
                        play(position);
                        break;
                    case 1:*/
                        // delete
                    {
                        AbstractFileBrowserAdapter adapter=mFileBrowserView.getFileBrowserAdapter();
                        AdapterData adapterData=adapter.getAdapterData();
                        File file=new File(adapterData.getPathsList().get(position));
                        if (file.exists()){
                            file.delete();
                        }
                        adapterData.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {

                    Utils.playVideo(SwipeMenuListActivity.this,mFileBrowserView.getFileBrowserAdapter().getAdapterData().getPathsList().get(position));
                }catch (Exception e){
                    Toast.makeText(SwipeMenuListActivity.this,"打开文件失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void play(int position){
        try {

            Utils.playVideo(SwipeMenuListActivity.this,mFileBrowserView.getFileBrowserAdapter().getAdapterData().getPathsList().get(position));
        }catch (Exception e){
            Toast.makeText(SwipeMenuListActivity.this,"打开文件失败", Toast.LENGTH_LONG).show();
        }
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     * Navigation interface for the view. Used to capture events such as a new
     * directory being loaded, files being opened, etc. For our purposes here,
     * we'll be using the onNewDirLoaded() method to update the ActionBar's title
     * with the current directory's path.
     */
    private NavigationInterface navInterface = new NavigationInterface() {

        @Override
        public void onNewDirLoaded(File dirFile) {
            //Update the action bar title.
            getActionBar().setTitle(dirFile.getAbsolutePath());
        }

        @Override
        public void onFileOpened(File file) {

        }

        @Override
        public void onParentDirLoaded(File dirFile) {

        }

        @Override
        public void onFileFolderOpenFailed(File file) {

        }

    };
}
