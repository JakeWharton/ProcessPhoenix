Process Phoenix
===============

Process Phoenix facilitates restarting your application process.

This should only be used for things like fundamental state changes in your debug builds (e.g.,
changing from staging to production).



Usage
-----

Start the default activity in a new process:
```java
ProcessPhoenix.triggerRebirth(context);
```

Or, if you want to launch with a specific `Intent`:
```java
Intent nextIntent = //...
ProcessPhoenix.triggerRebirth(context, nextIntent);
```

To check if your application is inside the Phoenix process to skip initialization in `onCreate`:
```java
if (ProcessPhoenix.isPhoenixProcess(this)) {
  return;
}
```



Download
--------

```groovy
compile 'com.jakewharton:process-phoenix:2.0.0'
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].



License
-------

    Copyright (C) 2015 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.




 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
