/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system;

import org.lwjgl.*;
import org.lwjgl.system.jni.*;
import org.testng.annotations.*;

import static org.lwjgl.system.MemoryStack.*;
import static org.testng.Assert.*;

@Test
public class StructTest {

    public void testLayout() {
        new Struct(-1L, null) {
            {
                Layout layout;

                layout = __struct(
                    __member(1),
                    __member(2),
                    __member(4),
                    __member(8)
                );
                assertEquals(layout.getSize(), 16);
                assertEquals(layout.getAlignment(), 8);
                assertEquals(layout.offsetof(0), 0);
                assertEquals(layout.offsetof(1), 2);
                assertEquals(layout.offsetof(2), 4);
                assertEquals(layout.offsetof(3), 8);

                layout = __struct(
                    __member(8),
                    __member(4),
                    __member(2),
                    __member(1)
                );
                assertEquals(layout.getSize(), 16);
                assertEquals(layout.getAlignment(), 8);
                assertEquals(layout.offsetof(0), 0);
                assertEquals(layout.offsetof(1), 8);
                assertEquals(layout.offsetof(2), 12);
                assertEquals(layout.offsetof(3), 14);

                Layout aligned = __struct(
                    DEFAULT_PACK_ALIGNMENT, 32,
                    __member(4),
                    __member(4),
                    __member(4),
                    __member(4)
                );

                assertEquals(aligned.getSize(), 32);
                assertEquals(aligned.getAlignment(), 32);
                assertEquals(aligned.offsetof(0), 0);
                assertEquals(aligned.offsetof(1), 4);
                assertEquals(aligned.offsetof(2), 8);
                assertEquals(aligned.offsetof(3), 12);

                layout = __struct(
                    DEFAULT_PACK_ALIGNMENT, 8,
                    __member(4),
                    __member(4),
                    __member(4),
                    __member(4)
                );
                assertEquals(layout.getSize(), 16);
                assertEquals(layout.getAlignment(), 8);
                assertEquals(layout.offsetof(0), 0);
                assertEquals(layout.offsetof(1), 4);
                assertEquals(layout.offsetof(2), 8);
                assertEquals(layout.offsetof(3), 12);

                layout = __struct(
                    aligned,
                    __member(4)
                );
                assertEquals(layout.getSize(), 64);
                assertEquals(layout.getAlignment(), 32);
                assertEquals(layout.offsetof(0), 0);
                {
                    assertEquals(layout.offsetof(1), 0);
                    assertEquals(layout.offsetof(2), 4);
                    assertEquals(layout.offsetof(3), 8);
                    assertEquals(layout.offsetof(4), 12);
                }
                assertEquals(layout.offsetof(5), 32);

                layout = __struct(
                    __member(4),
                    aligned
                );
                assertEquals(layout.getSize(), 64);
                assertEquals(layout.getAlignment(), 32);
                assertEquals(layout.offsetof(0), 0);
                assertEquals(layout.offsetof(1), 32);
                {
                    assertEquals(layout.offsetof(2), 32);
                    assertEquals(layout.offsetof(3), 36);
                    assertEquals(layout.offsetof(4), 40);
                    assertEquals(layout.offsetof(5), 44);
                }

                Member[] members = {
                    __member(1),
                    __member(2),
                    __member(8),
                    __member(8, 32, true),
                    __member(1),
                    __member(8)
                };

                layout = __struct(1, DEFAULT_ALIGN_AS, members);
                assertEquals(layout.getSize(), 64);
                assertEquals(layout.getAlignment(), 32);
                assertEquals(layout.offsetof(0), 0);
                assertEquals(layout.offsetof(1), 1);
                assertEquals(layout.offsetof(2), 3);
                assertEquals(layout.offsetof(3), 32);
                assertEquals(layout.offsetof(4), 40);
                assertEquals(layout.offsetof(5), 41);

                layout = __struct(2, DEFAULT_ALIGN_AS, members);
                assertEquals(layout.getSize(), 64);
                assertEquals(layout.getAlignment(), 32);
                assertEquals(layout.offsetof(0), 0);
                assertEquals(layout.offsetof(1), 2);
                assertEquals(layout.offsetof(2), 4);
                assertEquals(layout.offsetof(3), 32);
                assertEquals(layout.offsetof(4), 40);
                assertEquals(layout.offsetof(5), 42);

                layout = __struct(4, DEFAULT_ALIGN_AS, members);
                assertEquals(layout.getSize(), 64);
                assertEquals(layout.getAlignment(), 32);
                assertEquals(layout.offsetof(0), 0);
                assertEquals(layout.offsetof(1), 2);
                assertEquals(layout.offsetof(2), 4);
                assertEquals(layout.offsetof(3), 32);
                assertEquals(layout.offsetof(4), 40);
                assertEquals(layout.offsetof(5), 44);

                layout = __struct(8, DEFAULT_ALIGN_AS, members);
                assertEquals(layout.getSize(), 64);
                assertEquals(layout.getAlignment(), 32);
                assertEquals(layout.offsetof(0), 0);
                assertEquals(layout.offsetof(1), 2);
                assertEquals(layout.offsetof(2), 8);
                assertEquals(layout.offsetof(3), 32);
                assertEquals(layout.offsetof(4), 40);
                assertEquals(layout.offsetof(5), 48);
            }

            @Override public int sizeof() {
                return 0;
            }
        };
    }

    public void testAllocation() {
        JNINativeMethod.create();
        JNINativeMethod.malloc().free();
        JNINativeMethod.calloc().free();

        try (MemoryStack stack = stackPush()) {
            JNINativeMethod.mallocStack(stack);
            JNINativeMethod.callocStack(stack);
        }

        expectThrows(IllegalArgumentException.class, () -> new JNINativeMethod(BufferUtils.createByteBuffer(4)));
    }

    public void testAllocationBuffer() {
        JNINativeMethod.create(10);
        JNINativeMethod.malloc(10).free();
        JNINativeMethod.calloc(10).free();

        try (MemoryStack stack = stackPush()) {
            JNINativeMethod.mallocStack(10, stack);
            JNINativeMethod.callocStack(10, stack);
        }

        assertEquals(new JNINativeMethod.Buffer(BufferUtils.createByteBuffer(4)).capacity(), 0);
    }

    public void testValidation() {
        try (MemoryStack stack = stackPush()) {
            JNINativeMethod s = JNINativeMethod.callocStack(stack);

            assertTrue(s.isNull(JNINativeMethod.NAME));
            expectThrows(NullPointerException.class, s::name);

            expectThrows(NullPointerException.class, () -> JNINativeMethod.validate(s.address()));
            s.name(stack.UTF8("myMethodName"));
            expectThrows(NullPointerException.class, () -> JNINativeMethod.validate(s.address()));
            s.signature(stack.UTF8("(I)I"));
            expectThrows(NullPointerException.class, () -> JNINativeMethod.validate(s.address()));
            s.fnPtr(0x12341234L);
            JNINativeMethod.validate(s.address());

            assertFalse(s.isNull(JNINativeMethod.NAME));
            assertEquals(s.nameString(), "myMethodName");

        }
    }

}
