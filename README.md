# Detekt Decompose Ruleset
[![Build Action](https://github.com/LionZXY/detekt-decompose-rule/actions/workflows/push.yml/badge.svg)](https://github.com/LionZXY/detekt-decompose-rule/actions/workflows/push.yml) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/uk.kulikov.detekt.decompose/decompose-detekt-rules/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.kulikov.detekt.decompose/decompose-detekt-rules) [![Apache License](http://img.shields.io/badge/license-Apache%20License%202.0-lightgrey.svg)](http://choosealicense.com/licenses/apache-2.0/)

[Decompose](https://github.com/arkivanov/Decompose) is the best and safest navigation framework I know. However, some fundamental and architectural decisions may not be obvious to newcomers to the project.
In order to avoid typical mistakes when using Decompose and this Detekt plugin was created

## How to use it

Connect the detekt plugin the same way you do with the others:

```kotlin
detektPlugins("uk.kulikov.detekt.decompose:decompose-detekt-rules:1.0.1")
```

## Rules

### PushForbiddenRule

Push is a very dangerous operation in Decompose - if the config matches, the whole application will crash. This rule was created to avoid that.

The config for this rule looks like this:
```yaml
DecomposeRule:
  PushForbiddenRule:
    active: true
    checkImport: true
    replaceTo: 'pushToFront()'
```

1) `checkImport` is needed to track a rule without enabling [Detekt Type Resolution](https://detekt.dev/docs/gettingstarted/type-resolution). If you have TypeResolution enabled, you don't need this flag and can turn it off. 
2) `replaceTo` - is the default way of assigning a replacement. The rules of what `push()` should be replaced with differ from project to project. 
   - The default is `replaceTo` is `null` and gives this error:
    ```plaintext
    The push() method can cause crashes in runtime. Use safer ways to add a screen to the stack. More information: https://arkivanov.github.io/Decompose/navigation/stack/navigation/#stacknavigator-extension-functions
    ```
   - If `replaceTo` is not empty (for example `pushToFront()`), the output is:
    ```plaintext
    Use pushToFront() instead of push() to avoid runtime crashes
    ```
