/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.vulkan;

import javax.annotation.*;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.system.*;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;

/**
 * Structure describing the capabilities of a mode and plane combination.
 * 
 * <h5>Valid Usage (Implicit)</h5>
 * 
 * <ul>
 * <li>{@code sType} <b>must</b> be {@link KHRGetDisplayProperties2#VK_STRUCTURE_TYPE_DISPLAY_PLANE_CAPABILITIES_2_KHR STRUCTURE_TYPE_DISPLAY_PLANE_CAPABILITIES_2_KHR}</li>
 * <li>{@code pNext} <b>must</b> be {@code NULL}</li>
 * </ul>
 * 
 * <h5>See Also</h5>
 * 
 * <p>{@link VkDisplayPlaneCapabilitiesKHR}, {@link KHRGetDisplayProperties2#vkGetDisplayPlaneCapabilities2KHR GetDisplayPlaneCapabilities2KHR}</p>
 * 
 * <h3>Member documentation</h3>
 * 
 * <ul>
 * <li>{@code sType} &ndash; the type of this structure.</li>
 * <li>{@code pNext} &ndash; {@code NULL} or a pointer to an extension-specific structure.</li>
 * <li>{@code capabilities} &ndash; an instance of the {@link VkDisplayPlaneCapabilitiesKHR} structure.</li>
 * </ul>
 * 
 * <h3>Layout</h3>
 * 
 * <pre><code>
 * struct VkDisplayPlaneCapabilities2KHR {
 *     VkStructureType sType;
 *     void * pNext;
 *     {@link VkDisplayPlaneCapabilitiesKHR VkDisplayPlaneCapabilitiesKHR} capabilities;
 * }</code></pre>
 */
