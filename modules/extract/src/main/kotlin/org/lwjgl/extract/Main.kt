/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.extract

import org.lwjgl.system.*
import java.awt.*
import java.awt.GridBagConstraints.*
import java.awt.event.*
import java.awt.event.MouseEvent.*
import java.io.*
import java.nio.file.*
import java.util.*
import javax.imageio.*
import javax.swing.*
import javax.swing.JFileChooser.*
import javax.swing.ScrollPaneConstants.*
import javax.swing.plaf.basic.*

fun main(args: Array<String>) {
    if (Configuration.LLVM_CLANG_LIBRARY_NAME.get() == null) {
        throw IllegalStateException("Please configure the Clang shared library path with -Dorg.lwjgl.llvm.clang.libname=<path>");
    }

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    System.setProperty("line.separator", "\n")

    val app = Application()
    app.frame.isVisible = true
}

private fun checkModal() = Window.getWindows().none { it is Dialog && it.isModal && it.isVisible }

class Application {
    val frame = JFrame("LWJGL Template Extraction Tool [BETA]")

    // Settings

    private val includePaths = DefaultListModel<String>()
    private val includePathsList = JList(includePaths)

    private val header = JTextField()

    private var lastIncludePath: File? = null
    private var lastHeader: File? = null

    private val module = JTextField()
    private val prefixConstant = JTextField()
    private val prefixMethod = JTextField()

    private val mainFileOnly = JCheckBox("Main file only", true)
    private val ignoreSystemHeaders = JCheckBox("Ignore system headers", true)

    private val compilerArgs = JTextField()

    private val ignoreErrors = JCheckBox("Ignore errors", false)
    private val parseAllComments = JCheckBox("Parse all comments", true)

    private val renderTypes = JCheckBox("Render types", true)
    private val renderStructs = JCheckBox("Render structs", true)
    private val renderConstants = JCheckBox("Render constants", true)
    private val renderFunctions = JCheckBox("Render functions", true)

    private val extract = JButton("Extract")

    // Output

    private val source = JTextArea(256 * 3, 64)
    private val console = JTextArea(256, 64)

