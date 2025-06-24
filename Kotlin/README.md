# Fct25 Android #

# TODO: Your project information.

## Architecture ##

The project uses a Modern App Architecture which is organized in these layers:

- **App**: "UI layer" (or presentation layer) which displays the application data on the screen. Contains the UI elements that render the data on the screen, state holders (such as ViewModel classes) that hold data, expose it to the UI, and handle logic.
	- The UI Layer uses MVVM and Jetpack Compose.

- **Data**: The data layer is made of repositories that each can contain zero to many data sources. Also contains the API description and model.

- **Domain**: it is an optional layer that sits between the UI and data layers. The domain layer is responsible for encapsulating complex business logic, or simple business logic that is reused by multiple ViewModels.

## Naming Conventions for Models

In this project, we use the following conventions to name data models across the different architecture layers:

### Data Layer
- **API Models**  
  These models represent the data as it is received from an external API.
	- **Suffix**: `Data`
	- **Example**: `UserData`, `ProductData`

- **Database Models**  
  Models used to represent entities in the local database (e.g. Room).
	- **Suffix**: `DB`
	- **Example**: `UserDB`, `ProductDB`

- **Remote Config Models**
  Models used to represent entities in the Firebase Remote Config.
	- **Suffix**: `RemoteConfig`
	- **Example**: `UserRemoteConfig`, `ProductRemoteConfig`

- **Local Models**  
  Models used for data that are locally stored (e.g. DataStore).
	- **Suffix**: `Local`
	- **Example**: `UserLocal`, `ProductLocal`

### Domain Layer
- **Domain Models**
  These models represent business objects and are used within the application logic.
	- **No suffix will be applied**.
	- **Example**: `User`, `Product`

### UI Layer
- **UI Models**  
  These models are used to represent data specifically in the user interface layer. They are optional and will be used when other models require transformation to simplify their use or representation in the UI.
	- **Suffix**: `UiModel`
	- **Example**: `UserUiModel`, `ProductUiModel`

- **View State Models**  
  State models that are exposed from the `ViewModel` to the user interface. These models represent the state of a specific view at any given time.
	- **Suffix**: `UiState`
	- **Example**: `UserUiState`, `ProductUiState`

## Requirements ##

- At [libs.versions.toml](./gradle/libs.versions.toml) you can check:
	- Minimum supported Android SDK (minSdk)
	- Target Android SDK (targetSdk)
	- Android Gradle Plugin version (androidGradlePlugin)
	- At https://developer.android.com/studio/releases it is possible to check which Android Studio version is needed for the Android Gradle Plugin version used.
- Kotlin.

## Localizables ##

