# TextView Query
![query1](https://github.com/user-attachments/assets/8dbb8471-d90c-4790-918a-6147df6e7574)
![query2](https://github.com/user-attachments/assets/cdc24722-10db-4b81-9584-542de6262a01)

## How To Use
#### XML
```xml
<com.erif.library.TextViewQuery
    android:id="@+id/txt"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Indonesia"
    app:query="dones"
    app:highlightColor="#03A9F4"
    app:highlightTextStyle="bold"
    app:ignoreCase="true"
    app:highlightUnderline="true"/>
```

#### Result
<img width="323" height="70" alt="Screenshot 2025-07-24 at 15 05 12" src="https://github.com/user-attachments/assets/73aeee5a-8f2d-478b-a8df-caea9028ecb8" />

#### Kotlin
```kotlin
val txt: TextViewQuery = findViewById(R.id.txt)
txt.query = "Text from your search view"
```

## Installation
#### build.gradle (Project: Name)
```kotlin
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

### When using gradle.kts
#### settings.gradle.kts
```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
 }
```

#### build.gradle(Module: app)
```kotlin
implementation 'com.github.eriffanani:TextViewQuery:1.2.0'
```
#### build.gradle.kts(Module: app)
```kotlin
implementation("com.github.eriffanani:TextViewQuery:1.2.0")
```

### Licence
```license
Copyright 2023 Mukhammad Erif Fanani

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