    init {
        /*
        val preset = Paths.get("modules", "lwjgl", "llvm", "src", "main", "c")
            .let { includePath ->
                Header(
                    "LLVM",
                    "CX",
                    "clang_",
                    listOf(includePath),
                    includePath.resolve(Paths.get("clang-c", "Index.h"))
                )
            }

        load(
            preset, Options(
                listOf("-std=c11"),
                mainFileOnly = true,
                ignoreSystemHeaders = false,
                ignoreErrors = false,
                parseAllComments = true,
                parseTypes = true,
                parseStructs = true,
                parseConstants = true,
                parseFunctions = true
            )
        )
        */

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher { e ->
                if (e.id == KeyEvent.KEY_PRESSED) {
                    when (e.keyCode) {
                        KeyEvent.VK_ESCAPE -> {
                            if (checkModal()) {
                                frame.dispose()
                                return@addKeyEventDispatcher true
                            }
                        }
                        KeyEvent.VK_O      -> {
                            if (e.isControlDown && checkModal()) {
                                header.dispatchEvent(MouseEvent(header, MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false, BUTTON1))
                                return@addKeyEventDispatcher true
                            }
                        }
                        KeyEvent.VK_ENTER  -> {
                            if (e.isControlDown && checkModal()) {
                                extract.doClick()
                                return@addKeyEventDispatcher true
                            }
                        }
                    }
                }
                false
            }

        // Settings

        if (!includePaths.isEmpty) {
            lastIncludePath = File(includePaths[includePaths.size - 1])
        }
        if (header.text.isNotEmpty()) {
            lastHeader = File(header.text)
            extract.isEnabled = true
        } else {
            extract.isEnabled = false
        }

        val add = JButton("+")
        val remove = JButton("-")
        val clear = JButton("\u2205")

        add.toolTipText = "Press to add directories to the include path list."
        add.addActionListener {
            val chooser = JFileChooser()
            chooser.dialogTitle = "Add include path"
            chooser.approveButtonText = "Add"
            chooser.approveButtonToolTipText = "Add include path"
            chooser.fileSelectionMode = DIRECTORIES_ONLY
            chooser.isMultiSelectionEnabled = true
            chooser.currentDirectory = lastIncludePath?.parentFile ?: lastIncludePath ?: File("./")
            if (chooser.showOpenDialog(header) == APPROVE_OPTION) {
                val path = chooser.selectedFile.let { directory ->
                    lastIncludePath = directory
                    directory.path
                }
                println(path)
                if (!includePaths.contains(path)) {
                    includePaths.addElement(path)
                    remove.isEnabled = true
                    clear.isEnabled = true
                }
            }
        }

        remove.toolTipText = "Press to remove directories from the include path list."
        remove.addActionListener {
            val selected = includePathsList.selectedIndices
            if (selected.isEmpty()) {
                if (!includePaths.isEmpty) {
                    includePaths.remove(includePaths.size - 1)
                }
            } else {
                for (i in selected.lastIndex downTo 0) {
                    includePaths.remove(selected[i])
                }
            }
            if (includePaths.isEmpty) {
                remove.isEnabled = false
                clear.isEnabled = false
            }
        }

        clear.toolTipText = "Press to clear the include path list."
        clear.addActionListener {
            includePaths.clear()
            remove.isEnabled = false
            clear.isEnabled = false
        }

        header.isEditable = false
        header.toolTipText = "Click to select a header file."
        header.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val chooser = JFileChooser(File(header.text).parent)
                chooser.addChoosableFileFilter(object : javax.swing.filechooser.FileFilter() {
                    override fun accept(f: File): Boolean {
                        return f.isDirectory || f.extension == "h"
                    }

                    override fun getDescription(): String {
                        return "C/C++ header"
                    }
                })
                chooser.fileFilter = chooser.choosableFileFilters[1]
                chooser.dialogTitle = "Select header to parse"
                chooser.approveButtonText = "Select"
                chooser.approveButtonToolTipText = "Parse selected header file"
                chooser.currentDirectory = lastHeader?.parentFile ?: File("./")
                if (chooser.showOpenDialog(header) == APPROVE_OPTION) {
                    header.text = chooser.selectedFile.let { file ->
                        lastHeader = file
                        file.path
                    }
                    extract.isEnabled = true
                }
            }
        })
        ignoreSystemHeaders.isEnabled = !mainFileOnly.isSelected

        mainFileOnly.toolTipText = "Ignore declarations from included files."
        mainFileOnly.addActionListener {
            ignoreSystemHeaders.isEnabled = !mainFileOnly.isSelected
        }

        ignoreSystemHeaders.toolTipText = "Ignore declarations from files that are not under one of the include paths."
        ignoreErrors.toolTipText = "Extract declarations even if there are compilation errors."

        extract.toolTipText = "Press to extract LWJGL template definitions from the selected header. [CTRL+Enter]"
        extract.addActionListener {
            extract()
        }

        val settings = JPanel(GridBagLayout())
        settings.border = BorderFactory.createEmptyBorder(4, 4, 4, 4)

        val insets = Insets(2, 2, 2, 2)

        var l = JLabel("Include paths")
        l.labelFor = includePathsList
        settings.add(l, GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, NORTHEAST, NONE, insets, 0, 0))
        settings.add(
            JScrollPane(includePathsList, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_AS_NEEDED),
            GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, NORTHWEST, BOTH, insets, 0, 0)
        )

        val includeActionsPane = JPanel(GridBagLayout())

        includeActionsPane.add(add, GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, WEST, NONE, insets, 0, 0))
        includeActionsPane.add(remove, GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, WEST, NONE, insets, 0, 0))
        includeActionsPane.add(clear, GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, WEST, NONE, insets, 0, 0))

        settings.add(includeActionsPane, GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, EAST, NONE, insets, 0, 0))

        l = JLabel("Header")
        l.labelFor = header
        settings.add(l, GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, EAST, NONE, insets, 0, 0))
        settings.add(header, GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, insets, 0, 0))

        l = JLabel("Compiler args")
        l.labelFor = compilerArgs
        settings.add(l, GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, EAST, NONE, insets, 0, 0))
        settings.add(compilerArgs, GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, insets, 0, 0))

        l = JLabel("LWJGL Module")
        l.labelFor = module
        settings.add(l, GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, EAST, NONE, insets, 0, 0))
        settings.add(module, GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, insets, 0, 0))

        l = JLabel("Constant prefix")
        l.labelFor = prefixConstant
        settings.add(l, GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, EAST, NONE, insets, 0, 0))
        settings.add(prefixConstant, GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, insets, 0, 0))

        l = JLabel("Method prefix")
        l.labelFor = prefixMethod
        settings.add(l, GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, EAST, NONE, insets, 0, 0))
        settings.add(prefixMethod, GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, insets, 0, 0))

        val flagsPane = JPanel(GridBagLayout())

        flagsPane.add(mainFileOnly, GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, WEST, NONE, insets, 0, 0))
        flagsPane.add(ignoreSystemHeaders, GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, WEST, NONE, insets, 0, 0))
        flagsPane.add(ignoreErrors, GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, WEST, NONE, insets, 0, 0))
        flagsPane.add(parseAllComments, GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, WEST, NONE, insets, 0, 0))
        parseAllComments.toolTipText = "Parse comments that are not in Doxygen format."

        flagsPane.add(renderTypes, GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, WEST, NONE, insets, 0, 0))
        flagsPane.add(renderStructs, GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, WEST, NONE, insets, 0, 0))
        flagsPane.add(renderConstants, GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, WEST, NONE, insets, 0, 0))
        flagsPane.add(renderFunctions, GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, WEST, NONE, insets, 0, 0))

        settings.add(flagsPane, GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, EAST, HORIZONTAL, insets, 0, 0))

        settings.add(extract, GridBagConstraints(1, 8, 1, 1, 1.0, 0.0, EAST, NONE, Insets(4, 4, 4, 16), 0, 0))

        settings.add(JPanel(), GridBagConstraints(1, 9, 4, 1, 1.0, 1.0, CENTER, BOTH, insets, 0, 0))

        // Output

        val font = if (
            GraphicsEnvironment.getLocalGraphicsEnvironment().availableFontFamilyNames.any {
                it == "Consolas"
            }
        )
            Font("Consolas", Font.PLAIN, 13)
        else
            Font(Font.MONOSPACED, Font.PLAIN, 12)

        fun JTextArea.setup(foreground: Color) {
            this.font = font
            this.lineWrap = false
            this.tabSize = 4
            this.foreground = foreground
            this.background = Color(0x2B2B2B)
            this.caretColor = foreground
            this.border = BorderFactory.createEmptyBorder(4, 4, 4, 4)
        }

        source.setup(Color(0xA9B7C6))
        console.setup(Color(0xFF6B68))
        console.isEditable = false;

        fun JSplitPane.removeBorders(): JSplitPane {
            ui.let {
                if (it is BasicSplitPaneUI) {
                    it.getDivider().setBorder(null)
                }
            }
            setBorder(BorderFactory.createEmptyBorder());
            return this
        }

        val outputSplit = JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            true,
            JScrollPane(source),
            JScrollPane(console)
        ).removeBorders()

        val mainSplit = JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            true,
            JScrollPane(settings, VERTICAL_SCROLLBAR_NEVER, HORIZONTAL_SCROLLBAR_AS_NEEDED),
            outputSplit
        ).removeBorders()

        frame.contentPane = mainSplit

        val cl = Thread.currentThread().contextClassLoader
        frame.setIconImages(
            Arrays.asList(
                ImageIO.read(Objects.requireNonNull(cl.getResource("lwjgl16.png"))),
                ImageIO.read(Objects.requireNonNull(cl.getResource("lwjgl32.png")))
            )
        )

        frame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        frame.pack()
        frame.setLocationRelativeTo(null)

        mainSplit.setDividerLocation(0.5)
        outputSplit.setDividerLocation(0.75)

        frame.extendedState = frame.extendedState or Frame.MAXIMIZED_BOTH
    }

    private fun load(
        header: Header,
        options: Options
    ) {
        includePaths.clear()
        header.includePaths.forEach {
            includePaths.addElement(it.toString())
        }

        this.header.text = header.path.toString()

        module.text = header.module

        prefixConstant.text = header.prefixConstant
        prefixMethod.text = header.prefixMethod

        compilerArgs.text = options.compilerArgs.joinToString(" ")

        mainFileOnly.isSelected = options.mainFileOnly
        ignoreSystemHeaders.isSelected = options.ignoreSystemHeaders
        ignoreErrors.isSelected = options.ignoreErrors
        parseAllComments.isSelected = options.parseAllComments

        renderTypes.isSelected = options.parseTypes
        renderStructs.isSelected = options.parseStructs
        renderConstants.isSelected = options.parseConstants
        renderFunctions.isSelected = options.parseFunctions

        ignoreSystemHeaders.isEnabled = !options.mainFileOnly
        extract.isEnabled = true
    }

    private fun extract() {
        if (header.text.isEmpty()) {
            JOptionPane.showMessageDialog(extract, "Please select a C/C++ header file.", "Error", JOptionPane.ERROR_MESSAGE)
            return
        }

        val header = Header(
            module.text,

            prefixConstant.text,
            prefixMethod.text,

            includePaths.elements()
                .asSequence()
                .map { Paths.get(it) }
                .toList(),
            Paths.get(header.text)
        )

        val options = Options(
            compilerArgs.text.split(' '),

            mainFileOnly.isSelected,
            ignoreSystemHeaders.isSelected,
            ignoreErrors.isSelected,
            parseAllComments.isSelected,

            renderTypes.isSelected,
            renderStructs.isSelected,
            renderConstants.isSelected,
            renderFunctions.isSelected
        )

        val out = ByteArrayOutputStream(16 * 1024)
        val err = ByteArrayOutputStream(4 * 1024)

        try {
            parse(
                header,
                options,
                PrintStream(out),
                PrintStream(err)
            )

            source.text = out.toString("UTF-8")
            console.text = err.toString("UTF-8")
        } catch (t: Throwable) {
            t.printStackTrace()

            err.reset()
            t.printStackTrace(PrintStream(err))

            source.text = ""
            console.text = err.toString("UTF-8")

            JOptionPane.showMessageDialog(frame, "Failed to parse header.", "Parsing error.", JOptionPane.ERROR_MESSAGE)
        }

        source.caretPosition = 0
        console.caretPosition = 0
    }

}