Document with localized texts: [Localizables](https://docs.google.com/spreadsheets/d/1anjeqHxbcibfwZP42BBmJAP-TdtWt8x9zsM78mXBrCU/edit?gid=0#gid=0)

To execute Localio use these commands in the project's root directory:

First time:

```
bundle install
```

Next times:
```
bundle exec localize
```

This command will call Localio in order to generate all texts resource files.

It needs Mobivery Localio package installed, which can be found in [repository](https://bitbucket.org/mobivery-public/localio).

## Feature Flags ##

This project includes **feature flags** as part of its architecture. A *feature flag* is a mechanism that allows you to control access to certain features of the application. Depending on its configuration, a specific functionality can be enabled or disabled at runtime. This allows us to:

- **Control new features** without having to release new versions of the app.
- **Quickly disable** a feature if an error or problem is detected.

### Application Update

In addition to the functionality control, the **feature flags** system also incorporates a mechanism to check if the application should be updated. Using another parameter in the feature flags, it is managed when the application should check if an update is available in the store, and if it should be mandatory or not. If it is mandatory, it forces the user to install it, and if it is not installed, it does not allow the application to start. If it is optional, it only recommends the user to update, but if the user does not want to do it at that moment, they can continue using the application. This system is complemented by [In-app updates](https://developer.android.com/guide/playcore/in-app-updates), which allows the update to be carried out in an integrated way in the application if a new version is detected.

### Implementation in the project

Feature flags are implemented using Firebase Remote Config, storing the configuration in a JSON, so it is very easy to manage them by editing the corresponding parameter in Firebase. Below is an example of how feature flags are represented in Remote Config:

```json
 { 
    "update": {
    	"isActive": true,
    	"force": false,
    	"fromVersion": "1.0.0",
    	"toVersion": "1.1.0"
  	},
    "features": 
    [     
      {       
        "name": "feature_x",       
        "isActive": false,       
        "fromVersion": "1.0.0",       
        "toVersion": "1.1.0"     
      }   
    ]
  }
```

#### Description of feature flags fields

- **name**: This is the unique identifier for the _feature flag_, used to reference a specific feature in the code.

- **isActive**: Indicates the current state of the feature. If set to `true`, the feature is active; if set to `false`, it remains disabled.

- **fromVersion** and **toVersion**: These fields allow you to specify a range of versions, including both, in which the feature will be available. This is useful for features that should be active only in specific versions of the application.

#### Description of update fields

- **isActive**: Indicates whether update checking is enabled. If set to `true`, the app will check for available updates on the Play Store.

- **force**: Defines whether the update is mandatory. If set to `true`, the user will not be able to continue using the app until it is updated. This can be used to force critical updates that resolve serious security or functionality issues.

- **fromVersion** and **toVersion**: Specifies the range of versions, including both, in which update checking should be applied. This allows update checking to be enabled only for specific versions.

### Example of use in the code

For usage examples, see the `RemoteConfigDataSource` and `FeatureFlagsRepository` classes. The logic for checking feature flags, as well as handling updates, is implemented in these classes.
These classes provide a foundation for understanding how this system fits into the flow of your application.

## Templates ##

In the root of the repository there is a folder called `templates`. There templates are useful for creating new classes with a predefined structure. To install them, run the script `install_latest_templates.sh` in that folder.
You can then use them by selecting the template in **the 'New' dialog**.

## Compose Preview Screenshot Testing 

https://developer.android.com/studio/preview/compose-screenshot-testing

A screenshot test is an automated test that takes a screenshot of a piece of UI and then compares it against a previously approved reference image. If the images don't match, the test fails and produces an HTML report to help you compare and find the differences.

### Generate reference images

These reference images are used to identify changes later, after you make code changes.

```
./gradlew {:module:}update{Variant}ScreenshotTest
```

For example:

```
./gradlew app:feature:login:update<flavor>PreDebugScreenshotTest
```

After the task completes, find the reference images in `app/feature/login/src/<flavor>PreDebug/screenshotTest/reference/.../LoginScreenScreenshots`

### Generate a test report

Once the reference images exist, run the validate task to take a new screenshot and compare it with the reference image:

```
./gradlew {:module:}validate{Variant}ScreenshotTest
```

For example:

```
./gradlew app:feature:login:validate<flavor>PreDebugScreenshotTest
```

The verification task creates an HTML report at `app/feature/login/build/reports/screenshotTest/preview/.../index.html`

## App Theme

### **Types of Colors**:
#### **1. Default Colors**:
- Colors defined in the Material Design 3 color scheme (e.g., `primary`, `secondary`, etc.).
#### **2. Custom Colors**:
- Additional colors not included in the default scheme but necessary for app customization (e.g., `primaryVariant`, `warning`, etc.).

### **Adding a Color to the Theme**

#### **1. Add a Default Color**
- For Material Design 3 colors, update `LightColorScheme` and `DarkColorScheme` with the new color.

#### **2. Add a Custom Color**
- Add the color to the `CustomColorScheme` class.
- Include the new color in `CustomLightColorScheme` and `CustomDarkColorScheme`.

#### **3. Provide the Color in the Theme**
- Update `MaterialColors` in the `AppTheme` function to include the new color.

#### **4. Access the Color**
- All colors (default and custom) will be globally accessible via `AppColors`.  

---

### **Typographies**

#### **Modifying a Typography**
The `AppTypography` object should be overridden to customize Material Design's default text styles.

#### **Adding a Custom Typography**
Adding extra typographies is uncommon but, if needed, it should follow an approach similar to the solution for colors.

#### **Accessing the Typography**
All typographies will be globally accessible via `AppTypographies`.