public class VkDisplayPlaneCapabilities2KHR extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        STYPE,
        PNEXT,
        CAPABILITIES;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(VkDisplayPlaneCapabilitiesKHR.SIZEOF, VkDisplayPlaneCapabilitiesKHR.ALIGNOF)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        STYPE = layout.offsetof(0);
        PNEXT = layout.offsetof(1);
        CAPABILITIES = layout.offsetof(2);
    }

    /**
     * Creates a {@code VkDisplayPlaneCapabilities2KHR} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public VkDisplayPlaneCapabilities2KHR(ByteBuffer container) {
        super(memAddress(container), __checkContainer(container, SIZEOF));
    }

    @Override
    public int sizeof() { return SIZEOF; }

    /** Returns the value of the {@code sType} field. */
    @NativeType("VkStructureType")
    public int sType() { return nsType(address()); }
    /** Returns the value of the {@code pNext} field. */
    @NativeType("void *")
    public long pNext() { return npNext(address()); }
    /** Returns a {@link VkDisplayPlaneCapabilitiesKHR} view of the {@code capabilities} field. */
    public VkDisplayPlaneCapabilitiesKHR capabilities() { return ncapabilities(address()); }

    /** Sets the specified value to the {@code sType} field. */
    public VkDisplayPlaneCapabilities2KHR sType(@NativeType("VkStructureType") int value) { nsType(address(), value); return this; }
    /** Sets the specified value to the {@code pNext} field. */
    public VkDisplayPlaneCapabilities2KHR pNext(@NativeType("void *") long value) { npNext(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public VkDisplayPlaneCapabilities2KHR set(
        int sType,
        long pNext
    ) {
        sType(sType);
        pNext(pNext);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public VkDisplayPlaneCapabilities2KHR set(VkDisplayPlaneCapabilities2KHR src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code VkDisplayPlaneCapabilities2KHR} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static VkDisplayPlaneCapabilities2KHR malloc() {
        return wrap(VkDisplayPlaneCapabilities2KHR.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code VkDisplayPlaneCapabilities2KHR} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static VkDisplayPlaneCapabilities2KHR calloc() {
        return wrap(VkDisplayPlaneCapabilities2KHR.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code VkDisplayPlaneCapabilities2KHR} instance allocated with {@link BufferUtils}. */
    public static VkDisplayPlaneCapabilities2KHR create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(VkDisplayPlaneCapabilities2KHR.class, memAddress(container), container);
    }

    /** Returns a new {@code VkDisplayPlaneCapabilities2KHR} instance for the specified memory address. */
    public static VkDisplayPlaneCapabilities2KHR create(long address) {
        return wrap(VkDisplayPlaneCapabilities2KHR.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static VkDisplayPlaneCapabilities2KHR createSafe(long address) {
        return address == NULL ? null : wrap(VkDisplayPlaneCapabilities2KHR.class, address);
    }

    /**
     * Returns a new {@link VkDisplayPlaneCapabilities2KHR.Buffer} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static VkDisplayPlaneCapabilities2KHR.Buffer malloc(int capacity) {
        return wrap(Buffer.class, nmemAllocChecked(__checkMalloc(capacity, SIZEOF)), capacity);
    }

    /**
     * Returns a new {@link VkDisplayPlaneCapabilities2KHR.Buffer} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static VkDisplayPlaneCapabilities2KHR.Buffer calloc(int capacity) {
        return wrap(Buffer.class, nmemCallocChecked(capacity, SIZEOF), capacity);
    }

    /**
     * Returns a new {@link VkDisplayPlaneCapabilities2KHR.Buffer} instance allocated with {@link BufferUtils}.
     *
     * @param capacity the buffer capacity
     */
    public static VkDisplayPlaneCapabilities2KHR.Buffer create(int capacity) {
        ByteBuffer container = __create(capacity, SIZEOF);
        return wrap(Buffer.class, memAddress(container), capacity, container);
    }

    /**
     * Create a {@link VkDisplayPlaneCapabilities2KHR.Buffer} instance at the specified memory.
     *
     * @param address  the memory address
     * @param capacity the buffer capacity
     */
    public static VkDisplayPlaneCapabilities2KHR.Buffer create(long address, int capacity) {
        return wrap(Buffer.class, address, capacity);
    }

    /** Like {@link #create(long, int) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static VkDisplayPlaneCapabilities2KHR.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }

    // -----------------------------------

    /** Returns a new {@code VkDisplayPlaneCapabilities2KHR} instance allocated on the thread-local {@link MemoryStack}. */
    public static VkDisplayPlaneCapabilities2KHR mallocStack() {
        return mallocStack(stackGet());
    }

    /** Returns a new {@code VkDisplayPlaneCapabilities2KHR} instance allocated on the thread-local {@link MemoryStack} and initializes all its bits to zero. */
    public static VkDisplayPlaneCapabilities2KHR callocStack() {
        return callocStack(stackGet());
    }

    /**
     * Returns a new {@code VkDisplayPlaneCapabilities2KHR} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static VkDisplayPlaneCapabilities2KHR mallocStack(MemoryStack stack) {
        return wrap(VkDisplayPlaneCapabilities2KHR.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code VkDisplayPlaneCapabilities2KHR} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static VkDisplayPlaneCapabilities2KHR callocStack(MemoryStack stack) {
        return wrap(VkDisplayPlaneCapabilities2KHR.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
    }

    /**
     * Returns a new {@link VkDisplayPlaneCapabilities2KHR.Buffer} instance allocated on the thread-local {@link MemoryStack}.
     *
     * @param capacity the buffer capacity
     */
    public static VkDisplayPlaneCapabilities2KHR.Buffer mallocStack(int capacity) {
        return mallocStack(capacity, stackGet());
    }

    /**
     * Returns a new {@link VkDisplayPlaneCapabilities2KHR.Buffer} instance allocated on the thread-local {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param capacity the buffer capacity
     */
    public static VkDisplayPlaneCapabilities2KHR.Buffer callocStack(int capacity) {
        return callocStack(capacity, stackGet());
    }

    /**
     * Returns a new {@link VkDisplayPlaneCapabilities2KHR.Buffer} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static VkDisplayPlaneCapabilities2KHR.Buffer mallocStack(int capacity, MemoryStack stack) {
        return wrap(Buffer.class, stack.nmalloc(ALIGNOF, capacity * SIZEOF), capacity);
    }

    /**
     * Returns a new {@link VkDisplayPlaneCapabilities2KHR.Buffer} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static VkDisplayPlaneCapabilities2KHR.Buffer callocStack(int capacity, MemoryStack stack) {
        return wrap(Buffer.class, stack.ncalloc(ALIGNOF, capacity, SIZEOF), capacity);
    }

    // -----------------------------------

    /** Unsafe version of {@link #sType}. */
    public static int nsType(long struct) { return UNSAFE.getInt(null, struct + VkDisplayPlaneCapabilities2KHR.STYPE); }
    /** Unsafe version of {@link #pNext}. */
    public static long npNext(long struct) { return memGetAddress(struct + VkDisplayPlaneCapabilities2KHR.PNEXT); }
    /** Unsafe version of {@link #capabilities}. */
    public static VkDisplayPlaneCapabilitiesKHR ncapabilities(long struct) { return VkDisplayPlaneCapabilitiesKHR.create(struct + VkDisplayPlaneCapabilities2KHR.CAPABILITIES); }

    /** Unsafe version of {@link #sType(int) sType}. */
    public static void nsType(long struct, int value) { UNSAFE.putInt(null, struct + VkDisplayPlaneCapabilities2KHR.STYPE, value); }
    /** Unsafe version of {@link #pNext(long) pNext}. */
    public static void npNext(long struct, long value) { memPutAddress(struct + VkDisplayPlaneCapabilities2KHR.PNEXT, value); }

    // -----------------------------------

    /** An array of {@link VkDisplayPlaneCapabilities2KHR} structs. */
    public static class Buffer extends StructBuffer<VkDisplayPlaneCapabilities2KHR, Buffer> implements NativeResource {

        private static final VkDisplayPlaneCapabilities2KHR ELEMENT_FACTORY = VkDisplayPlaneCapabilities2KHR.create(-1L);

        /**
         * Creates a new {@code VkDisplayPlaneCapabilities2KHR.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link VkDisplayPlaneCapabilities2KHR#SIZEOF}, and its mark will be undefined.
         *
         * <p>The created buffer instance holds a strong reference to the container object.</p>
         */
        public Buffer(ByteBuffer container) {
            super(container, container.remaining() / SIZEOF);
        }

        public Buffer(long address, int cap) {
            super(address, null, -1, 0, cap, cap);
        }

        Buffer(long address, @Nullable ByteBuffer container, int mark, int pos, int lim, int cap) {
            super(address, container, mark, pos, lim, cap);
        }

        @Override
        protected Buffer self() {
            return this;
        }

        @Override
        protected VkDisplayPlaneCapabilities2KHR getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** Returns the value of the {@code sType} field. */
        @NativeType("VkStructureType")
        public int sType() { return VkDisplayPlaneCapabilities2KHR.nsType(address()); }
        /** Returns the value of the {@code pNext} field. */
        @NativeType("void *")
        public long pNext() { return VkDisplayPlaneCapabilities2KHR.npNext(address()); }
        /** Returns a {@link VkDisplayPlaneCapabilitiesKHR} view of the {@code capabilities} field. */
        public VkDisplayPlaneCapabilitiesKHR capabilities() { return VkDisplayPlaneCapabilities2KHR.ncapabilities(address()); }

        /** Sets the specified value to the {@code sType} field. */
        public VkDisplayPlaneCapabilities2KHR.Buffer sType(@NativeType("VkStructureType") int value) { VkDisplayPlaneCapabilities2KHR.nsType(address(), value); return this; }
        /** Sets the specified value to the {@code pNext} field. */
        public VkDisplayPlaneCapabilities2KHR.Buffer pNext(@NativeType("void *") long value) { VkDisplayPlaneCapabilities2KHR.npNext(address(), value); return this; }

    }

}