# MBDA

# INFO
### Reset location permissons with: 
C:\Users\USER_NAME\AppData\Local\Android\Sdk\platform-tools\adb.exe shell pm reset-permissions

### If location doesn't work in emulator startup google maps once, and reinstall the app.
Still doesn't fully work in the emulator for me, but works fine on real phone.

### If search stops working, the API key has probably reached its daily max.
There are multiple others at the top of MainActivity, and you can generate your own.

# TODO
* redirect to the youtube video in the youtube app, on click in results
* load youtube video title into app, from the youtube app
* store some settings 
* fill in searchbar, on click search history item

* nice to have :
   * cleanup code
   * optimize api calls (scrolling causes an excessive amount of api calls)
   * Extra features
