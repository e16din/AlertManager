# AlertManager
Simple manager pop-up alerts

[![Release](https://jitpack.io/v/e16din/AlertManager.svg)](https://jitpack.io/#e16din/AlertManager)

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

## Download
Step 1. Add it in your root build.gradle at the end of repositories:
```groovy
    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```
Step 2. Add the dependency
```groovy
    dependencies {
        compile 'com.github.e16din:AlertManager:1.2.4'
    }
```

## Credits
This library use following libraries:
* [Material Dialogs](https://github.com/afollestad/material-dialogs)
* [MaterialDateTimePicker](https://github.com/wdullaer/MaterialDateTimePicker)
* [LightUtils](https://github.com/e16din/LightUtils)

## License MIT
Copyright (c) 2015 [Александр Кундрюков (e16din)](http://goo.gl/pzjc8x)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
