# ProbeTools

ProbeTools displays the data from the Database and SharedPreferences in the browser.

## Setup
##### 1. To add it to your project, include the following in your **project** `build.gradle` file:
```groovy
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
##### 2. And on your **app module** `build.gradle`:

```groovy
compile 'com.github.pavel163:ProbeTools:1.0.0'
```

##### or Maven:
```xml
<dependency>
	    <groupId>com.github.pavel163</groupId>
	    <artifactId>ProbeTools</artifactId>
	    <version>1.0.0</version>
	</dependency>
```

##### 3. In your `Application` class:

```java
public class MyApplication extends Application {
  public void onCreate() {
    super.onCreate();
    ProbeTools.init(this);
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

