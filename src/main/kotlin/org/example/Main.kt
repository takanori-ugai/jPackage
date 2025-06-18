package org.example

import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.SwingConstants
import javax.swing.SwingUtilities

fun main() {
    println("Hello World!")
    SwingUtilities.invokeLater { createAndShowGUI() }
}

private fun createAndShowGUI() {
    val frame =
        JFrame("Hello Swing").apply {
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            setSize(300, 200)
            add(
                JLabel("Hello, Swing World!").apply {
                    horizontalAlignment = SwingConstants.CENTER
                },
            )
            isVisible = true
        }
}
