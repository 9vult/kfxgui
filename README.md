<!-- omit in toc -->
# KFX-GUI
<!-- omit in toc -->
#### 9volt GUI Karaoke Template Builder

KFX-GUI is a highly extensible graphical karaoke template builder for Advanced Substation Alpha (ASS) subtitles. KFX-GUI does not execute karaoke templates, it only provides a graphical building tool for creating them with automation via plugins.

<!-- omit in toc -->
## Contents

- [Plugins](#plugins)
  - [Effect Component Plugins](#effect-component-plugins)
    - [Creating a Component plugin](#creating-a-component-plugin)
    - [Component Plugin Format](#component-plugin-format)
  - [Templater Line Definition Plugins](#templater-line-definition-plugins)
    - [Creating a Line Definition Plugin](#creating-a-line-definition-plugin)
    - [Line Definition Plugin Format](#line-definition-plugin-format)
      - [Code line definitions](#code-line-definitions)
- [Dependencies](#dependencies)

# Plugins

KFX-GUI uses plugins in a couple different ways. Currently, there are two different plugin types - effect components and templater line definitions.
Plugins are used to provide extensibility and both backward and forward compatibility with existing and future templaters and ASS versions.

## Effect Component Plugins

The most basic component plugins represent a single ASS tag, such as `\pos` for setting the position or `\border` to add an outline. However, the beauty of a plugin system is being able to define pretty much whatever you want, so long as it fits within a few constraints.

Plugins are written in the TOML format, and are automatically loaded from the plugins folder on startup. They should follow a filename convention of `pluginname.toml`.

### Creating a Component plugin

The following example defines a plugin that creates a basic grow-and-rotate syl effect:

```toml
name = "Grow-And-Rotate"
author = "9volt"
description = "Expand syl size and rotate it, then return to normal over the syl's duration"
transform = false
params = [
    "Scale", "Rotation"
]
format = "?t($start, $mid, ?fscx${Scale}?fscy${Scale}?frz${Rotation})?t($mid, $end, ?fscx100?fscy100?frz0)"
```

### Component Plugin Format

The component plugin format consists of 3 sections.

1. Plugin identification
   - The first 4 lines of the file describe the `name`, `author`, and short `description` of the plugin. The description is used as a tooltip in the toolbox panel.
   - The 5th line, `transform`, defines if the plugin may be used within Transform events. For most custom plugins, this is likely to be false, as a transform tag will be provided by the plugin itself.

2. Parameters
    - Here we define a list of parameters the user will supply. Each item in the list will be linked to a text field labeled with the parameter name.
    - In the example, the user will be asked to supply the `Scale` and `Rotation` values.

3. Format String
   - The format string defines the output of the plugin.
   - The backslash `\` should be replaced with a question mark `?` in the format string to avoid any potential issues with escape characters. Question marks in the format string will automatically be replaced with backslashes with the finalized ASS code is generated.
   - Variables follow a format similar to f-strings in Python, `${VariableName}`, and are case-sensitive.

<!-- omit in toc -->
#### Result

Assuming a user suppliad values of `120` for Scale and `15` for Rotation, the following ASS code would be generated when this plugin is executed:

```
\t($start, $mid, \fscx120\fscy120\frz15)\t($mid, $end, \fscx100\fscy100\frz0)
```

## Templater Line Definition Plugins

The second type of plugin used by KFX-GUI defines the line types used by a templater. This allows KFX-GUI to support an unlimited number of templaters, both present and future, since one can easily write a plugin to support their favorite templater.

Line definition plugins must use the `*.linetype.toml` file extension.

### Creating a Line Definition Plugin

Below is an excerpt from the stock plugin providing support for The0x539's karaoke templater:

```toml
target = "The0x539"
components = [
    "code once",
    "code line",
    "template line",
    "template syl",
    "mixin word",
    "mixin char"
]
```

### Line Definition Plugin Format

There are two parts to line definition plugins:

1. `target` - The target is the name of the templater this plugin supports.
2. `components` - A list of strings defining the line types used by the templater.

#### Code line definitions

Code lines are a special part of any karaoke template, and KFX-GUI has some special features for code lines. This section applies to any line with a line type containing the word `code`.

- A code entry box is displayed on the line settings page
- Components will not be parsed on a code line during ASS generation
  - Similarly, code will not be parsed on non-code lines


# Dependencies

KFX-GUI is a Maven project, and specifics on dependency and plugin usage can be found in the `pom.xml` file in the root directory of this project. TL;DR is that KFX-GUI depends on:
- Java 11
- JavaFX 11
- Night-Config Core, TOML
- Gson
