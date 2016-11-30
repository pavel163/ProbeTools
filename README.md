# ProbeTools

ProbeTools displays the data from the Database and SharedPreferences in the browser.

<p align="center"><img style="padding-left:20px" src ="https://github.com/pavel163/ProbeTools/blob/master/1.png" width="350"/>

<img style="padding-left:20px" src ="https://github.com/pavel163/ProbeTools/blob/master/2.png" width="350"/>
</p>

#### Opportunities
1. Viewing SharedPreferences.
2. Add new variables and change the old variables in SharedPreferences.
3. Upload database.
4. Viewing tables.
5. Create sql request.


## Setup
#### 1. To add it to your project, include the following in your **project** `build.gradle` file:
```groovy
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
#### 2. And on your **app module** `build.gradle`:

```groovy
compile 'com.github.pavel163:ProbeTools:1.0.1'
```

#### or Maven:
```xml
<dependency>
	    <groupId>com.github.pavel163</groupId>
	    <artifactId>ProbeTools</artifactId>
	    <version>1.0.1</version>
	</dependency>
```

#### 3. In your `Application` class:

```java
public class MyApplication extends Application {
  public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG){
        Probetools.init(this);
    }
  }
}
```
## Database
To work with the database you need:
```java
Probetools.setDBName(DB_NAME);
Probetools.setSQLiteOpenHelper(mySQLiteOpenHelper);
```
## SharedPreferences
The default `referenceManager.getDefaultSharedPreferences(context)`
For replacement use:
```java
Probetools.setPreferences(getSharedPreferences("pref", MODE_PRIVATE));
```
Open the browser and go to ip address in notification
<p align="center"><img style="padding-left:20px" src ="https://github.com/pavel163/ProbeTools/blob/master/Screenshot_20161130-170820.png" width="250"/>
</p>

License
--------

    Copyright 2016 Ergashev Bakht.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
