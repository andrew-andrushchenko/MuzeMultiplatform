# Muze Multiplatform

Multiplatform client application for
[Muze RESTful Web Service](https://github.com/andrew-andrushchenko/MuzeServer).

# Build
Add this line to the project's **local.properties** file:
```
base_url=[YOUR BACKEND URL]
```


# Currently supported platforms
- :iphone: Android<sup>1</sup>
- :computer: Desktop
- :green_apple: iOS<sup>2</sup>

> [!NOTE]
> (1) Muze was tested in a local network environment not using HTTPS, thats why the next line is present in the app's Manifest:
> ```
> android:usesCleartextTraffic="true"
> ```
> Make sure you remove it if you're going to run your server using HTTPS.

<br/>

> [!IMPORTANT]
> (2) Muze is not yet tested in macOS or iOS environment because I don't have any Apple machines.
> Planning to be fixed soon.


# Technology stack
* Kotlin Multiplatform
* Compose Multiplatform (+ adaptive layouts)
* Coroutines
* Koin
* Ktor client
* Paging 3

# About kotlin multiplatform
This is a Kotlin Multiplatform project targeting Android, iOS, Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

# Special thanks
[Albert Chang](https://github.com/mxalbert1996) and [Tlaster](https://github.com/Tlaster)
for the [Zoomable composable component](https://github.com/mxalbert1996/Zoomable)

# License
```
MIT License
Copyright (c) 2024 Andrii Andrushchenko
Copyright (c) 2021 Tlaster
Copyright (c) 2022 Albert Chang

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
```
