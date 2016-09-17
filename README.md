# AlertManager
Simple manager pop-up alerts

[![Release](https://jitpack.io/v/e16din/AlertManager.svg)](https://jitpack.io/#e16din/AlertManager)

## Download (Gradle)

```groovy
repositories {
    maven { url "https://jitpack.io" }
}

buildscript {
    repositories {
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    compile 'com.github.e16din:AlertManager:1.2.0'
}
```

## Usage examples
### Show
```java
Show.message(activity, "Hi!").dialog();

Show.message(activity, "Hi!").toast();

Show.message(activity, "Hi!").snackbar();
```
### AlertManager
```java
AlertManager.manager(context).showAlert("Show must go on!");

AlertManager.manager(context).showAlert(R.string.any_message,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //do something on "Ok" button click
                                            }
                                        });
```

## Change title of alert dialogs
```java
setCustomAlertTitle(R.string.my_title);
setCustomErrorTitle(R.string.my_error_title);
```

# Credits
This library use following libraries:
* [Material Dialogs](https://github.com/afollestad/material-dialogs)
* [MaterialDateTimePicker](https://github.com/wdullaer/MaterialDateTimePicker)
* [LightUtils](https://github.com/e16din/LightUtils)