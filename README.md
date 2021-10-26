# Scooter Hunter

The purpose of the application is to showcase modern Android development the MVVM pattern, Hilt, Retrofit, LiveData, ViewModel, Coroutines, Room, Navigation Components, Data Binding and some other libraries from the [Modern Android Development](https://developer.android.com/modern-android-development) blueprint.

## Features

The app displays electric scooter vehicles on the map.

* Google Maps based display of the vehicles.
* The markers of the vehicles are clustered in a far view.
* Tapping on a marker shows the details of the vehicle.
* Linear progress bar is indicating the loading state at the top of the screen.
* Error handling with retry option.
* The location permission is handled in a user friendly way.
* The app is distributed via Firebase App Distribution. You can join to the testers via [this invite link](https://appdistribution.firebase.dev/i/76fbc5a9a37db93b)

## Technologies used:

* [Ktor](https://ktor.io/) Lightweight networking library built from the ground up using Kotlin and Coroutines. You get to use a concise, multiplatform language, as well as the power of asynchronous programming with an intuitive imperative flow.
* [Hilt](https://dagger.dev/hilt/) for dependency injection.
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) to store and manage UI-related data in a lifecycle conscious way.
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) to handle data in a lifecycle-aware fashion.
* [Navigation Component](https://developer.android.com/guide/navigation) a single-activity architecture to handle all navigation and also passing of data between destinations with [SafeArgs](https://developer.android.com/guide/navigation/navigation-pass-data) plugin.
* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) used to manage the local storage i.e. `writing to and reading from the database`. Coroutines help in managing background threads and reduces the need for callbacks.
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/) to bind UI components in layouts to data sources.
* [Room](https://developer.android.com/topic/libraries/architecture/room) persistence library which provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
* [Android KTX](https://developer.android.com/kotlin/ktx) provides concise, idiomatic Kotlin to Jetpack, Android platform, and other APIs.
* [Firebase Analytics](https://firebase.google.com/products/analytics) Analytics of the screens usage across the users.
* [Firebase Crashlytics](https://firebase.google.com/products/crashlytics) Crash reports.
* [Firebase App Distribution](https://firebase.google.com/docs/app-distribution) The built apps are distributed for internal testing. See The invite link above.
* [GitHub Actions](https://github.com/features/actionss) The aps are built here.

## Build instructions

The project uses 2 values from `local.properties`:

* The Google Maps APi needs API key to work. It is stored in the value of the `MAPS_API_KEY` parameter
* The network call needs a key in the request header. It is stored in the value of the `SECRET_KEY`

The content of the `local.properties` file should look like this:
```
MAPS_API_KEY=XXXX
SECRET_KEY="YYYY"
```

## LICENSE

MIT License

Copyright (c) 2021 János Kernács

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
