# Microsoft Fluent Design System Key Principles

*   **Platform**: Windows 11, cross-platform apps (via Fluent UI libraries).
*   **Core Metaphors**: Light, Depth, Motion, Material, Scale.

## Key Elements

1.  **Light**: Draws attention to information and illuminates actions (e.g., Reveal Highlight on hover).
2.  **Depth**: Layers of information. Uses Z-axis, shadows, and layering to create a visual hierarchy.
3.  **Motion**: Connects experiences, making transitions feel natural and continuous.
4.  **Material**: Textures that simulate real-world materials (e.g., Acrylic, Mica).
5.  **Scale**: Adapts to different screen sizes and input methods (mouse, touch, pen).

## Materials (Textures)
*   **Mica**: An opaque, dynamic material that incorporates the user's desktop theme and wallpaper to paint the background of long-lived windows. It improves performance compared to Acrylic.
*   **Acrylic**: A translucent material that creates a frosted glass effect, useful for transient surfaces like context menus or flyouts.

## Typography
*   System Font: Segoe UI Variable. It scales dynamically, improving legibility at small sizes and looking elegant at display sizes.

## Geometry & Layout
*   **Rounded Corners**: Windows 11 uses rounded corners on top-level windows, flyouts, and controls to soften the UI.
*   **Spacing**: Uses a 4px/8px grid system for consistent alignment and rhythm.

## Key Components
*   **Navigation View**: Typically a left-aligned pane (hamburger menu) for app-level navigation.
*   **Command Bar**: Top area for actions and tools.
*   **Context Menus**: Right-click menus that use Acrylic material and rounded corners.
