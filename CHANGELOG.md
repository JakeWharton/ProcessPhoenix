Change Log
==========

Version 3.0.0 *(2024-03-25)*
----------------------------

 * New: Support restarting `Service`s with `triggerServiceRebirth`.
 * New: Restart an `Activity` with only a `Class` reference.
 * Change: `ProcessPheonix` class no longer extends `Activity`. In practice no one should have been relying on this anyway.


Version 2.2.0 *(2024-02-26)*
----------------------------

 * New: Support for launching the default Leanback intent on Android TV.
 * Strict mode's unsafe intent policy is now automatically disabled before launching back into the main application.


Version 2.1.2 *(2021-08-13)*
----------------------------

 * Fix: Add `exported=false` to activity for Android 12 targetSdk compatibility.


Version 2.1.1 *(2021-06-25)*
----------------------------

 * Fix: Ensure a new, clear task stack is used when user intent(s) are supplied.


Version 2.1.0 *(2021-06-25)*
----------------------------

 * Fix: Ensure default activity can be found on Android 8.0 and newer.
 * Fix: Reduce/eliminate launcher flashing when performing a process restart.
 * Fix: Useless `BuildConfig` class is no longer included.


Version 2.0.0 *(2017-03-11)*
----------------------------

 * New: Support for starting multiple intents at once.


Version 1.1.1 *(2016-11-10)*
----------------------------

 * Fix: Avoid potential NPE inside `isPhoenixProcess` method due to Android returning an unexpected
   `null` value.


Version 1.1.0 *(2016-09-19)*
----------------------------

 * New: `isPhoenixProcess` method checks whether the current process belongs to the library. Use this
   to skip any initialization you would otherwise do for a normal process.


Version 1.0.2 *(2015-08-24)*
----------------------------

 * Minimum SDK version is now set to 4. Sorry Cupcake users!


Version 1.0.1 *(2015-07-31)*
----------------------------

 * Fix: Finish the current activity when triggering rebirth which prevents it from remaining in the
   backstack if a diffrent activity was started.
 * Minimum SDK version is now set to 3.


Version 1.0.0 *(2015-07-08)*
----------------------------

Initial import and release from [gist](https://gist.github.com/JakeWharton/9404647aa6a2b2818d22)
