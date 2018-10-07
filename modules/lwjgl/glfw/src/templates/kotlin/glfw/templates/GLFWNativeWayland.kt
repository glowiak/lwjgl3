/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package glfw.templates

import org.lwjgl.generator.*
import glfw.*
import core.freebsd.*

val GLFWNativeWayland = "GLFWNativeWayland".nativeClass(Module.GLFW, nativeSubPath = "freebsd", prefix = "GLFW", binding = GLFW_BINDING_DELEGATE) {
    documentation = "Native bindings to the GLFW library's Wayland native access functions."

    wl_display.p(
        "GetWaylandDisplay",
        """
        Returns the {@code struct wl_display*} used by GLFW.

        This function may be called from any thread. Access is not synchronized.
        """,
        returnDoc = "the {@code struct wl_display*} used by GLFW, or #NULL if an error occurred.",
        since = "version 3.2"
    )

    wl_output.p(
        "GetWaylandMonitor",
        """
        Returns the {@code struct wl_output*} of the specified monitor.

        This function may be called from any thread. Access is not synchronized.
        """,

        GLFWmonitor.p.IN("monitor", ""),
        returnDoc = "the {@code struct wl_output*} of the specified monitor, or #NULL if an error occurred.",
        since = "version 3.2"
    )

    wl_surface.p(
        "GetWaylandWindow",
        """
        Returns the main {@code struct wl_surface*} of the specified window.

        This function may be called from any thread. Access is not synchronized.
        """,

        GLFWwindow.p.IN("window", ""),
        returnDoc = "the main {@code struct wl_surface*} of the specified window, or #NULL if an error occurred.",
        since = "version 3.2"
    )
}